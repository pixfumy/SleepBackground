package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.logging.LoggingHelper;
import net.minecraft.client.render.LoadingScreenRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingScreenRenderer.class)
public class LoadingScreenRendererMixin {
       /**
        * Explicitly render each loading screen frame to keep runs verifiable and to not break wall macros that use
        * pixel detection.
        *
        * Normally the render flag is updated in MinecraftClient$runGameLoop, but runGameLoop is not running
        * during the entirety of the loading screen.
        */
       @Inject(method = {"method_884", "setProgressPercentage"}, at = @At("HEAD"), require = 2)
       private void setShouldRenderToTrue(CallbackInfo ci) {
              SleepBackground.shouldRenderCurrentFrame = true;
       }
}
