package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.12 only
 */
@Mixin(targets = "net.minecraft.class_3264", remap = false)
public class ToastManagerMixin {

    @Inject(method = {"method_14490"}, at = @At("HEAD"), cancellable = true, remap = false)
    public void onDraw(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) ci.cancel();
    }

}
