package com.nomiceu.nomilabs.mixin.nuclearcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.nuclearcraft.GTCEuRecipes;

import nc.integration.gtce.GTCERecipeHelper;
import nc.recipe.ProcessorRecipe;

@Mixin(value = GTCERecipeHelper.class, remap = false)
public class GTCEuRecipeIntegration {

    /**
     * Override existing method to fix NuclearCraft Incompatibility with GTCEu.
     */
    @Inject(method = "addGTCERecipe", at = @At("HEAD"), cancellable = true)
    private static void addGTCEuRecipe(String recipeName, ProcessorRecipe recipe, CallbackInfo ci) {
        GTCEuRecipes.addGTCEuRecipe(recipeName, recipe);
        ci.cancel();
    }
}
