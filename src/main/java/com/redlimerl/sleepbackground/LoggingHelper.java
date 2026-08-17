package com.redlimerl.sleepbackground;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.reflect.InvocationTargetException;

public class LoggingHelper {
    public static java.util.logging.Logger JAVA_UTIL_LOGGER = java.util.logging.Logger.getLogger(SleepBackground.MOD_ID);
    public static org.apache.logging.log4j.Logger LOG4J_LOGGER = org.apache.logging.log4j.LogManager.getLogger(SleepBackground.MOD_ID);

    public static void info(String logMessage) {
        logMessage = "[" + SleepBackground.MOD_ID + "] " + logMessage;
        String version = SleepBackground.MINECRAFT_VERSION;

        if (version.startsWith("1.3") || version.startsWith("1.4")) { // use java.util.logging.Logger
            JAVA_UTIL_LOGGER.info(logMessage);
        } else if (version.startsWith("1.5") || version.startsWith("1.6")) { // use net.minecraft.util.logging.LogManager
            Object loggerInstance = VersionSpecificClientHelper.getClientLogManagerInstance();
            Class<?> loggerClass = loggerInstance.getClass();

            MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
            String infoMethodName = mappingResolver.mapMethodName(
                    "intermediary",
                    mappingResolver.unmapClassName("intermediary", VersionSpecificClientHelper.clientLoggerClass.getName()),
                    "method_5331",
                    "(Ljava/lang/String;)V"
            );

            try {
                loggerClass.getMethod(infoMethodName, String.class).invoke(loggerInstance, logMessage);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("MinecraftClient$LogManager$info failed", e);
            }
        } else { // for 1.7+, Minecraft uses org.apache.logging.log4j.Logger
            LOG4J_LOGGER.info(logMessage);
        }
    }

    public static void warn(String logMessage) {
        logMessage = "[" + SleepBackground.MOD_ID + "] " + logMessage;
        String version = SleepBackground.MINECRAFT_VERSION;

        if (version.startsWith("1.3") || version.startsWith("1.4")) { // use java.util.logging.Logger
            JAVA_UTIL_LOGGER.warning(logMessage);
        } else if (version.startsWith("1.5") || version.startsWith("1.6")) { // use net.minecraft.util.logging.LogManager
            Object loggerInstance = VersionSpecificClientHelper.getClientLogManagerInstance();
            Class<?> loggerClass = loggerInstance.getClass();

            MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
            String infoMethodName = mappingResolver.mapMethodName(
                    "intermediary",
                    mappingResolver.unmapClassName("intermediary", VersionSpecificClientHelper.clientLoggerClass.getName()),
                    "method_5332",
                    "(Ljava/lang/String;)V"
            );

            try {
                loggerClass.getMethod(infoMethodName, String.class).invoke(loggerInstance, logMessage);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("MinecraftClient$LogManager$info failed", e);
            }
        } else { // for 1.7+, Minecraft uses org.apache.logging.log4j.Logger
            LOG4J_LOGGER.warn(logMessage);
        }
    }

    public static void error(String logMessage) {
        logMessage = "[" + SleepBackground.MOD_ID + "] " + logMessage;
        String version = SleepBackground.MINECRAFT_VERSION;

        if (version.startsWith("1.3") || version.startsWith("1.4")) { // use java.util.logging.Logger
            JAVA_UTIL_LOGGER.severe(logMessage);
        } else if (version.startsWith("1.5") || version.startsWith("1.6")) { // use net.minecraft.util.logging.LogManager
            Object loggerInstance = VersionSpecificClientHelper.getClientLogManagerInstance();
            Class<?> loggerClass = loggerInstance.getClass();

            MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
            String infoMethodName = mappingResolver.mapMethodName(
                    "intermediary",
                    mappingResolver.unmapClassName("intermediary", VersionSpecificClientHelper.clientLoggerClass.getName()),
                    "method_5335",
                    "(Ljava/lang/String;)V"
            );

            try {
                loggerClass.getMethod(infoMethodName, String.class).invoke(loggerInstance, logMessage);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("MinecraftClient$LogManager$info failed", e);
            }
        } else { // for 1.7+, Minecraft uses org.apache.logging.log4j.Logger
            LOG4J_LOGGER.error(logMessage);
        }
    }
}
