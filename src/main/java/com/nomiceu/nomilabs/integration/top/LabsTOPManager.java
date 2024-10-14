package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.nomiceu.nomilabs.LabsValues;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;

public class LabsTOPManager {

    public static int FLUID_NAME_ELEMENT;

    public static void register() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;
        TOP.registerProvider(new TOPTooltipMessage());
        TOP.registerProvider(new SteamMachineInfoProvider());
        FLUID_NAME_ELEMENT = TOP.registerElementFactory(LabsFluidNameElement::new);
    }

    public static class TOPTooltipMessage implements IProbeInfoProvider {

        @Override
        public String getID() {
            return LabsValues.LABS_MODID + ":top_tooltips";
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world,
                                 IBlockState blockState, IProbeHitData probeHitData) {
            Block block = blockState.getBlock();
            if (block instanceof TOPInfoProvider infoProvider) {
                var msg = infoProvider.getTOPMessage(blockState);
                if (msg == null) return;
                for (var text : msg) {
                    probeInfo.text(text);
                }
            }
        }
    }
}
