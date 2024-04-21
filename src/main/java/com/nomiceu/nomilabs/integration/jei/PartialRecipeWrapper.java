package com.nomiceu.nomilabs.integration.jei;

import com.cleanroommc.groovyscript.compat.mods.jei.ShapedRecipeWrapper;
import com.nomiceu.nomilabs.groovy.PartialRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;

/**
 * Straight from {@link ShapedRecipeWrapper}
 */
public class PartialRecipeWrapper extends ShapelessRecipeWrapper<PartialRecipe>
                                  implements IShapedCraftingRecipeWrapper {

    public PartialRecipeWrapper(IJeiHelpers jeiHelpers, PartialRecipe recipe) {
        super(jeiHelpers, recipe);
    }

    @Override
    public int getWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return recipe.getRecipeHeight();
    }
}
