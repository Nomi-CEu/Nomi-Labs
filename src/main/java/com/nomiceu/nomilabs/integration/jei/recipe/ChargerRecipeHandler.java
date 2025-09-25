package com.nomiceu.nomilabs.integration.jei.recipe;

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class ChargerRecipeHandler implements IRecipeWrapperFactory<ChargerRecipeHandler.ChargerRecipe> {

    @Override
    @NotNull
    public IRecipeWrapper getRecipeWrapper(@NotNull ChargerRecipeHandler.ChargerRecipe recipe) {
        return recipe;
    }

    public static class ChargerRecipe implements IRecipeWrapper {

        private final List<List<ItemStack>> in;
        private final ItemStack out;

        public ChargerRecipe(ItemStack in, ItemStack out) {
            this.in = Collections.singletonList(Collections.singletonList(in));
            this.out = out;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(VanillaTypes.ITEM, in);
            ingredients.setOutput(VanillaTypes.ITEM, out);
        }
    }
}
