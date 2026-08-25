package com.redlimerl.sleepbackground;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VersionSpecificClientHelper {
    private static Class<?> minecraftClientClass;
    private static Object minecraftClientInstance;

    private static Field clientWorldField;

    public static Class<?> clientLoggerClass;
    private static Method clientGetLogManagerMethod;

    private static Field clientCurrentScreenField;

    public static void initVersionSpecificClientFields() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

        try {

            String minecraftClientClassName = mappingResolver.mapClassName(
                    "intermediary",
                    "net.minecraft.class_1600"
            );
            minecraftClientClass = Class.forName(minecraftClientClassName);
        } catch (ClassNotFoundException e) {
            try {
                // 1.3-1.5
                minecraftClientClass = Class.forName("net.minecraft.client.Minecraft");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("net.minecraft.client.Minecraft / MinecraftClient class not found", ex);
            }
        }

        String unmappedMinecraftClientName = mappingResolver.unmapClassName("intermediary", minecraftClientClass.getName());
        String getInstanceMethodName = mappingResolver.mapMethodName(
                "intermediary",
                unmappedMinecraftClientName,
                "method_2965",
                "()L" + unmappedMinecraftClientName.replace(".", "/") + ";"
        );

        try {
            minecraftClientInstance = minecraftClientClass.getMethod(getInstanceMethodName).invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("MinecraftClient::getInstance / Minecraft::getMinecraft method not found", e);
        }

        try {
            String clientWorldClassName =  "net.minecraft.class_478";

            String clientWorldFieldName = mappingResolver.mapFieldName(
                    "intermediary",
                    unmappedMinecraftClientName,
                    "field_3803",
                    "L" + clientWorldClassName.replace(".", "/") + ";"
            );
            clientWorldField = minecraftClientClass.getField(clientWorldFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        String unmappedClientLoggerClassName = "net.minecraft.class_1555";
        String clientLoggerClassName = mappingResolver.mapClassName(
                "intermediary",
                unmappedClientLoggerClassName
        );

        try {
            String screenClassName = "net.minecraft.class_388";

            String clientCurrentScreenFieldName = mappingResolver.mapFieldName(
                    "intermediary",
                    unmappedMinecraftClientName,
                    "field_3816",
                    "L" + screenClassName.replace(".", "/") + ";"
            );

            clientCurrentScreenField = minecraftClientClass.getDeclaredField(clientCurrentScreenFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        try {
            clientLoggerClass = Class.forName(clientLoggerClassName);
        } catch (ClassNotFoundException e) {
            LoggingHelper.info("Could not find MinecraftClient.LogManager, Minecraft Version is 1."
                    + SleepBackground.MINECRAFT_MINOR_VERSION + ".x. If your game is still running at this point, this is fine.");
            return;
        }

        try {
            String unmappedSnoopableClassName = "net.minecraft.class_855";
            String getLogManagerMethodName = mappingResolver.mapMethodName(
                    "intermediary",
                    unmappedSnoopableClassName,
                    "method_5352",
                    "()L" + unmappedClientLoggerClassName.replace(".", "/") + ";"
            );
            clientGetLogManagerMethod = minecraftClientClass.getDeclaredMethod(getLogManagerMethodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Minecraft jar has class LogManager but the method MinecraftClient$getLogManager" +
                    " was not found. Namespace is " + FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace() + ", "
                    + "Minecraft Version is 1." + SleepBackground.MINECRAFT_MINOR_VERSION + ".x");
        }
    }

    public static Object getClientWorldInstance() {
        Object clientWorldInstance;
        try {
            clientWorldInstance = clientWorldField.get(minecraftClientInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MinecraftClient::world / Minecraft::world field not found", e);
        }
        return clientWorldInstance;
    }

    public static Object getClientLogManagerInstance() {
        Object clientLogManager;
        try {
            clientLogManager = clientGetLogManagerMethod.invoke(minecraftClientInstance);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("MinecraftClient::LogManager field not found", e);
        }
        return clientLogManager;
    }

    public static Object getClientCurrentScreenInstance() {
        Object currentScreen;
        try {
            currentScreen = clientCurrentScreenField.get(minecraftClientInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MinecraftClient::LogManager field not found", e);
        }
        return currentScreen;
    }
}
