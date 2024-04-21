package com.nomiceu.nomilabs.groovy.mixinhelper;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;

@FunctionalInterface
public interface ShapedRecipeClassFunctionSimplified {

    ShapedCraftingRecipe createRecipe(ItemStack output, int width, int height, List<IIngredient> ingredients);
}
