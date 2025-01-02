package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import net.minecraft.util.NonNullList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.compat.mods.jei.JeiPlugin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.groovy.mixinhelper.LabsJEIApplied;
import com.nomiceu.nomilabs.integration.jei.SavedJEIValues;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.ingredients.IngredientBlacklistInternal;
import mezz.jei.ingredients.IngredientFilter;
import mezz.jei.ingredients.IngredientListElementFactory;
import mezz.jei.startup.IModIdHelper;
import mezz.jei.startup.JeiStarter;
import mezz.jei.startup.ModRegistry;

/**
 * Fix GrS JEI Hiding Issues, by re-checking if applied, and if not, applying at end of registers,
 * instead of before ProgressManager.pop(). This can happen due to HEI's new 'Progress Bar' config.
 * <p>
 * Also allows for 'fast' reloading of JEI, reusing the existing Ingredient Filter, Search Trees and Bookmarks Data.
 */
@Mixin(value = JeiStarter.class, remap = false)
public class JeiStarterMixin {

    @SuppressWarnings("rawtypes")
    @Redirect(method = "start",
              at = @At(value = "INVOKE",
                       target = "Lmezz/jei/ingredients/IngredientListElementFactory;createBaseList(Lmezz/jei/api/ingredients/IIngredientRegistry;Lmezz/jei/startup/IModIdHelper;)Lnet/minecraft/util/NonNullList;"),
              require = 1)
    private NonNullList<IIngredientListElement> stopRegenerationOfList(IIngredientRegistry ingredientType,
                                                                       IModIdHelper ingredientRegistry) {
        if (SavedJEIValues.savedFilter == null)
            return IngredientListElementFactory.createBaseList(ingredientType, ingredientRegistry);
        return null;
    }

    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "start",
                   at = @At(value = "NEW",
                            target = "(Lmezz/jei/ingredients/IngredientBlacklistInternal;Lnet/minecraft/util/NonNullList;)Lmezz/jei/ingredients/IngredientFilter;"),
                   require = 1)
    private IngredientFilter reuseIngredientBlacklist(IngredientBlacklistInternal blacklist,
                                                      NonNullList<IIngredientListElement> ingredients,
                                                      Operation<IngredientFilter> original) {
        if (SavedJEIValues.savedFilter == null) return original.call(blacklist, ingredients);

        NomiLabs.LOGGER.info("[Fast JEI Reload] Using Saved JEI Ingredient Filter...");
        return SavedJEIValues.savedFilter;
    }

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
