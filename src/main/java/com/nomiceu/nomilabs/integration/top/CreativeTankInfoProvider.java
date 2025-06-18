package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleCreativeTank;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

public class CreativeTankInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":creative_tank_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        if (!state.getBlock().hasTileEntity(state)) return;

        TileEntity te = world.getTileEntity(data.getPos());
        if (!(te instanceof IGregTechTileEntity gte)) return;

        MetaTileEntity mte = gte.getMetaTileEntity();
        if (!(mte instanceof AccessibleCreativeTank creative)) return;

        if (!creative.labs$isActive()) return;

        info.element(new LabsFluidNameElement(creative.labs$getFluid(), "nomilabs.top.creative_tank"));
    }
}
