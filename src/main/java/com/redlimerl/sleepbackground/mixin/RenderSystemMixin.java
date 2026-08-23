package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.3 - 1.7: GL11 is called directly from MC code
 * 1.8 - 1.12: blaze3d.platform.GlStateManager is used as a GL11 wrapper
 */
@Mixin(targets = {"org.lwjgl.opengl.GL11", "com.mojang.blaze3d.platform.GlStateManager"}, remap = false)
public class RenderSystemMixin {

    @Inject(method = {"glClear", "glEnable", "glFlush", "glPushMatrix", "glPopMatrix"},
            remap = false,
            at = @At("HEAD"),
            cancellable = true)
    private static void cancelOperation1_3To1_7(CallbackInfo ci) {
        String version = SleepBackground.MINECRAFT_VERSION;

        if (version.startsWith("1.3") || version.startsWith("1.4") || version.startsWith("1.5") || version.startsWith("1.6") ||
             version.startsWith("1.7")) {
            if (!SleepBackground.shouldRenderCurrentFrame) {
                ci.cancel();
            }
        }
    }

    @Inject(method = {"clear", "enableCull", "enableTexture", "pushMatrix", "popMatrix"},
            remap = false,
            at = @At("HEAD"),
            cancellable = true)
    private static void cancelOperation1_8To1_12(CallbackInfo ci) {
        String version = SleepBackground.MINECRAFT_VERSION;
        if (version.startsWith("1.8") || version.startsWith("1.9") || version.startsWith("1.10") || version.startsWith("1.11") ||
                version.startsWith("1.12")) {
            if (!SleepBackground.shouldRenderCurrentFrame) {
                ci.cancel();
            }
        }
    }

}
