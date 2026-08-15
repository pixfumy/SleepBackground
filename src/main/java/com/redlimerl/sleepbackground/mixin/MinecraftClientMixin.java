package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.VersionSpecificClientHelper;
import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ClassNotFoundException")
@Mixin(targets = {
            "net.minecraft.class_1600",
            "net.minecraft.client.MinecraftClient",
            "net.minecraft.client.Minecraft"
        },
        remap = false)
public class MinecraftClientMixin {

    @Inject(method = {"method_2916", "runGameLoop"}, at = @At("HEAD"), remap = false)
    public void onRender(CallbackInfo ci) {
        SleepBackground.shouldRenderCurrentFrame = SleepBackground.shouldRenderInBackground();
    }

    @Inject(method = {"method_2954", "tick"}, at = @At("TAIL"), remap = false)
    private void incrementWorldTickCount(CallbackInfo ci) {
        Object clientWorldInstance = VersionSpecificClientHelper.getClientWorldInstance();
        SleepBackground.CLIENT_WORLD_TICK_COUNT = clientWorldInstance == null ? 0 :
                Math.min(SleepBackground.CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());
    }

    /* 1.3 - 1.6: This Redirect has 2 targets but only one of them will run per frame depending on whether or not F7 is being held.
    The behaviour seems identical for both calls. Probably debugging logic added by Mojang that they forgot to remove.

       1.7+: runGameLoop calls method_6648 calls Display.update(), so we redirect there.
    */
    @Redirect(method = {"method_2916", "runGameLoop", "method_6648"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;update()V"), remap = false)
    private void wrapDisplayUpdate() {
        if (SleepBackground.shouldRenderCurrentFrame) {
            Display.update();
        } else if (SleepBackground.shouldPollMouse()) {
            Display.processMessages();
        }
    }
}
