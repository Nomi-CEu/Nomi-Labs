package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.registries.ModCreativeTabs;
import com.nomiceu.nomilabs.registries.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = NomiLabs.MODID)
@SuppressWarnings("unused")
public class CommonProxy {
    public static void preInit() {
        ModCreativeTabs.preInit();
        ModItems.preInit();
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ModItems.register(registry);
    }
}
