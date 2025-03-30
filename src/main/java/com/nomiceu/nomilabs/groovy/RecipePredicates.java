package com.nomiceu.nomilabs.groovy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.cleanroommc.groovyscript.api.IIngredient;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.ingredients.GTRecipeInput;

@SuppressWarnings({ "SimplifyStreamApiCallChains", "unused" })
public class RecipePredicates {

    public static Predicate<Recipe> hasExactlyInput(IIngredient... items) {
        return ((recipe) -> hasExactlyInputs(recipe.getInputs(), items));
    }

    public static Predicate<Recipe> hasInput(IIngredient... items) {
        return ((recipe) -> hasInputs(recipe.getInputs(), items));
    }

    public static Predicate<Recipe> hasExactlyFluidInput(IIngredient... fluids) {
        return ((recipe) -> hasExactlyInputs(recipe.getFluidInputs(), fluids));
    }

    public static Predicate<Recipe> hasFluidInput(IIngredient... items) {
        return ((recipe) -> hasInputs(recipe.getFluidInputs(), items));
    }

    private static boolean hasInputs(List<GTRecipeInput> inputs, IIngredient[] items) {
        var gtItems = Arrays.stream(items).map(GroovyHelpers.GTRecipeHelpers::toGtInput).collect(Collectors.toList());

        return new HashSet<>(inputs).containsAll(gtItems);
    }

    private static boolean hasExactlyInputs(List<GTRecipeInput> inputs, IIngredient[] items) {
        if (inputs.size() != items.length)
            return false;
        var gtItems = Arrays.stream(items).map(GroovyHelpers.GTRecipeHelpers::toGtInput).collect(Collectors.toList());

        return new HashSet<>(inputs).containsAll(gtItems);
    }
}
