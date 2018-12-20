package com.photogroup.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {

    public static String LEVEL_INFO = "LEVEL_INFO";

    public static String LEVEL_DEBUG = "LEVEL_DEBUG";

    public static String LEVEL_ERROR = "LEVEL_ERROR";

    private static Map<String, Object> logs = new HashMap<String, Object>();

    public static void log(String logLevel, String message) {
        logs.put(logLevel, message);
    }

    public static void log(String logLevel, Throwable exception) {
        logs.put(logLevel, exception);
        output(exception.toString());
    }

    public static void logError(String message) {
        logs.put(LEVEL_ERROR, message);
        output(message);
    }

    public static void logInfo(String message) {
        logs.put(LEVEL_INFO, message);
        output(message);
    }

    public static void logDebug(String message) {
        logs.put(LEVEL_DEBUG, message);
        output(message);
    }

    private static void output(String messgae) {
        System.out.println(messgae);
    }
}
