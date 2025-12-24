package com.nomiceu.nomilabs.integration.jei.mixinhelper;

import java.util.List;

import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;

public interface AccessibleModRegistry {

    <T> List<IngredientInfoRecipe<T>> labs$registerDescriptionWithRecipesReturned(List<T> ingredients,
                                                                                  IIngredientType<T> ingredientType,
                                                                                  String... descriptionKeys);
}
