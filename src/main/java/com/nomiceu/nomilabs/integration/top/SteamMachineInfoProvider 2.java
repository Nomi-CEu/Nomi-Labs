package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleSteamMetaTileEntity;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import mcjty.theoneprobe.api.*;

public class SteamMachineInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":steam_machine_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState state, IProbeHitData data) {
        if (!state.getBlock().hasTileEntity(state)) return;

        TileEntity te = world.getTileEntity(data.getPos());
        if (!(te instanceof IGregTechTileEntity igtte)) return;

        MetaTileEntity mte = igtte.getMetaTileEntity();
        if (!(mte instanceof AccessibleSteamMetaTileEntity steamMte)) return;

        if (!steamMte.labs$ventingStuck()) return;

        probeInfo.text(TextStyleClass.INFO.toString() + TextFormatting.RED +
                LabsTranslate.topTranslate("nomilabs.top.steam_venting_stuck.1"));
        probeInfo.text(TextStyleClass.WARNING +
                LabsTranslate.topTranslate("nomilabs.top.steam_venting_stuck.2"));
    }
}
