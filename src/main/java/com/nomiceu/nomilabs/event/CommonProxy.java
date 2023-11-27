package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.multiblock.registry.LabsMultiblocks;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.recipe.HandFramingRecipe;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {

    public static void preInit() {
        if (LabsConfig.customContent.enableCustomContent) {
            LabsCreativeTabs.preInit();
            LabsItems.preInit();
            LabsBlocks.preInit();
            LabsFluids.preInit();
        }
        if (LabsConfig.customContent.enableGTCustomContent) {
            LabsMetaBlocks.preInit();
            LabsRecipeMaps.preInit();
        }
    }

    public static void postInit() {
        if (LabsConfig.customContent.enableGTCustomContent)
            LabsMultiblocks.postInit();
        // GreenhouseTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        if (LabsConfig.customContent.enableCustomContent)
            LabsItems.register(registry);
        if (LabsConfig.customContent.enableGTCustomContent)
            LabsMetaBlocks.registerItems(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        if (LabsConfig.customContent.enableCustomContent)
            LabsBlocks.register(registry);
        if (LabsConfig.customContent.enableGTCustomContent)
            LabsMetaBlocks.registerBlocks(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        if (LabsConfig.customContent.enableGTCustomContent) {
            LabsMaterials.init();
            LabsMaterials.materialChanges();
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (LabsConfig.customContent.enableCustomContent) {
            event.getRegistry().register(new HandFramingRecipe(LabsNames.makeLabsName(
                    Objects.requireNonNull(LabsItems.HAND_FRAMING_TOOL.getRegistryName()).getPath() + "_recipe")));
        }
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LabsValues.LABS_MODID)) {
            ConfigManager.sync(LabsValues.LABS_MODID, Config.Type.INSTANCE);
        }
    }
}
