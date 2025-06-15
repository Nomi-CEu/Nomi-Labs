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
    public static int FLUID_STACK_ELEMENT;
    public static int CHANCED_ITEM_STACK_ELEMENT;
    public static int CHANCED_FLUID_STACK_ELEMENT;
    public static int CHANCED_FLUID_NAME_ELEMENT;
    public static int QUANTUM_LINK_CONNECTION_ELEMENT;

    public static void register() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;

        // AE2 TOP Integration
        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            TOP.registerBlockDisplayOverride(new AECustomNameOverride());

        // GT TOP Integration
        TOP.registerProvider(new SteamMachineInfoProvider());
        TOP.registerProvider(new RecipeOutputsProvider());

        // AE2 TOP Integration
        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            TOP.registerProvider(new LabsQuantumLinkChamberProvider());

        // Labs TOP Integration
        TOP.registerProvider(new TOPTooltipMessage());

        // General TOP Integration
        TOP.registerProvider(new LabsRFInfoProvider());

        FLUID_NAME_ELEMENT = TOP.registerElementFactory(LabsFluidNameElement::new);
        CUSTOM_NAME_ELEMENT = TOP.registerElementFactory(CustomNameElement::new);
        FLUID_STACK_ELEMENT = TOP.registerElementFactory(LabsFluidStackElement::new);
        CHANCED_ITEM_STACK_ELEMENT = TOP.registerElementFactory(LabsChancedItemStackElement::new);
        CHANCED_FLUID_STACK_ELEMENT = TOP.registerElementFactory(LabsChancedFluidStackElement::new);
        CHANCED_FLUID_NAME_ELEMENT = TOP.registerElementFactory(LabsChancedFluidNameElement::new);
        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            QUANTUM_LINK_CONNECTION_ELEMENT = TOP
                    .registerElementFactory(LabsQuantumLinkChamberProvider.ConnectionInfoElement::new);
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
