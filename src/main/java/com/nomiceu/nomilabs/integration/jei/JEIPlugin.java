package com.nomiceu.nomilabs.integration.jei;

import com.cleanroommc.groovyscript.compat.mods.jei.ShapedRecipeWrapper;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        var jeiHelpers = registry.getJeiHelpers();

        // JEI does not recognise Custom Recipe Classes on its own
        // Uses the shaped recipe wrapper from GroovyScript
        registry.handleRecipes(PartialRecipe.class, recipe -> new ShapedRecipeWrapper(jeiHelpers, recipe), VanillaRecipeCategoryUid.CRAFTING);
    }
}
