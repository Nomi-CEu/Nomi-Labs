package com.nomiceu.nomilabs.groovy.mixinhelper;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

@FunctionalInterface
public interface ShapedRecipeClassFunctionSimplified {
    ShapedCraftingRecipe createRecipe(ItemStack output, int width, int height, List<IIngredient> ingredients);
}
