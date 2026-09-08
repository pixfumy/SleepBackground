package com.redlimerl.sleepbackground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.redlimerl.sleepbackground.config.ConfigValue;
import com.redlimerl.sleepbackground.config.ConfigValues;
import com.redlimerl.sleepbackground.logging.LoggingHelper;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SleepBackgroundConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve("sleepbg.json").toFile();

        if (configFile.exists()) {
            try {
                JsonObject jsonObject = new JsonParser().parse(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8)).getAsJsonObject();
                for (ConfigValue configValue : ConfigValues.ALL_CONFIGS) {
                    try {
                        configValue.load(jsonObject);
                    } catch (Throwable e2) {
                        e2.printStackTrace();
                        LoggingHelper.error("Failed to load '"+configValue.getKeyName()+"'");
                    }
                }
            } catch (Throwable e1) {
                e1.printStackTrace();
                LoggingHelper.error("Failed to read config file");
            }
        }

        JsonObject writeObject = new JsonObject();
        for (ConfigValue configValue : ConfigValues.ALL_CONFIGS) {
            try {
                configValue.writeToJsonObject(writeObject);
            } catch (Throwable e2) {
                e2.printStackTrace();
                LoggingHelper.error("Failed to write '"+configValue.getKeyName()+"'");
            }
        }

        LoggingHelper.info("FPS limit in the background has been initalized.");
        LoggingHelper.info("> Config data");
        LoggingHelper.info(GSON.toJson(writeObject));

        try {
            FileUtils.writeStringToFile(configFile, GSON.toJson(writeObject), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            LoggingHelper.error("Failed to write config file");
        }
    }
}
