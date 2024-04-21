package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import gregtech.api.recipes.Recipe;
import gregtech.api.unification.stack.MaterialStack;

public interface AccessibleMaterial {

    void setComponents(ImmutableList<MaterialStack> components, boolean changeFormula);

    void setComponents(ImmutableList<MaterialStack> components);

    void recalculateDecompositionType();

    void setOriginalRecipes(CompositionRecipeType type, List<Recipe> originals);

    Map<CompositionRecipeType, List<Recipe>> getOriginalRecipes();

    ImmutableList<MaterialStack> getOriginalComponents();
}
