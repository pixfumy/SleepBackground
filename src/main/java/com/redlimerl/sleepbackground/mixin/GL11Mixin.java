package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.3 - 1.7: GL11 is called directly from MC code
 */
@Pseudo
@Mixin(targets = "org.lwjgl.opengl.GL11", remap = false)
public class GL11Mixin {

    @Inject(method = {"glClear", "glEnable", "glFlush"},
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
