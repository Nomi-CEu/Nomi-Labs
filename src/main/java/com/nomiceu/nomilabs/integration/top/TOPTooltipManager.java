package com.nomiceu.nomilabs.integration.top;

import com.nomiceu.nomilabs.LabsValues;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TOPTooltipManager {
    public static void registerProviders() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;
        TOP.registerProvider(new TOPTooltipMessage());
    }

    public static class TOPTooltipMessage implements IProbeInfoProvider {
        @Override
        public String getID() {
            return LabsValues.LABS_MODID + "top_tooltips";
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData probeHitData) {
            Block block = blockState.getBlock();
            if (block instanceof TOPInfoProvider infoProvider) {
                var msg = infoProvider.getTOPMessage(blockState);
                for (var text : msg) {
                    probeInfo.text(text);
                }
            }
        }
    }
}
