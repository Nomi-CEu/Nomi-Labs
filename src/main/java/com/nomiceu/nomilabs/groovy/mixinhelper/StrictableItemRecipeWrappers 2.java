package com.nomiceu.nomilabs.groovy.mixinhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.compat.mods.jei.ShapedRecipeWrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import mezz.jei.recipes.BrokenCraftingRecipeException;
import mezz.jei.util.ErrorUtil;

/**
 * Recipe wrappers that can return the matching stacks exactly for JEI,
 * ignoring duplicates according to JEI and wildcard itemstacks.
 */
public class StrictableItemRecipeWrappers {

    public static void getIngredientsImpl(@NotNull IIngredients ingredients, IRecipe recipe, IJeiHelpers jeiHelpers) {
        ItemStack recipeOutput = recipe.getRecipeOutput();
        List<List<ItemStack>> inputLists;

        try {
            if (recipe instanceof StrictableRecipe strict && strict.labs$getIsStrict()) {
                inputLists = new ArrayList<>();
                for (var input : recipe.getIngredients()) {
                    inputLists.add(new ArrayList<>(Arrays.asList(input.getMatchingStacks())));
                }
            } else {
                IStackHelper stackHelper = jeiHelpers.getStackHelper();
                inputLists = stackHelper.expandRecipeItemStackInputs(recipe.getIngredients());
            }
            ingredients.setInputLists(VanillaTypes.ITEM, inputLists);
            ingredients.setOutput(VanillaTypes.ITEM, recipeOutput);
        } catch (RuntimeException e) {
            String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(recipe, recipe.getIngredients(), recipeOutput);
            throw new BrokenCraftingRecipeException(info, e);
        }
    }

    public static class Shapeless extends ShapelessRecipeWrapper<IRecipe> {

        protected final IJeiHelpers jeiHelpers;

        public Shapeless(IJeiHelpers jeiHelpers, IRecipe recipe) {
            super(jeiHelpers, recipe);
            this.jeiHelpers = jeiHelpers;
        }

        @Override
        public void getIngredients(@NotNull IIngredients ingredients) {
            StrictableItemRecipeWrappers.getIngredientsImpl(ingredients, recipe, jeiHelpers);
        }
    }

    public static class Shaped extends ShapedRecipeWrapper {

        protected final IJeiHelpers jeiHelpers;

        public Shaped(IJeiHelpers jeiHelpers, IShapedRecipe recipe) {
            super(jeiHelpers, recipe);
            this.jeiHelpers = jeiHelpers;
        }

        @Override
        public void getIngredients(@NotNull IIngredients ingredients) {
            StrictableItemRecipeWrappers.getIngredientsImpl(ingredients, recipe, jeiHelpers);
        }
    }
}
