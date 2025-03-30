package com.nomiceu.nomilabs.mixin.groovyscript;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipe;
import com.nomiceu.nomilabs.groovy.mixinhelper.StrictableRecipe;

/**
 * Allows for recipes to be 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
 * exactly as set, instead of expanding wildcards and removing duplicates.
 */
@Mixin(value = CraftingRecipe.class, remap = false)
public class CraftingRecipeMixin implements StrictableRecipe {

    @Unique
    private boolean labs$isStrict = false;

    @Override
    @Unique
    public boolean labs$getIsStrict() {
        return labs$isStrict;
    }

    @Override
    @Unique
    public void labs$setStrict() {
        labs$isStrict = true;
    }
}
