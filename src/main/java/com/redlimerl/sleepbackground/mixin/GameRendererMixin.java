package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.logging.LoggingHelper;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // GameRenderer::render
    //  1.3-1.7 -> method_1331/render
    //  1.8-1.12 -> method_9775/renderGui

    @Inject(
            method = {
                    "method_1331",
                    "renderGui",
                    "method_9775",
                    "render"
            },
            at = @At(
                    value = "HEAD"
            ),
            remap = false,
            cancellable = true
    )
    private void onRender(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

}
