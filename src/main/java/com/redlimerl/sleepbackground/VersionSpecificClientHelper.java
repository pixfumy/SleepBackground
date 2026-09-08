package com.redlimerl.sleepbackground;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class VersionSpecificClientHelper {
    private static Class<?> minecraftClientClass;
    private static final Object minecraftClientInstance = initMinecraftClientClassAndInstance();

    private static final MethodHandle clientWorldGetterMethodHandle = createClientWorldGetter();

    public static void init() {

    }

    private static Object initMinecraftClientClassAndInstance() {
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

        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

        try {
            MethodType mt = MethodType.methodType(minecraftClientClass);

            MethodHandle getInstanceMethod = publicLookup.findStatic(minecraftClientClass, getInstanceMethodName, mt);

            return getInstanceMethod.invoke();
        } catch (Throwable e) {
            throw new RuntimeException("MinecraftClient::getInstance / Minecraft::getMinecraft method not found", e);
        }
    }

    private static MethodHandle createClientWorldGetter() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

        try {
            String unmappedMinecraftClientName = mappingResolver.unmapClassName("intermediary", minecraftClientClass.getName());

            String clientWorldClassName =  "net.minecraft.class_478";
            String mappedClientWorldClassName = mappingResolver.mapClassName("intermediary", clientWorldClassName);

            Class<?> clientWorldClass = Class.forName(mappedClientWorldClassName);

            String clientWorldFieldName = mappingResolver.mapFieldName(
                    "intermediary",
                    unmappedMinecraftClientName,
                    "field_3803",
                    "L" + clientWorldClassName.replace(".", "/") + ";"
            );

            return publicLookup.findGetter(minecraftClientClass, clientWorldFieldName, clientWorldClass);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getClientWorldInstance() {
        Object clientWorldInstance;
        try {
            clientWorldInstance = clientWorldGetterMethodHandle.invoke(minecraftClientInstance);
        } catch (Throwable e) {
            throw new RuntimeException("MinecraftClient::world / Minecraft::world field not found", e);
        }
        return clientWorldInstance;
    }

}
