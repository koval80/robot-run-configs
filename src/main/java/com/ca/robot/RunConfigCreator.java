package com.ca.robot;

import com.ca.robot.model.TestCase;
import com.ca.robot.model.TestData;
import com.ca.robot.model.TestModel;
import com.ca.robot.model.TestSuite;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.robotframework.RobotFramework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class RunConfigCreator {

    public static final String UNIQUE_ID = String.valueOf(System.currentTimeMillis());
    public static final String JSON_PREFIX = "testdoc = ";

    public static void main(String[] args) {
        String testsRootFolder = "C:\\code\\rp-plugins\\integrationTests\\src\\main\\cdd-plugins";
        String tempFolder = "c:\\temp";
        String outFolder = "c:\\temp\\runConfigs";

        args = new String[]{testsRootFolder, outFolder};
        Settings settings = null;
        try {
            settings = Settings.fromArguments(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(e.getMessage().hashCode());
        }

        if(1==0){
            return;
        }

        String htmlReportFilePath = generateRobotHtmlReport(settings.getSrcFolder(), settings.getTempFolder());
        String testModelJson = generateJsonTestModel(htmlReportFilePath);
        //System.out.println(testModelJson);

        TestModel testModel = parseTestModel(testModelJson);
        //System.out.println(testModel);

        if ( outFolder == null || testModel.getSuite() == null ) {
            //todo: log
            System.out.println("I found nothing !");
            return;
        }

        clearOutFolder(outFolder);
        createRunConfigurations(testModel.getSuite(), outFolder);
    }

    private static void createRunConfigurations(TestSuite suite, String outFolder) {

        if ( !suite.getTests().isEmpty() ) {
            String confFolderName = String.format("%s (%d)", suite.getName(), suite.getNumberOfTests());
            //todo: suite
            for (TestCase testCase : suite.getTests()) {
                createRunConfiguration(testCase, confFolderName, outFolder);
            }
        }

        for (TestSuite subSuite : suite.getSuites()) {
            createRunConfigurations(subSuite, outFolder);
        }
    }

    private static void createRunConfiguration(TestData test, String confFolderName, String outFolder) {
        String runConfigName = test.getName();
        String fileName = test.getId() + ".xml";

        String msg = String.format("Creating runconfig named: %s, in folder: %s, in file: %s",
                runConfigName, confFolderName, fileName);
        System.out.println(msg);

        RunConfig runConfig = RunConfig.builder()
                .suite(false)
                .name(runConfigName)
                .confFolder(confFolderName)
                .fileName(fileName)
                .outFolder(outFolder)
                .build();

        try {
            runConfig.writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearOutFolder(String outFolder) {
        //stub
    }

    private static String generateRobotHtmlReport(String testsRootFolder, String tempFolder) {
        String outputHtml = tempFolder + File.separator + "tests.html";
        RobotFramework.run(new String[] {"testdoc", testsRootFolder, outputHtml});

        return outputHtml;
    }

    private static String generateJsonTestModel(String htmlTestDocFilePath) {
        try {
            Scanner scanner = new Scanner(new File(htmlTestDocFilePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(JSON_PREFIX)) {
                    line = line
                            .substring(JSON_PREFIX.length(), line.length() - 1) // also, remove ; from the end
                            .replace("\\x3c", "<")
                            .replace("\\x3C", "<");
                    return line;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static TestModel parseTestModel(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(json, TestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}