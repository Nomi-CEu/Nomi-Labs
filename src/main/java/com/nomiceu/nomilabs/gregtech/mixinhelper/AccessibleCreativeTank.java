package com.nomiceu.nomilabs.gregtech.mixinhelper;

import net.minecraftforge.fluids.FluidStack;

public interface AccessibleCreativeTank {

    boolean labs$isActive();

    FluidStack labs$getFluid();
}
