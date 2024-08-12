package com.nomiceu.nomilabs.mixin.topaddons;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.drmanganese.topaddons.addons.AddonForge;

/**
 * Fixes Localization of Fluid Names.
 */
@Mixin(value = AddonForge.class, remap = false)
public class AddonForgeMixin {

    @Redirect(method = "addTankElement(Lmcjty/theoneprobe/api/IProbeInfo;Ljava/lang/Class;Lnet/minecraftforge/fluids/FluidTank;ILmcjty/theoneprobe/api/ProbeMode;Lnet/minecraft/entity/player/EntityPlayer;)Lmcjty/theoneprobe/api/IProbeInfo;",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraftforge/fluids/FluidStack;getLocalizedName()Ljava/lang/String;"))
    private static String useFluidName1(FluidStack instance) {
        return FluidRegistry.getFluidName(instance);
    }

    @Redirect(method = "addTankElement(Lmcjty/theoneprobe/api/IProbeInfo;Ljava/lang/String;Lnet/minecraftforge/fluids/FluidTankInfo;Lmcjty/theoneprobe/api/ProbeMode;Lnet/minecraft/entity/player/EntityPlayer;)Lmcjty/theoneprobe/api/IProbeInfo;",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraftforge/fluids/FluidStack;getLocalizedName()Ljava/lang/String;"))
    private static String useUnlocalizedName2(FluidStack instance) {
        return FluidRegistry.getFluidName(instance);
    }

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraftforge/fluids/Fluid;getLocalizedName(Lnet/minecraftforge/fluids/FluidStack;)Ljava/lang/String;"))
    private String useUnlocalizedName3(Fluid instance, FluidStack stack) {
        return FluidRegistry.getFluidName(instance);
    }
}
