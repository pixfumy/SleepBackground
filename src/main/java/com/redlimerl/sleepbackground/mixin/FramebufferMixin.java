package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.7 - 1.12: Frame buffer is used for rendering
 */
@Mixin(targets = "net.minecraft.class_1862", remap = false)
public class FramebufferMixin {

    @Inject(method = "bind", at = @At("HEAD"), cancellable = true)
    private void onBegin(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

    @Inject(method = "endWrite", at = @At("HEAD"), cancellable = true)
    private void onEnd(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

    @Inject(method = "draw(II)V", at = @At("HEAD"), cancellable = true)
    private void onDraw(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

}
