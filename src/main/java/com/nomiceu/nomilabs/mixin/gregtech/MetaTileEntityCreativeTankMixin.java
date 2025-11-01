package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.common.metatileentities.storage.MetaTileEntityCreativeTank;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Makes the default mbPerTick be Int.MAX_VALUE, provides no fluid info to TOP to allow for custom handler.
 * Adds the configured fluid to tooltip.
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

    @Inject(method = "addInformation", at = @At("TAIL"))
    private void addFluidInfo(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced,
                              CallbackInfo ci) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("Fluid")) return;

        FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("Fluid"));
        if (fluid == null) return;

        tooltip.add(LabsTranslate.translate("tooltip.nomilabs.creative_tank.configured", fluid.getLocalizedName()));
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
