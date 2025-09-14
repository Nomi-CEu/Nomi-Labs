package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleCreativeTank;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.metatileentities.storage.MetaTileEntityCreativeEnergy;
import mcjty.theoneprobe.api.*;

public class CreativeBlockInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":creative_block_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        if (!state.getBlock().hasTileEntity(state)) return;

        TileEntity te = world.getTileEntity(data.getPos());
        if (!(te instanceof IGregTechTileEntity gte)) return;

        MetaTileEntity mte = gte.getMetaTileEntity();
        if (mte instanceof AccessibleCreativeTank creative) {
            if (creative.labs$isActive()) {
                info.element(new LabsFluidNameElement(creative.labs$getFluid(), "nomilabs.top.creative_tank"));
            } else {
                info.text(TextStyleClass.WARNING + LabsTranslate.topTranslate("gregtech.top.working_disabled"));
            }
            return;
        }

        if (mte instanceof MetaTileEntityCreativeEnergy creative) {
            // Working disabled handled by controllable cap
            if (!creative.isWorkingEnabled()) return;

            if (creative.inputsEnergy(EnumFacing.NORTH)) {
                // Sink
                info.text(LabsTranslate.topTranslate("nomilabs.top.creative_energy.sink"));
            } else {
                info.element(new LabsLangKeyWithArgsElement(
                        "nomilabs.top.creative_energy.prov",
                        GTValues.VNF[GTUtility.getTierByVoltage(creative.getOutputVoltage())],
                        Long.toString(creative.getOutputAmperage())));
            }
        }
    }
}
