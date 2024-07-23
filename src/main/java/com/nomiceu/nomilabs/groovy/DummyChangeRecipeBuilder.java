package com.nomiceu.nomilabs.groovy;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;

public class DummyChangeRecipeBuilder<R extends RecipeBuilder<R>> extends ChangeRecipeBuilder<R> {

    public DummyChangeRecipeBuilder(RecipeMap<R> map) {
        // No graceful way to handle maps with no recipes... just let iterator throw.
        super(map.getRecipeList().iterator().next(), map);
    }

    @Override
    public void buildAndRegister() {}

    @Override
    public void replaceAndRegister() {}
}
