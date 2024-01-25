package com.nomiceu.nomilabs.mixin.gregtech;

import gregtech.api.recipes.category.GTRecipeCategory;

public interface RecipeMapAccessor {

    void removeAllRecipesInCategory(GTRecipeCategory category);
}
