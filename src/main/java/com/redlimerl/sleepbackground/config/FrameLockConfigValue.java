package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class FrameLockConfigValue extends FrameLimitConfigValue {

    private int tickInterval;

    public FrameLockConfigValue(String keyName, int defaultValue, int defaultInterval, String comment) {
        super(keyName, defaultValue, comment, false);
        this.tickInterval = defaultInterval;

    }

    public int getTickInterval() {
        return this.isEnabled() ? tickInterval : 20;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        super.loadToInit(configObject);
        if (configObject.has("tick_interval")) {
            this.tickInterval = configObject.get("tick_interval").getAsInt();
            if (this.tickInterval < 1) throw new IllegalArgumentException("The Tick Interval should always be 1 or over");
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        super.writeToJson(configObject);
        configObject.addProperty("tick_interval", this.tickInterval);
    }
}
