package com.nomiceu.nomilabs.mixin.advancedrocketry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import zmaster587.advancedRocketry.client.KeyBindings;
import zmaster587.libVulpes.common.CommonProxy;

/**
 * Fixes KeyBinding Keys THEMSELVES (And Categories) being localized. Why?
 */
@Mixin(value = KeyBindings.class, remap = false)
public class KeyBindingsMixin {

    @Redirect(method = "<clinit>",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/libVulpes/common/CommonProxy;getLocalizedString(Ljava/lang/String;)Ljava/lang/String;"),
              require = 14)
    private static String useKey(CommonProxy instance, String str) {
        return str;
    }
}
