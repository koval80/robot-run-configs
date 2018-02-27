package com.ca.robot.conzt;

public interface Messages {
    String CANT_CREATE_TEMP_FOLDER = "Can't create temp folder: [%s]";
    String USUPPORTED_LOG_LEVEL = "Unsupported log level: [%s]. Use one of: DEBUG, INFO";
    String NOT_2_ARGUMENTS = "Exactly 2 arguments should be provided. Found: [%s]";
    String FOLDER_DOESNT_EXIST = "Folder does not exist: [%s]";
    String PARSING_CLI_ARGS_FAILED = "Parsing failed: %s \n%s";
}
