package com.nomiceu.nomilabs.integration.ae2;

import net.minecraftforge.fluids.FluidStack;

import appeng.fluids.util.IAEFluidTank;

public interface NotifiableFluidInventory {

    void labs$onFluidInventoryChanged(IAEFluidTank inv, FluidStack added, FluidStack removed);
}
