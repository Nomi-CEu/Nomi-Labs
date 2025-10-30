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
    public static int ITEM_OUTPUT_ELEMENT;
    public static int FLUID_OUTPUT_ELEMENT;
    public static int CHANCED_ITEM_OUTPUT_ELEMENT;
    public static int CHANCED_FLUID_OUTPUT_ELEMENT;
    public static int QUANTUM_LINK_CONNECTION_ELEMENT;
    public static int TANK_GAUGE_ELEMENT;
    public static int ITEM_NAME_ELEMENT;
    public static int TEXT_WITH_ARGS_ELEMENT;
    public static int PROPERTIES_ELEMENT;
    public static int LOCKED_ITEM_ELEMENT;

    public static void register() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;

        // AE2 TOP Integration
        if (Loader.isModLoaded(LabsValues.AE2_MODID)) {
            TOP.registerBlockDisplayOverride(new AECustomNameOverride());
            TOP.registerProvider(new LabsQuantumLinkChamberProvider());
            TOP.registerProvider(new GeneralAEPowerStateInfoProvider());
        }

        // Storage Drawers TOP Integration
        if (Loader.isModLoaded(LabsValues.STORAGE_DRAWERS_MODID)) {
            TOP.registerProvider(new LabsDrawersProvider());
        }

        // GT TOP Integration
        TOP.registerProvider(new SteamMachineInfoProvider());
        TOP.registerProvider(new RecipeOutputsProvider());
        TOP.registerProvider(new CreativeBlockInfoProvider());
        TOP.registerProvider(new LabsTankInfoProvider());

        // Labs TOP Integration
        TOP.registerProvider(new TOPTooltipMessage());

        // General TOP Integration
        TOP.registerProvider(new LabsRFInfoProvider());

        // Property Providers
        TOP.registerProvider(new LabsPropertiesProvider());

        FLUID_NAME_ELEMENT = TOP.registerElementFactory(LabsFluidNameElement::new);
        CUSTOM_NAME_ELEMENT = TOP.registerElementFactory(CustomNameElement::new);
        ITEM_OUTPUT_ELEMENT = TOP.registerElementFactory(LabsItemOutputElement::new);
        FLUID_OUTPUT_ELEMENT = TOP.registerElementFactory(LabsFluidOutputElement::new);
        CHANCED_ITEM_OUTPUT_ELEMENT = TOP.registerElementFactory(LabsChancedItemOutputElement::new);
        CHANCED_FLUID_OUTPUT_ELEMENT = TOP.registerElementFactory(LabsChancedFluidOutputElement::new);
        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            QUANTUM_LINK_CONNECTION_ELEMENT = TOP
                    .registerElementFactory(LabsQuantumLinkChamberProvider.ConnectionInfoElement::new);
        TANK_GAUGE_ELEMENT = TOP.registerElementFactory(LabsTankGaugeElement::new);
        ITEM_NAME_ELEMENT = TOP.registerElementFactory(LabsItemNameElement::new);
        TEXT_WITH_ARGS_ELEMENT = TOP.registerElementFactory(LabsLangKeyWithArgsElement::new);
        PROPERTIES_ELEMENT = TOP.registerElementFactory(LabsPropertyElement::new);
        LOCKED_ITEM_ELEMENT = TOP.registerElementFactory(LabsCustomCountItemStackElement::new);
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
