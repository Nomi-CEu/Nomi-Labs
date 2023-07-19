package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.registries.LabsCreativeTabs;
import com.nomiceu.nomilabs.registries.ModBlocks;
import com.nomiceu.nomilabs.registries.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {

    public static void preInit() {
        LabsCreativeTabs.preInit();
        ModItems.preInit();
        ModBlocks.preInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ModItems.register(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        ModBlocks.register(registry);
    }
}
