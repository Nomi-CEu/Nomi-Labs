package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.groovy.ReplaceRecipe;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.ValidationResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

/**
 * Precaution to make sure only Recycling Recipes are added during recycling recipe reloading.<br>
 * This is because Arc Smelting sometimes generates non-recycling recipes.
 */
@Mixin(value = RecipeMap.class, remap = false)
public class RecipeMapMixin {
    @Inject(method = "addRecipe", at = @At("HEAD"), cancellable = true)
    public void addRecipeInRecycling(@NotNull ValidationResult<Recipe> validationResult, CallbackInfoReturnable<Boolean> cir) {
        if (!ReplaceRecipe.isReloadingRecycling()) return;
        // If not in the map returns null, which will never equal the recipe category of the recipe, which is never null
        if (!Objects.equals(ReplaceRecipe.recyclingMaps.get((RecipeMap<?>) (Object) this),
                validationResult.getResult().getRecipeCategory()))
            cir.setReturnValue(false);
    }
}
