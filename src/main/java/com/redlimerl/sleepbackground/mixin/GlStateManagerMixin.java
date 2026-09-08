package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.8 - 1.12: blaze3d.platform.GlStateManager is used as a GL11 wrapper
 */
@Pseudo
@Mixin(targets = "com.mojang.blaze3d.platform.GlStateManager", remap = false)
public class GlStateManagerMixin {

    @Inject(method = {"clear", "enableCull", "enableTexture"},
            remap = false,
            at = @At("HEAD"),
            cancellable = true,
            require = 3)
    private static void cancelOperation(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

}
