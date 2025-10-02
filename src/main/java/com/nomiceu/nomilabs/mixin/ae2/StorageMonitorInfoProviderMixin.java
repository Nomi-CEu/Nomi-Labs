package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;
import com.nomiceu.nomilabs.integration.top.LabsItemNameElement;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.integration.modules.theoneprobe.TheOneProbeText;
import appeng.integration.modules.theoneprobe.part.StorageMonitorInfoProvider;
import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Properly localizes a TOP string.
 */
@Mixin(value = StorageMonitorInfoProvider.class, remap = false)
public class StorageMonitorInfoProviderMixin {

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;",
                       ordinal = 0))
    private IProbeInfo properlyAddTextItem(IProbeInfo instance, String s, @Local IAEItemStack ais) {
        // Its a little cursed passing a non-lang key for a lang key argument, but it should be fine
        // TOP should also localise the key contained within the string (through getLocal()) by itself.
        return instance.element(
                new LabsItemNameElement(ais.asItemStackRepresentation(), TheOneProbeText.SHOWING.getLocal() + ": %s"));
    }

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;",
                       ordinal = 1))
    private IProbeInfo properlyAddTextFluid(IProbeInfo instance, String s, @Local IAEFluidStack ais) {
        // Its a little cursed passing a non-lang key for a lang key argument, but it should be fine
        // TOP should also localise the key contained within the string (through getLocal()) by itself.
        return instance
                .element(new LabsFluidNameElement(ais.getFluidStack(), TheOneProbeText.SHOWING.getLocal() + ": %s"));
    }
}
