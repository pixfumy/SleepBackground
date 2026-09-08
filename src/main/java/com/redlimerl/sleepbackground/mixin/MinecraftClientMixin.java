package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ClassNotFoundException")
@Pseudo
@Mixin(targets = {
            "net.minecraft.class_1600",
            "net.minecraft.client.MinecraftClient",
            "net.minecraft.client.Minecraft"
        },
        remap = false)
public class MinecraftClientMixin {
    @Inject(method = {"runGameLoop"}, at = @At("HEAD"))
    public void onRender(CallbackInfo ci) {
        SleepBackground.shouldRenderCurrentFrame = SleepBackground.shouldRenderInBackground();
    }

    @Inject(method = {"tick"}, at = @At("TAIL"))
    private void tickSleepBackground(CallbackInfo ci) {
        SleepBackground.tick();
    }

    /* 1.3 - 1.6: This Redirect has 2 targets in method_2916/runGameLoop but only one of them will run per
       frame depending on whether or not F7 is being held. The behaviour seems identical for both calls. Probably
       debugging logic added by Mojang that they forgot to remove.

       1.7: runGameLoop calls method_6648 which calls Display.update(), so we redirect there.
       1.8 - 1.12:  runGameLoop calls method_9403/updateDisplay which calls Display.update(), so we redirect there.
    */
    @Redirect(method = {"runGameLoop", "method_6648", "method_9403", "updateDisplay"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;update()V"), remap = false)
    private void wrapDisplayUpdate() {
        if (SleepBackground.shouldRenderCurrentFrame) {
            Display.update();
        } else if (SleepBackground.shouldPollMouse()) {
            Display.processMessages();
        }
    }

}
