package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.registries.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = NomiLabs.MODID)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerModels();
    }
}
