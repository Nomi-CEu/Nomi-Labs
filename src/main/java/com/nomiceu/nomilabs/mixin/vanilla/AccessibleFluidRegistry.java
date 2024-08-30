package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.BiMap;

@Mixin(value = FluidRegistry.class, remap = false)
public interface AccessibleFluidRegistry {

    @Accessor(value = "masterFluidReference")
    static BiMap<String, Fluid> getMasterFluidReference() {
        throw new NotImplementedException("AccessibleFluidRegistry Mixin Failed to Apply!");
    }
}
