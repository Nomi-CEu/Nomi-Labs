package com.nomiceu.nomilabs.mixin.jei;

import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.jei.mixinhelper.AccessibleModRegistry;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import mezz.jei.startup.ModRegistry;

/**
 * Allows accessing recipe results after description registration.
 */
@Mixin(value = ModRegistry.class, remap = false)
public abstract class ModRegistryMixin implements AccessibleModRegistry {

    @Shadow
    @Final
    private IJeiHelpers jeiHelpers;

    @Shadow
    public abstract void addRecipes(Collection<?> recipes, String recipeCategoryUid);

    @Override
    public <T> List<IngredientInfoRecipe<T>> labs$registerDescriptionWithRecipesReturned(List<T> ingredients,
                                                                                         IIngredientType<T> ingredientType,
                                                                                         String... descriptionKeys) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        List<IngredientInfoRecipe<T>> recipes = IngredientInfoRecipe.create(guiHelper, ingredients, ingredientType,
                descriptionKeys);
        addRecipes(recipes, VanillaRecipeCategoryUid.INFORMATION);

        return recipes;
    }
}
