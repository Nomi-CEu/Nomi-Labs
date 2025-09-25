package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.client.settings.GameSettings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;

/**
 * Allows setting a default mipmap level.
 */
@Mixin(GameSettings.class)
public class GameSettingsMixin {

    @Shadow
    public int mipmapLevels;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void setDefaultMipmap(CallbackInfo ci) {
        mipmapLevels = LabsConfig.advanced.defaultMipmap;
    }
}
