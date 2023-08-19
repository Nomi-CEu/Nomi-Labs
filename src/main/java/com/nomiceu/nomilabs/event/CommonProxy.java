package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.material.LabsMaterials;
import com.nomiceu.nomilabs.recipe.GreenhouseTestRecipes;
import com.nomiceu.nomilabs.registry.*;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {

    public static void preInit() {
        LabsCreativeTabs.preInit();
        LabsItems.preInit();
        LabsBlocks.preInit();
        LabsMetaBlocks.preInit();
        LabsRecipeMaps.preInit();
    }

    public static void postInit() {
        LabsMultiblocks.postInit();
        GreenhouseTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        LabsItems.register(registry);
        LabsMetaBlocks.registerItems(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        LabsBlocks.register(registry);
        LabsMetaBlocks.registerBlocks(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        LabsMaterials.init();
        LabsMaterials.materialChanges();
    }

}
