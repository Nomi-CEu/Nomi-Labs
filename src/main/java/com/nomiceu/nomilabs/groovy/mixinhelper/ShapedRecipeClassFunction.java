package com.nomiceu.nomilabs.groovy.mixinhelper;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;

import groovy.lang.Closure;

@FunctionalInterface
public interface ShapedRecipeClassFunction {

    ShapedCraftingRecipe createRecipe(ItemStack output, int width, int height, List<IIngredient> ingredients,
                                      boolean mirrored, Closure<ItemStack> recipeFunction, Closure<Void> recipeAction);
}
