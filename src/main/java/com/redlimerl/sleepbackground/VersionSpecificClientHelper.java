package com.redlimerl.sleepbackground;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class VersionSpecificClientHelper {
    public static Class<?> minecraftClientClass;
    public static Object minecraftClientInstance;

    public static Field clientWorldField;

    public static void initVersionSpecificClientFields() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

        try {
            // 1.6-1.12
            // class_1600 -> MinecraftClient
            SleepBackgroundConfig.init();

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
            clientWorldField = minecraftClientClass.getDeclaredField(clientWorldFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
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
}
