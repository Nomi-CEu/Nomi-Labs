package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleCreativeTank;
import com.nomiceu.nomilabs.integration.top.CustomFluidTankProvider;

import gregtech.common.metatileentities.storage.MetaTileEntityCreativeTank;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Makes the default mbPerTick be Int.MAX_VALUE, provides no fluid info to TOP to allow for custom handler.
 * TODO: Maybe change in GT 2.9 when Creative Tanks have proper internal tank?
 */
@Mixin(value = MetaTileEntityCreativeTank.class, remap = false)
public class MetaTileEntityCreativeTankMixin extends MetaTileEntityQuantumTank
                                             implements CustomFluidTankProvider, AccessibleCreativeTank {

    @Shadow
    private int mBPerCycle;

    @Shadow
    private boolean active;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityCreativeTankMixin(ResourceLocation metaTileEntityId, int tier, int maxFluidCapacity) {
        super(metaTileEntityId, tier, maxFluidCapacity);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setDefaultMbPerTick(ResourceLocation metaTileEntityId, CallbackInfo ci) {
        mBPerCycle = Integer.MAX_VALUE;
    }

    @Override
    public @Nullable IFluidTankProperties[] labs$getOverrideTanks() {
        return new IFluidTankProperties[0];
    }

    @Override
    public boolean labs$isActive() {
        return active && fluidTank.getFluid() != null;
    }

    @Override
    public FluidStack labs$getFluid() {
        return fluidTank.getFluid();
    }
}
