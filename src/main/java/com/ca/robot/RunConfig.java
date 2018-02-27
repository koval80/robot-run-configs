package com.ca.robot;

import java.io.*;

public class RunConfig {
    private boolean isSuite;
    private String name;
    private String confFolder;
    private String fileName;
    private String outFolder;

    public static RunConfigBuilder builder() {
        return new RunConfigBuilder();
    }

    private RunConfig(boolean isSuite, String name, String confFolder, String fileName, String outFolder) {
        this.isSuite = isSuite;
        this.name = name;
        this.confFolder = confFolder;
        this.fileName = fileName;
        this.outFolder = outFolder;
    }

    public void writeToFile() throws IOException {
        // (1) Generate the run configuration
        InputStream templateStream = RunConfig.class.getClassLoader().getResourceAsStream(Constants.TEST_CASE_TEMPLATE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(templateStream));

        File outFile = new File(this.outFolder, this.fileName);
        outFile.createNewFile();

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));

        String line = null;
        //todo: escape xml
        //todo: Unique test names
        while ((line = reader.readLine()) != null) {
            line = line.replace(Constants.PLACEHOLDER_NAME, this.name)
                    .replace(Constants.PLACEHOLDER_FOLDER, this.confFolder)
                    .replace(Constants.PLACEHOLDER_TEST_CASE, this.name);
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        reader.close();
        //todo: try, finally
    }

//   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    public static class RunConfigBuilder {
        private boolean isSuite;
        private String name;
        private String confFolder;
        private String fileName;
        private String outFolder;

        public RunConfigBuilder suite(boolean suite) {
            isSuite = suite;
            return this;
        }

        public RunConfigBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RunConfigBuilder confFolder(String confFolder) {
            this.confFolder = confFolder;
            return this;
        }

        public RunConfigBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public RunConfigBuilder outFolder(String outFolder) {
            this.outFolder = outFolder;
            return this;
        }

        public RunConfig build() {
            return new RunConfig(isSuite, name, confFolder, fileName, outFolder);
        }
    }
}