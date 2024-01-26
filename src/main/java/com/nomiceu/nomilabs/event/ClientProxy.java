package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.command.LabsKeyBindings;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.tooltip.TooltipAdder;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Every texture is registered, in case something in that registry, not in that config, is enabled.
 * Nothing happens if each classes registries are empty.
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy {
    public static void onConstruction() {
        LabsKeyBindings.onConstruction();
    }

    public static void preInit() {
        LabsTextures.preInit();
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

    @SubscribeEvent
    public static void onInput(InputEvent.KeyInputEvent event) {
        LabsKeyBindings.onInput();
    }
}
