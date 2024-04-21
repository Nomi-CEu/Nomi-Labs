package com.nomiceu.nomilabs.event;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.integration.betterquesting.LabsTierHelper;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.tooltip.TooltipAdder;

/*
 * Every texture is registered, in case something in that registry, not in that config, is enabled.
 * Nothing happens if each classes registries are empty.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LabsValues.LABS_MODID)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy {

    public static void earlyPreInit() {
        LabsTextures.preInit();
    }

    public static void latePreInit() {
        if (Loader.isModLoaded(LabsValues.BQU_MODID))
            LabsTierHelper.preInit();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        LabsItems.registerModels();
        LabsFluids.registerFluidBlockModels();
        LabsMetaBlocks.registerModels();
    }

    @SubscribeEvent
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        LabsFluids.registerFluidModels(event);
    }

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        TooltipAdder.addTooltipNormal(event.getToolTip(), event.getItemStack());
    }
}
