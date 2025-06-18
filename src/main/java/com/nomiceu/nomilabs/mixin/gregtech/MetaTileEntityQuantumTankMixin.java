package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.top.CustomFluidTankProvider;

import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Provides locked fluid info to TOP.
 */
@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public abstract class MetaTileEntityQuantumTankMixin implements CustomFluidTankProvider {

    @Shadow
    protected abstract boolean isLocked();

    @Shadow
    protected FluidTank fluidTank;

    @Shadow
    private @Nullable FluidStack lockedFluid;

    @Shadow
    @Final
    private int maxFluidCapacity;

    @Override
    @Nullable
    public IFluidTankProperties[] labs$getOverrideTanks() {
        // Default logic: if not locked, no fluid tank, or there is fluid in tank
        if (!isLocked() || lockedFluid == null || fluidTank == null || fluidTank.getFluidAmount() != 0)
            return null;

        var fluidToUse = lockedFluid.copy();
        fluidToUse.amount = 0;

        return new FluidTankProperties[] { new FluidTankProperties(fluidToUse, maxFluidCapacity) };
    }
}
