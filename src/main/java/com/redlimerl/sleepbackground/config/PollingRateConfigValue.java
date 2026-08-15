package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class PollingRateConfigValue extends ConfigValue {

    private int pollingRate;

    public PollingRateConfigValue(String keyName, int pollingRate, String comment) {
        super(keyName, comment, false);
        this.pollingRate = pollingRate;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        if (configObject.has("polling_rate")) {
            this.pollingRate = configObject.get("polling_rate").getAsInt();
            if (this.pollingRate < 1) throw new IllegalArgumentException("The Polling Rate should always be 1 or over");
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        configObject.addProperty("polling_rate", this.pollingRate);
    }

    public int getPollingRate() {
        return pollingRate;
    }
}
