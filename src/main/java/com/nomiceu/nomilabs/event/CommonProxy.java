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
import gregtech.api.GTValues;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

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
        //if (LabsConfig.customContent.enableGTCustomContent) {
        if (LabsConfig.customContent.gtCustomContent.betaContent) {
            LabsMetaBlocks.preInit();
            LabsRecipeMaps.preInit();
        }
    }

    public static void postInit() {
       //if (LabsConfig.customContent.enableGTCustomContent)
        if (LabsConfig.customContent.gtCustomContent.betaContent)
            LabsMultiblocks.postInit();
        // GreenhouseTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        if (LabsConfig.customContent.enableCustomContent)
            LabsItems.register(registry);
        //if (LabsConfig.customContent.enableGTCustomContent)
        if (LabsConfig.customContent.gtCustomContent.betaContent)
            LabsMetaBlocks.registerItems(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        if (LabsConfig.customContent.enableCustomContent)
            LabsBlocks.register(registry);
        //if (LabsConfig.customContent.enableGTCustomContent)
        if (LabsConfig.customContent.gtCustomContent.betaContent)
            LabsMetaBlocks.registerBlocks(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        //if (LabsConfig.customContent.enableGTCustomContent) {
        if (LabsConfig.customContent.gtCustomContent.enableMaterials) {
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

    @SubscribeEvent
    public static void missingMappings(MissingMappings<Item> event) {
        for (MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            // Remap old DevTech perfect gem to new perfect gem.
            // DevTech did this badly, and created a MetaPrefixItem with GT's material registry but their
            // Mod ID, so we need to map the DevTech metaitem to the GT metaitem in missing mappings.
            if (entry.key.toString().equals("devtech:meta_gem_perfect")) {
                ResourceLocation newMapping = new ResourceLocation(GTValues.MODID, "meta_gem_perfect");
                entry.remap(ForgeRegistries.ITEMS.getValue(newMapping));
            }
        }
    }
}
