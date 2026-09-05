package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.util.snooper.Snooper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This one is an ordinary mixin, as the class name and method name is the same from 1.3-1.13
 */
@Mixin(Snooper.class)
public class SnooperMixin {

    @Inject(method = "addCpuInfo", at = @At("HEAD"), cancellable = true)
    private void onAddCpuInfo(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) ci.cancel();
    }

}
