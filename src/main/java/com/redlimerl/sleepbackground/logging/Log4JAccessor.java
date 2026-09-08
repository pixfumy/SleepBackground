package com.redlimerl.sleepbackground.logging;

import com.redlimerl.sleepbackground.SleepBackground;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that interfaces with log4j only in environments where it exists - avoids a runtime error in environments
 * where it doesn't exist.
 */
public class Log4JAccessor {
    public static final Logger LOGGER = LogManager.getLogger(SleepBackground.MOD_ID);

    public static void info(String logMessage) {
        LOGGER.info(logMessage);
    }

    public static void warn(String logMessage) {
        LOGGER.warn(logMessage);
    }

    public static void error(String logMessage) {
        LOGGER.error(logMessage);
    }
}
