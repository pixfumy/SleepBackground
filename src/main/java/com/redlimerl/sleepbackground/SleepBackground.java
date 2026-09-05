package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValues;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {
    public static final String MOD_ID = "sleepbackground";

    public static Integer MINECRAFT_MINOR_VERSION = Integer.parseInt(FabricLoader.getInstance()
            .getModContainer("minecraft")
            .get()
            .getMetadata()
            .getVersion()
            .getFriendlyString()
            .split("\\.")
            [1]
    );

    public static long CLIENT_WORLD_TICK_COUNT = 0;

    /* save this one in a field because we limit both Display$update and GameRenderer$method_1331 (render) based on this
    value. SleepBackground.lastRenderTime is updated when shouldRenderInBackground() returns true, so calling it twice in one frame
    will yield different values.*/
    public static boolean shouldRenderCurrentFrame;

    private static boolean lockExists;
    private static final File LOCK_FILE = new File(FileUtils.getUserDirectory(), "sleepbg.lock");
    private static int lockTick;

    @Override
    public void onInitializeClient() {
        VersionSpecificClientHelper.initVersionSpecificClientFields();

        SleepBackgroundConfig.init();
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

        Integer pollingRate = ConfigValues.POLLING_RATE_LIMIT.getPollingRate();
        if (pollingRate == null) {
            return true;
        }

        long pollTime = 1000 / pollingRate;
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
                if (SleepBackground.lockExists) {
                    Integer value = ConfigValues.LOCKED_INSTANCE_FRAME_RATE.getFrameLimit();
                    if (value != null) {
                        return value;
                    }
                }

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

    public static void tick() {
        Object clientWorldInstance = VersionSpecificClientHelper.getClientWorldInstance();
        CLIENT_WORLD_TICK_COUNT = clientWorldInstance == null ? 0 :
                Math.min(CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());

        if (ConfigValues.LOCKED_INSTANCE_FRAME_RATE.isEnabled()) {
            if (++lockTick >= ConfigValues.LOCKED_INSTANCE_FRAME_RATE.getTickInterval()) {
                lockExists = LOCK_FILE.exists();
                lockTick = 0;
            }
        } else {
            SleepBackground.lockExists = false;
        }
    }
}