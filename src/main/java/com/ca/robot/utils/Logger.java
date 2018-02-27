package com.ca.robot.utils;

import java.io.PrintStream;

public class Logger {
    static final PrintStream OUT_STREAM = System.out;
    static final PrintStream ERROR_STREAM = System.err;

    final private boolean debug;

    public Logger(boolean debug) {
        this.debug = debug;
    }

    public void debug(String msg){
        if (debug){
            OUT_STREAM.println(msg);
        }
    }

    public void error(String msg) {
        ERROR_STREAM.println(msg);
    }

    public void info(String msg) {
        OUT_STREAM.println(msg);
    }
}