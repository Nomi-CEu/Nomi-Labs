package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleIntCircuitIngredient;

import gregtech.api.recipes.ingredients.IntCircuitIngredient;

/**
 * Allows Accessing of the Meta.
 */
@Mixin(value = IntCircuitIngredient.class, remap = false)
public class IntCircuitIngredientMixin implements AccessibleIntCircuitIngredient {

    @Shadow
    @Final
    private int matchingConfigurations;

    @Override
    @Unique
    public int getMeta() {
        return matchingConfigurations;
    }
}
