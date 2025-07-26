package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.Nullable;

/**
 * Used to allow a TileEntity or MetaTileEntity to provide custom fluid tank info to TOP.
 */
public interface CustomFluidTankProvider {

    /**
     * The fluid tank properties TOP should render.
     * The FluidStack inside the TankProperties is not modified.
     * 
     * @return Null for default; empty list for none, else a list of fluid tank properties.
     */
    @Nullable
    IFluidTankProperties[] labs$getOverrideTanks();
}
