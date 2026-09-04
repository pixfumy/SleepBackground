package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.7 - 1.12: Frame buffer is used for rendering
 */
@Mixin(targets = {"net.minecraft.class_1862", "net.minecraft.client.gl.Framebuffer"}, remap = false)
public class FramebufferMixin {

    @Inject(method = {"method_6894", "bind"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onBegin(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

    @Inject(method = {"method_6900", "unbind"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onEnd(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

    @Inject(method = {"method_6898", "draw"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onDraw(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

}
