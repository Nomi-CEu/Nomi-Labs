package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;

public class LabsTOPManager {

    public static int FLUID_NAME_ELEMENT;
    public static int CUSTOM_NAME_ELEMENT;

    public static void register() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;

        // AE2 TOP Integration
        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            TOP.registerBlockDisplayOverride(new AECustomNameOverride());

        // GT TOP Integration
        TOP.registerProvider(new SteamMachineInfoProvider());

        // Labs TOP Integration
        TOP.registerProvider(new TOPTooltipMessage());

        FLUID_NAME_ELEMENT = TOP.registerElementFactory(LabsFluidNameElement::new);
        CUSTOM_NAME_ELEMENT = TOP.registerElementFactory(CustomNameElement::new);
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
