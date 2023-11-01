package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.Crafting;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.RecyclingHandler;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.category.RecipeCategories;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.GTRecipeOreInput;
import gregtech.api.unification.OreDictUnifier;
import gregtech.loaders.recipe.RecyclingRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class ReplaceRecipe {
    private static final Crafting crafting;
    static {
        crafting = new Crafting();
    }
    static void reloadRecyclingRecipes() {
        removeRecipesInCategory(RecipeMaps.ARC_FURNACE_RECIPES, RecipeCategories.ARC_FURNACE_RECYCLING);
        removeRecipesInCategory(RecipeMaps.MACERATOR_RECIPES, RecipeCategories.MACERATOR_RECYCLING);
        removeRecipesInCategory(RecipeMaps.EXTRACTOR_RECIPES, RecipeCategories.EXTRACTOR_RECYCLING);
        RecyclingRecipes.init();
    }

    private static void removeRecipesInCategory(RecipeMap<?> recipeMap, GTRecipeCategory category) {
        if (!recipeMap.getRecipesByCategory().containsKey(category)) return;
        var recipes = new ArrayList<>(recipeMap.getRecipesByCategory().get(category));
        if (recipes.isEmpty()) return;
        for (var recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
    }

    static void replaceRecipeShaped(ResourceLocation name, ItemStack output, List<List<IIngredient>> inputs) {
        crafting.remove(name);
        crafting.addShaped(LabsNames.makeLabsName(name.getPath()), output, inputs);
        List<GTRecipeInput> gtInputs = new ArrayList<>();
        for (var inputList : inputs) {
            for (var input : inputList) {
                gtInputs.add(ofGroovyIngredient(input));
            }
        }
        OreDictUnifier.registerOre(output, RecyclingHandler.getRecyclingIngredients(gtInputs, output.getCount()));
    }

    @SuppressWarnings("ConstantValue")
    private static GTRecipeInput ofGroovyIngredient(IIngredient ingredient) {
        if (ingredient instanceof OreDictIngredient oreDictIngredient) {
            return GTRecipeOreInput.getOrCreate(oreDictIngredient.getOreDict(), ingredient.getAmount());
        }
        if ((Object) ingredient instanceof ItemStack stack) {
            return GTRecipeItemInput.getOrCreate(stack);
        }
        throw new IllegalArgumentException("Could not add groovy ingredient " + ingredient + " to recipe!");
    }
}
