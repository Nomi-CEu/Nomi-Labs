package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.groovy.AccessibleGTRecipeInput;

import gregtech.api.recipes.ingredients.GTRecipeInput;

/**
 * Allows us to Copy GTRecipeInputs.
 */
@Mixin(value = GTRecipeInput.class, remap = false)
public abstract class GTRecipeInputMixin implements AccessibleGTRecipeInput {

    @Shadow
    protected abstract GTRecipeInput copy();

    @Unique
    @Override
    public GTRecipeInput labs$accessibleCopy() {
        return copy();
    }
}
