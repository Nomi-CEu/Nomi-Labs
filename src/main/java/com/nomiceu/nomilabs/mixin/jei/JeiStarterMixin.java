package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.compat.mods.jei.JeiPlugin;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.groovy.mixinhelper.LabsJEIApplied;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.startup.JeiStarter;
import mezz.jei.startup.ModRegistry;

/**
 * Fix GrS JEI Hiding Issues, by re-checking if applied, and if not, applying at end of registers,
 * instead of before ProgressManager.pop(). This can happen due to HEI's new 'Progress Bar' config.
 */
@Mixin(value = JeiStarter.class, remap = false)
public class JeiStarterMixin {

    @Inject(method = "registerPlugins", at = @At("TAIL"))
    private static void handleAfterRegister(List<IModPlugin> plugins, ModRegistry modRegistry, CallbackInfo ci) {
        if (LabsJEIApplied.afterRegisterApplied) {
            NomiLabs.LOGGER.info("[GrS + JEI] After Register Applied Correctly.");
            return;
        }

        NomiLabs.LOGGER.error("[GrS + JEI] After Register Did Not Apply Correctly! Applying...");
        JeiPlugin.afterRegister();
    }

    @Inject(method = "sendRuntime", at = @At("TAIL"))
    private static void handleAfterRuntime(List<IModPlugin> plugins, IJeiRuntime jeiRuntime, CallbackInfo ci) {
        if (LabsJEIApplied.afterRuntimeApplied) {
            NomiLabs.LOGGER.info("[GrS + JEI] After Runtime Applied Correctly.");
            return;
        }

        NomiLabs.LOGGER.error("[GrS + JEI] After Runtime Did Not Apply Correctly! Applying...");
        JeiPlugin.afterRuntimeAvailable();
    }
}
