package com.nomiceu.nomilabs.integration.jei;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class CrystalGrowthRecipeHandler implements
                                        IRecipeWrapperFactory<CrystalGrowthRecipeHandler.CrystalGrowthRecipe> {

    @Override
    @NotNull
    public IRecipeWrapper getRecipeWrapper(@NotNull CrystalGrowthRecipe recipe) {
        return new CrystalGrowthRecipeWrapper(recipe.getIn(), recipe.getOut());
    }

    public static CrystalGrowthRecipe createRecipe(List<ItemStack> in, ItemStack out) {
        return new CrystalGrowthRecipe(in, out);
    }

    public static class CrystalGrowthRecipe {

        private final List<ItemStack> in;
        private final ItemStack out;

        public CrystalGrowthRecipe(List<ItemStack> in, ItemStack out) {
            this.in = in;
            this.out = out;
        }

        public List<ItemStack> getIn() {
            return in;
        }

        public ItemStack getOut() {
            return out;
        }
    }
}
