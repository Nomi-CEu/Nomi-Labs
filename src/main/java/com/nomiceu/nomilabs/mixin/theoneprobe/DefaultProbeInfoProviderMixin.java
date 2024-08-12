package com.nomiceu.nomilabs.mixin.theoneprobe;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mcjty.theoneprobe.config.Config;

/**
 * Fixes Localization of Fluid Names.
 */
@Mixin(value = DefaultProbeInfoProvider.class, remap = false)
public class DefaultProbeInfoProviderMixin {

    @Inject(method = "addFluidInfo", at = @At("HEAD"), cancellable = true)
    private void showTranslatedFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack,
                                         int maxContents, CallbackInfo ci) {
        int contents = fluidStack == null ? 0 : fluidStack.amount;
        if (fluidStack != null) {
            probeInfo.element(new LabsFluidNameElement(fluidStack));
        }
        if (config.getTankMode() == 1) {
            probeInfo.progress(contents, maxContents,
                    probeInfo.defaultProgressStyle()
                            .suffix("mB")
                            .filledColor(Config.tankbarFilledColor)
                            .alternateFilledColor(Config.tankbarAlternateFilledColor)
                            .borderColor(Config.tankbarBorderColor)
                            .numberFormat(Config.tankFormat));
        } else {
            probeInfo.text(TextStyleClass.PROGRESS + ElementProgress.format(contents, Config.tankFormat, "mB"));
        }

        ci.cancel();
    }
}
