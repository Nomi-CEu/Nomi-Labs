package com.nomiceu.nomilabs.mixin.gregtech;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.LockableQuantumStorage;
import com.nomiceu.nomilabs.integration.top.CustomFluidTankProvider;

import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Provides locked fluid info to TOP. Properly syncs locked status to client; so we can display it in rendering.
 */
@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public abstract class MetaTileEntityQuantumTankMixin extends MetaTileEntity implements CustomFluidTankProvider,
                                                     LockableQuantumStorage {

    @Shadow
    protected abstract boolean isLocked();

    @Shadow
    protected FluidTank fluidTank;

    @Shadow
    private @Nullable FluidStack lockedFluid;

    @Shadow
    @Final
    private int maxFluidCapacity;

    @Shadow
    protected abstract void setLocked(boolean locked);

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumTankMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    /**
     * Sync locked stack in initial sync data.
     */
    @Inject(method = "writeInitialSyncData", at = @At("TAIL"))
    private void writeLockedInitialSync(PacketBuffer buf, CallbackInfo ci) {
        if (isLocked()) {
            if (lockedFluid == null)
                buf.writeBoolean(false);
            else {
                buf.writeBoolean(true);
                buf.writeCompoundTag(lockedFluid.writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Inject(method = "receiveInitialSyncData", at = @At("TAIL"))
    private void readLockedInitialSync(PacketBuffer buf, CallbackInfo ci) {
        try {
            if (isLocked() && buf.readBoolean())
                lockedFluid = FluidStack.loadFluidStackFromNBT(buf.readCompoundTag());
        } catch (IOException e) {
            NomiLabs.LOGGER.error(
                    "[QuantumTankMixin] Failed to load info from tile at {} from buffer in initial sync!",
                    getPos());
        }
    }

    @Inject(method = "receiveCustomData", at = @At("TAIL"))
    private void receiveLockedUpdate(int dataId, PacketBuffer buf, CallbackInfo ci) {
        if (dataId == GregtechDataCodes.UPDATE_LOCKED_STATE) {
            setLocked(buf.readBoolean());
            scheduleRenderUpdate();
        }
    }

    @Inject(method = "setLocked",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/common/metatileentities/storage/MetaTileEntityQuantumTank;markDirty()V"))
    private void sendCustomData(boolean locked, CallbackInfo ci) {
        writeCustomData(GregtechDataCodes.UPDATE_LOCKED_STATE, buf -> buf.writeBoolean(locked));
    }

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

    @Override
    public boolean labs$isLocked() {
        return isLocked() && lockedFluid != null;
    }
}
