package com.nomiceu.nomilabs.mixin.gregtech;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.cleanroommc.groovyscript.api.IIngredient;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;

@Mixin(value = RecipeBuilder.class, remap = false)
public interface RecipeBuilderAccessor {

    @Invoker("ofGroovyIngredient")
    @NotNull
    static GTRecipeInput ofGroovyIngredient(IIngredient ingredient) {
        throw new NotImplementedException("RecipeBuilderAccessor failed to apply!");
    }
}
