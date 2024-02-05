package com.nomiceu.nomilabs.mixin.gregtech;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.loaders.recipe.handlers.DecompositionRecipeHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = DecompositionRecipeHandler.class, remap = false)
public interface AccessibleDecompositionRecipeHandler {
    @Invoker(value = "processDecomposition")
    static void processDecomposition(OrePrefix decomposePrefix, Material material) {
        throw new NotImplementedException("AccessibleDecompositionRecipeHandler failed to apply!");
    }
}
