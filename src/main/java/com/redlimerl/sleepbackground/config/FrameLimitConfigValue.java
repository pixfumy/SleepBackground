package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class FrameLimitConfigValue extends ConfigValue {

    private int frameLimit;

    public FrameLimitConfigValue(String keyName, int defaultLimit, String comment) {
        this(keyName, defaultLimit, comment, true);
    }

    public FrameLimitConfigValue(String keyName, int defaultLimit, String comment, boolean defaultEnable) {
        super(keyName, comment, defaultEnable);
        this.frameLimit = defaultLimit;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        if (configObject.has("fps_limit")) {
            this.frameLimit = configObject.get("fps_limit").getAsInt();
            if (this.frameLimit < 1) throw new IllegalArgumentException("The FPS limit should always be 1 or over");
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        configObject.addProperty("fps_limit", this.frameLimit);
    }

    @Nullable
    public Integer getFrameLimit() {
        return this.isEnabled() ? frameLimit : null;
    }
}
