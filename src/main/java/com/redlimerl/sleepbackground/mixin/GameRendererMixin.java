package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // GameRenderer::render
    //  1.3-1.7 -> method_1331
    //  1.8-1.12 -> method_9775

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(
            method = {
                    "method_1331",
                    "method_9775",
                    "render"
            },
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = false
    )
    private void onRender(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderCurrentFrame) {
            ci.cancel();
        }
    }

}
