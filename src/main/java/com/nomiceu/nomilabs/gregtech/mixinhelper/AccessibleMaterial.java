package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import gregtech.api.recipes.Recipe;
import gregtech.api.unification.stack.MaterialStack;

public interface AccessibleMaterial {

    void labs$setComponents(ImmutableList<MaterialStack> components, boolean changeFormula);

    void labs$setComponents(ImmutableList<MaterialStack> components);

    void labs$recalculateDecompositionType();

    void labs$setOriginalRecipes(CompositionRecipeType type, List<Recipe> originals);

    Map<CompositionRecipeType, List<Recipe>> labs$getOriginalRecipes();

    ImmutableList<MaterialStack> labs$getOriginalComponents();
}
