package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.compat.mods.jei.JeiPlugin;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.groovy.mixinhelper.LabsJEIApplied;
import com.nomiceu.nomilabs.integration.jei.LabsJEIPlugin;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.gui.textures.Textures;
import mezz.jei.startup.JeiStarter;
import mezz.jei.startup.ModRegistry;

/**
 * Fix GrS JEI Hiding Issues, by re-checking if applied, and if not, applying at end of registers,
 * instead of before ProgressManager.pop(). This can happen due to HEI's new 'Progress Bar' config.
 * <p>
 * Also fixes a typo in latest JEI, where the default load is recipe only.
 * <p>
 * Also provides a proper loading time for Labs' Recipe Catalyst Override.
 */
@Mixin(value = JeiStarter.class, remap = false)
public class JeiStarterMixin {

    @Redirect(method = "start",
              at = @At(value = "INVOKE",
                       target = "Lmezz/jei/startup/JeiStarter;load(Ljava/util/List;Lmezz/jei/gui/textures/Textures;Z)V"),
              require = 1)
    private void loadWithRecipesFalseDefault(JeiStarter instance, List<IModPlugin> plugins, Textures textures,
                                             boolean recipeOnly) {
        instance.load(plugins, textures, false);
    }

    @Inject(method = "registerPlugins", at = @At("TAIL"))
    private static void handleAfterRegister(List<IModPlugin> plugins, ModRegistry modRegistry, CallbackInfo ci) {
        LabsJEIPlugin.afterModRegisters(modRegistry);

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
