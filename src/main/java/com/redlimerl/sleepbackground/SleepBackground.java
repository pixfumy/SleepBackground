package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValues;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {
    public static Logger LOGGER = LogManager.getLogger("sleepbackground");

    public static long CLIENT_WORLD_TICK_COUNT = 0;

    /* save this one in a field because we limit both Display$update and GameRenderer$method_1331 (render) based on this
    value. SleepBackground.lastRenderTime is updated when shouldRenderInBackground() returns true, so calling it twice in one frame
    will yield different values.*/
    public static boolean shouldRenderCurrentFrame;

    @Override
    public void onInitializeClient() {
        SleepBackgroundConfig.init();

        VersionSpecificClientHelper.initVersionSpecificClientFields();
    }

    private static long lastRenderTime = 0;
    public static boolean shouldRenderInBackground() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRender = currentTime - lastRenderTime;

        Integer targetFPS = getBackgroundFPS();
        if (targetFPS == null) return true;

        long frameTime = 1000 / targetFPS;

        if (timeSinceLastRender < frameTime) {
            idle(frameTime);
            return false;
        }

        lastRenderTime = currentTime;
        return true;
    }

    private static long lastPollTime;
    public static boolean shouldPollMouse() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastPoll = currentTime - lastPollTime;
        long pollTime = 1000 / ConfigValues.POLLING_RATE_LIMIT.getPollingRate();
        if (timeSinceLastPoll < pollTime) {
            return false;
        }
        lastPollTime = currentTime;
        return true;
    }

    private static void idle(long waitMillis) {
        waitMillis = Math.min(waitMillis, 30L);
        LockSupport.parkNanos("waiting to render", waitMillis * 1000000L);
    }

    @Nullable
    private static Integer getBackgroundFPS() {

        if (!Display.isActive() && !Mouse.isInsideWindow()) {
            Object clientWorldInstance = VersionSpecificClientHelper.getClientWorldInstance();
            if (clientWorldInstance != null) {

                if (ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks() > CLIENT_WORLD_TICK_COUNT) {
                    Integer value = ConfigValues.WORLD_INITIAL_FRAME_RATE.getFrameLimit();
                    if (value != null) return value;
                }

                return ConfigValues.BACKGROUND_FRAME_RATE.getFrameLimit();
            }

            return null;
        }
        return null;
    }
}