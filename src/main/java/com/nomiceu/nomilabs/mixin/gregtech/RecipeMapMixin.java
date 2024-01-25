package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.gregtech.RecipeMapAccessor;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(value = RecipeMap.class, remap = false)
public class RecipeMapMixin implements RecipeMapAccessor {
    @Shadow
    @Final
    private Map<GTRecipeCategory, List<Recipe>> recipeByCategory;

    @Override
    public void removeAllRecipesInCategory(GTRecipeCategory category) {
        recipeByCategory.remove(category);
    }
}
