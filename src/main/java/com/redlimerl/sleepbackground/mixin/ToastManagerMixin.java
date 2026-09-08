package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.12 only
 *
 * From minecraft wiki: "Toasts are informational text boxes shown on the screen.
 * They typically show up when the player unlocks new crafting recipes, grants advancements, or a song starts playing,
 * and there are informational messages that can be of any type."
 */
@Pseudo
@Mixin(targets = "net.minecraft.class_3264", remap = false)
public class ToastManagerMixin {

    @Inject(method = {"method_14490"}, at = @At("HEAD"), cancellable = true, remap = false)
    public void onDraw(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) ci.cancel();
    }

}
