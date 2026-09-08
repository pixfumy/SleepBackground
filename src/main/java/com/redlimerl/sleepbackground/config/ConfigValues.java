package com.redlimerl.sleepbackground.config;

import java.util.HashSet;

public class ConfigValues {

    public static final HashSet<ConfigValue> ALL_CONFIGS = new HashSet<>();

    public static final FrameLimitConfigValue BACKGROUND_FRAME_RATE =
            new FrameLimitConfigValue("background", 1, "Default background fps when tabbed out and not hovering over an instance.");

    /* Unlike in 1.14+, Loading screen renders at 5fps so configurable loading screen frame rate is not needed.
    In situations where WorldPreview exists pre1.14, it handles the loading screen framerate as its own config value.
     */

    public static final FrameLockConfigValue LOCKED_INSTANCE_FRAME_RATE =
            new FrameLockConfigValue("lock_instance", 1, 20,
                    "Frame rate for background instances if using macros that create a sleepbg.lock file.");

    public static final FrameTickConfigValue WORLD_INITIAL_FRAME_RATE =
            new FrameTickConfigValue("world_setup", 10, 20, "same with (background) config but for (max_ticks) ticks after joining the world.");

    public static final PollingRateConfigValue POLLING_RATE_LIMIT = new PollingRateConfigValue("polling_rate_limit", 15, "display rate for updating the window and polling input devices when in background.");

    static {
        ALL_CONFIGS.add(BACKGROUND_FRAME_RATE);
        ALL_CONFIGS.add(LOCKED_INSTANCE_FRAME_RATE);
        ALL_CONFIGS.add(WORLD_INITIAL_FRAME_RATE);
        ALL_CONFIGS.add(POLLING_RATE_LIMIT);
    }
}
