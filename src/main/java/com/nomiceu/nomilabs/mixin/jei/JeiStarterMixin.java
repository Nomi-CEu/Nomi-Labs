package com.nomiceu.nomilabs.mixin.jei;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.jei.LabsJEIPlugin;

import mezz.jei.startup.JeiStarter;

/**
 * Allow custom handling after all mods have handled runtime.
 */
@Mixin(value = JeiStarter.class, remap = false)
public class JeiStarterMixin {

    @Inject(method = "sendRuntime", at = @At("TAIL"))
    private static void afterRuntimeAvailable(CallbackInfo ci) {
        LabsJEIPlugin.afterRuntimeAvailable();
    }
}
