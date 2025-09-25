package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.ILaserContainer;
import gregtech.integration.theoneprobe.provider.ElectricContainerInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;

/**
 * Applies <a href="https://github.com/GregTechCEu/GregTech/pull/2597">GT #2597</a>.
 */
@Mixin(value = ElectricContainerInfoProvider.class, remap = false)
public class ElectricContainerInfoProviderMixin {

    @WrapMethod(method = "allowDisplaying(Lgregtech/api/capability/IEnergyContainer;)Z")
    private boolean checkIsLaser(IEnergyContainer capability, Operation<Boolean> original) {
        return original.call(capability) && !(capability instanceof ILaserContainer);
    }

    @Inject(method = "addProbeInfo(Lgregtech/api/capability/IEnergyContainer;Lmcjty/theoneprobe/api/IProbeInfo;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/tileentity/TileEntity;Lmcjty/theoneprobe/api/IProbeHitData;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void newRenderingLogic(IEnergyContainer capability, IProbeInfo probeInfo, EntityPlayer player,
                                   TileEntity tileEntity,
                                   IProbeHitData data, CallbackInfo ci) {
        ci.cancel();

        long maxStorage = capability.getEnergyCapacity();
        long stored = capability.getEnergyStored();
        if (maxStorage == 0) return; // do not add empty max storage progress bar
        probeInfo.progress(capability.getEnergyStored(), maxStorage, probeInfo.defaultProgressStyle()
                .numberFormat(player.isSneaking() || stored < 10000 ?
                        NumberFormat.COMMAS :
                        NumberFormat.COMPACT)
                .suffix(" / " + (player.isSneaking() || maxStorage < 10000 ?
                        ElementProgress.format(maxStorage, NumberFormat.COMMAS, " EU") :
                        ElementProgress.format(maxStorage, NumberFormat.COMPACT, "EU")))
                .filledColor(0xFFEEE600)
                .alternateFilledColor(0xFFEEE600)
                .borderColor(0xFF555555));
    }
}
