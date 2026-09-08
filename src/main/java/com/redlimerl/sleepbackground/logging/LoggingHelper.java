package com.redlimerl.sleepbackground.logging;

import com.redlimerl.sleepbackground.SleepBackground;

public class LoggingHelper {
    public static java.util.logging.Logger JAVA_UTIL_LOGGER = java.util.logging.Logger.getLogger(SleepBackground.MOD_ID);

    public static void info(String logMessage) {
       if (SleepBackground.MINECRAFT_MINOR_VERSION <= 6) {
           JAVA_UTIL_LOGGER.info(logMessage);
       } else {
           Log4JAccessor.info(logMessage);
       }
    }

    public static void warn(String logMessage) {
        if (SleepBackground.MINECRAFT_MINOR_VERSION <= 6) {
            JAVA_UTIL_LOGGER.warning(logMessage);
        } else {
            Log4JAccessor.warn(logMessage);
        }
    }

    public static void error(String logMessage) {
        if (SleepBackground.MINECRAFT_MINOR_VERSION <= 6) {
            JAVA_UTIL_LOGGER.severe(logMessage);
        } else {
            Log4JAccessor.error(logMessage);
        }
    }

}
