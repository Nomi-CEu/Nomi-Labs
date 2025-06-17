package com.nomiceu.nomilabs.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CrystalGrowthRecipeWrapper implements IRecipeWrapper {

    private final List<List<ItemStack>> in;
    private final ItemStack out;

    public CrystalGrowthRecipeWrapper(List<ItemStack> ingredients, ItemStack out) {
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
