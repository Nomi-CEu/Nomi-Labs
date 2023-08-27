package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LabsValues.LABS_MODID)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy {

    public static void preInit() {
        LabsTextures.preInit();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        LabsItems.registerModels();
        LabsMetaBlocks.registerModels();
    }
}
