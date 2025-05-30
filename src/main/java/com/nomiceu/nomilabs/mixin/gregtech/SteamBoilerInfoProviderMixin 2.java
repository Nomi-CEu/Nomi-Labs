package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleSteamBoiler;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.metatileentities.steam.boiler.SteamBoiler;
import gregtech.integration.theoneprobe.provider.SteamBoilerInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

/**
 * Improves Steam Boiler TOP Display.
 */
@Mixin(value = SteamBoilerInfoProvider.class, remap = false)
public class SteamBoilerInfoProviderMixin {

    @Inject(method = "addProbeInfo", at = @At("HEAD"), cancellable = true)
    private void replaceProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                                  IBlockState state, IProbeHitData data, CallbackInfo ci) {
        ci.cancel();

        if (!state.getBlock().hasTileEntity(state)) return;

        TileEntity te = world.getTileEntity(data.getPos());
        if (!(te instanceof IGregTechTileEntity igtte)) return;

        MetaTileEntity mte = igtte.getMetaTileEntity();
        if (!(mte instanceof SteamBoiler boiler)) return;

        int steamOutput = boiler.getTotalSteamOutput();

        // If we are not producing steam, and we have no fuel
        if (steamOutput <= 0 && !boiler.isBurning()) return;

        // Creating steam
        if (steamOutput > 0 && boiler.hasWater()) {
            probeInfo.text(TextStyleClass.INFO + "{*gregtech.top.energy_production*} " +
                    TextFormatting.AQUA + TextFormattingUtil.formatNumbers(steamOutput / 10) +
                    TextStyleClass.INFO + " L/t" + " {*" +
                    Materials.Steam.getUnlocalizedName() + "*}");
        }

        // Cooling Down
        if (!boiler.isBurning()) {
            probeInfo.text(TextStyleClass.INFO.toString() + TextFormatting.RED +
                    "{*nomilabs.top.steam_cooling_down*}");
        }

        // Initial Heat-Up
        if (steamOutput <= 0 && ((AccessibleSteamBoiler) boiler).labs$getCurrentTemperature() > 0) {
            // Current Temperature = the % until the boiler reaches 100
            probeInfo.text(TextStyleClass.INFO.toString() + TextFormatting.RED +
                    "{*nomilabs.top.steam_heating_up*} " +
                    TextFormattingUtil
                            .formatNumbers(((AccessibleSteamBoiler) boiler).labs$getCurrentTemperature()) +
                    "%");
        }

        // No Water
        if (!boiler.hasWater()) {
            probeInfo.text(TextStyleClass.WARNING + "{*gregtech.top.steam_no_water*}");
        }
    }
}
