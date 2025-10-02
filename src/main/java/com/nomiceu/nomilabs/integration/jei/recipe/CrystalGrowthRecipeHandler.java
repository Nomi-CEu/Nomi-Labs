package com.nomiceu.nomilabs.integration.jei.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class CrystalGrowthRecipeHandler implements
                                        IRecipeWrapperFactory<CrystalGrowthRecipeHandler.CrystalGrowthRecipe> {

    @Override
    @NotNull
    public IRecipeWrapper getRecipeWrapper(@NotNull CrystalGrowthRecipe recipe) {
        return recipe;
    }

    public static CrystalGrowthRecipe createRecipe(List<ItemStack> in, ItemStack out) {
        return new CrystalGrowthRecipe(in, out);
    }

    public static class CrystalGrowthRecipe implements IRecipeWrapper {

        private final List<List<ItemStack>> in;
        private final ItemStack out;

        public CrystalGrowthRecipe(List<ItemStack> ingredients, ItemStack out) {
            this.in = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                if (ingredients.size() <= i) {
                    in.add(Collections.emptyList());
                    continue;
                }
                in.add(Collections.singletonList(ingredients.get(i)));
            }
            this.out = out;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(VanillaTypes.ITEM, in);
            ingredients.setOutput(VanillaTypes.ITEM, out);
        }
    }
}
