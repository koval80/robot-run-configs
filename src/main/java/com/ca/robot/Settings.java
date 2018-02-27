package com.ca.robot;

import com.ca.robot.conzt.Messages;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class Settings {

    private static final Options OPTIONS = createOptions();
    private static final String LB = System.getProperty("line.separator");
    private static final String PADDING = "     ";
    private static final String HELP_FOOTER = "";
    private static final String COMMAND_NAME = "RunConfigCreator";
    private static final String SRC_FOLDER = "TESTS_FOLDER";
    private static final String TARGET_FOLDER = "RUN_CONFIGURATIONS_FOLDER";
    private static final String HELP_HEADER = PADDING + SRC_FOLDER + PADDING
            + "The folder which contains the test files" + LB
            + PADDING + TARGET_FOLDER + PADDING
            + "The folder to create run configurations in" + LB;

    private final String srcFolder;
    private final String targetFolder;
    private final boolean isLogDebug;
    private final String tempFolder;
    private final String testSuiteArguments;
    private final String testCaseArguments;

    public Settings(String srcFolder, String targetFolder, boolean isLogDebug, String tempFolder,
                    String testSuiteArguments, String testCaseArguments) {
        this.srcFolder = srcFolder;
        this.targetFolder = targetFolder;
        this.isLogDebug = isLogDebug;
        this.tempFolder = tempFolder;
        this.testSuiteArguments = testSuiteArguments;
        this.testCaseArguments = testCaseArguments;
    }

    public String getSrcFolder() {
        return srcFolder;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public boolean isLogDebug() {
        return isLogDebug;
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public String getTestSuiteArguments() {
        return testSuiteArguments;
    }

    public String getTestCaseArguments() {
        return testCaseArguments;
    }

    public static Settings fromArguments(String[] args) {
        CommandLine cli = createCli(args);

        String tempFolder = calculateTempFolder(cli);
        boolean isLogDebug = calculateIsDebug(cli);
        String testSuiteArguments = cli.getOptionValue('s');
        if (testSuiteArguments == null) {
            testSuiteArguments = "";
        }

        String testCaseArguments = cli.getOptionValue('c');
        if (testCaseArguments == null) {
            testCaseArguments = "";
        }

        // Arguments (not options)
        List<String> argList = cli.getArgList();
        if (argList.size() != 2) {
            String msg = String.format(Messages.NOT_2_ARGUMENTS, String.join(", ", argList));
            throw new IllegalArgumentException(msg);
        }

        String srcFolder = argList.get(0);
        String targetFolder = argList.get(1);
        validateFolderExists(srcFolder);
        validateFolderExists(targetFolder); //todo: create targetFolder

        return new Settings(srcFolder, targetFolder, isLogDebug, tempFolder, testSuiteArguments, testCaseArguments);
    }

    private static void validateFolderExists(String folder) {
        if ( ! new File(folder).exists() ) {
            String msg = String.format(Messages.FOLDER_DOESNT_EXIST, folder);
            throw new IllegalArgumentException(msg);
        }
    }

    private static boolean calculateIsDebug(CommandLine cli) {
        if (!cli.hasOption('l')) {
            return false;
        }
        String logLevel = cli.getOptionValue('l');
        if ( "DEBUG".equalsIgnoreCase(logLevel) ) {
            return true;
        }

        if ( "INFO".equalsIgnoreCase(logLevel) ) {
            String msg = String.format(Messages.USUPPORTED_LOG_LEVEL, logLevel);
            throw new IllegalArgumentException(msg);
        }
        return false;
    }

    private static String calculateTempFolder(CommandLine cli) {
        if ( cli.hasOption('t') ) {
            String tempFolderName = cli.getOptionValue('t');
            File tempFolder = new File(tempFolderName);
            if (!tempFolder.exists()) {
                boolean folderCreated = tempFolder.mkdirs();
                if (!folderCreated) {
                    String msg = String.format(Messages.CANT_CREATE_TEMP_FOLDER, tempFolderName);
                    throw new IllegalArgumentException(msg);
                }
            }
            return tempFolder.getAbsolutePath();
        }

        return System.getProperty("java.io.tmpdir");
    }

    private static String getHelp() {
        HelpFormatter formatter = new HelpFormatter();
        //formatter.setArgName("yay");


        // Create usage
        StringWriter usageStringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(usageStringWriter);
        formatter.printUsage(pw, formatter.getWidth(), "RunConfigCreator", OPTIONS);
        pw.flush();
        StringBuffer usageStringBuffer = usageStringWriter.getBuffer();
        String usage = usageStringBuffer
                .delete(usageStringBuffer.length() - LB.length(), usageStringBuffer.length()) //remove linebreak
                .append(' ').append("TESTS_FOLDER")
                .append("  ").append("RUN_CONFIGURATIONS_FOLDER")
                .append(LB)
                .toString();

        //create help
        StringWriter helpStringWriter = new StringWriter();
        pw = new PrintWriter(helpStringWriter);
        formatter.printHelp(pw, 400, COMMAND_NAME, HELP_HEADER, OPTIONS, formatter.getLeftPadding(), formatter.getDescPadding(), HELP_FOOTER, false);

        StringBuffer helpStringBuffer = helpStringWriter.getBuffer();
        int firstLineLength = helpStringBuffer.indexOf(LB) + LB.length();
        helpStringBuffer.delete(0, firstLineLength);

        return usageStringBuffer
                .append(helpStringBuffer)
                .toString();
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("t", "tempFolder", true, "A folder to generate temporary files in");
        options.addOption("s", "testSuiteArgs", true, "Additional text to add at the \"Arguments\" field of test suites");
        options.addOption("c", "testCaseArgs", true, "Additional text to add at the \"Arguments\" field of test cases");
        options.addOption("l", "logLevel", true, "One of: INFO, DEBUG");

        return options;
    }

    private static CommandLine createCli(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse( OPTIONS, args );
        }
        catch( ParseException ex ) {
            String msg = String.format(Messages.PARSING_CLI_ARGS_FAILED, ex.getMessage(), getHelp());
            throw new IllegalArgumentException(msg, ex);
        }
    }
}