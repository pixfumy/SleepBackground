package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SleepBackgroundMixinConfigPlugin implements IMixinConfigPlugin {
    public void onLoad(String mixinPackage) {

    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("com.redlimerl.sleepbackground.mixin.GL11Mixin")) {
            return SleepBackground.MINECRAFT_MINOR_VERSION <= 7;
        } else if (mixinClassName.equals("com.redlimerl.sleepbackground.mixin.GlStateManagerMixin")) {
            return SleepBackground.MINECRAFT_MINOR_VERSION > 7;
        }
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    public List<String> getMixins() {
        return new ArrayList<>();
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
