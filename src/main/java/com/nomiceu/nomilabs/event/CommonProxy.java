package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.multiblock.registry.LabsMultiblocks;
import com.nomiceu.nomilabs.gregtech.prefix.LabsMaterialFlags;
import com.nomiceu.nomilabs.gregtech.prefix.LabsOrePrefix;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.recipe.HandFramingRecipe;
import com.nomiceu.nomilabs.remap.LabsRemappers;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {
    public static void preInit() {
        LabsCreativeTabs.preInit();

        if (LabsConfig.content.customContent.enableItems)
            LabsItems.preInit();
        if (LabsConfig.content.customContent.enableBlocks)
            LabsBlocks.preInit();
        if (LabsConfig.content.customContent.enableFluids)
            LabsFluids.preInit();

        if (LabsConfig.content.gtCustomContent.enableBlocks)
            LabsMetaBlocks.preInit();

        LabsRecipeMaps.preInit();
        LabsRemappers.preInit();
    }

    public static void postInit() {
        if (LabsConfig.content.gtCustomContent.enableOldMultiblocks)
            LabsMultiblocks.initOld();
        if (LabsConfig.content.gtCustomContent.enableNewMultiblocks)
            LabsMultiblocks.initNew();
        // GreenhouseTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        LabsItems.register(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        LabsBlocks.register(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        if (LabsConfig.content.gtCustomContent.enablePerfectGems) {
            /* Initialize Custom OrePrefixes & Material Flags */
            LabsOrePrefix.init();
            LabsMaterialFlags.init();
        }
        if (LabsConfig.content.gtCustomContent.enableMaterials) {
            LabsMaterials.init();
            LabsMaterials.materialChanges();
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (LabsConfig.content.customContent.enableComplexRecipes)
            event.getRegistry().register(new HandFramingRecipe(LabsNames.makeLabsName(
                    Objects.requireNonNull(LabsItems.HAND_FRAMING_TOOL.getRegistryName()).getPath() + "_recipe")));
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LabsValues.LABS_MODID))
            ConfigManager.sync(LabsValues.LABS_MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void missingItemMappings(MissingMappings<Item> event) {
        LabsRemappers.remapItems(event);
    }

    @SubscribeEvent
    public static void missingBlockMappings(MissingMappings<Block> event) {
        LabsRemappers.remapBlocks(event);
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NomiLabs.LOGGER.info("player logged in");
        //DataFixerHandler.playerLoggedIn(event); TODO NEEDED?
    }

    @SubscribeEvent
    public static void worldLoadEvent(WorldEvent.Load event) {
        NomiLabs.LOGGER.info("world load event");
    }
}
