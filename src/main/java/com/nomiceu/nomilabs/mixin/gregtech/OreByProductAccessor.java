package com.nomiceu.nomilabs.mixin.gregtech;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.integration.jei.basic.OreByProduct;

@Mixin(value = OreByProduct.class, remap = false)
public interface OreByProductAccessor {

    @Accessor("NUM_INPUTS")
    static int getNumInputs() {
        throw new NotImplementedException("OreByProductAccessor failed to apply!");
    }
}
