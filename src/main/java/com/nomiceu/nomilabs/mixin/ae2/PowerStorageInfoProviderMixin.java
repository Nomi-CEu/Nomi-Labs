package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsLangKeyWithArgsElement;

import appeng.integration.modules.theoneprobe.tile.PowerStorageInfoProvider;
import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Properly localizes a TOP string; using a new lang key (original one has a typo)
 */
@Mixin(value = PowerStorageInfoProvider.class, remap = false)
public class PowerStorageInfoProviderMixin {

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;"),
              require = 1)
    private IProbeInfo properlyAddText(IProbeInfo instance, String s, @Local(ordinal = 0) String formatCurrentPower,
                                       @Local(ordinal = 1) String formatMaxPower) {
        return instance.element(new LabsLangKeyWithArgsElement("theoneprobe.appliedenergistics2.stored_energy_new",
                formatCurrentPower, formatMaxPower));
    }
}
