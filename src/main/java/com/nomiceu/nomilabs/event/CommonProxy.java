package com.nomiceu.nomilabs.event;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.gregtech.LabsSounds;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.multiblock.registry.LabsMultiblocks;
import com.nomiceu.nomilabs.gregtech.prefix.LabsMaterialFlags;
import com.nomiceu.nomilabs.gregtech.prefix.LabsOrePrefix;
import com.nomiceu.nomilabs.gregtech.recipe.PerfectGemsCutterRecipes;
import com.nomiceu.nomilabs.integration.top.TOPTooltipManager;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.recipe.HandFramingRecipe;
import com.nomiceu.nomilabs.remap.LabsRemappers;
import com.nomiceu.nomilabs.remap.Remapper;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.util.LabsModeHelper;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.MaterialRegistryEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {
    public static void preInit() {
        LabsModeHelper.check();
        
        LabsCreativeTabs.preInit();

        if (LabsConfig.content.customContent.enableItems)
            LabsItems.preInit();
        if (LabsConfig.content.customContent.enableBlocks)
            LabsBlocks.preInit();
        if (LabsConfig.content.customContent.enableFluids)
            LabsFluids.preInit();

        if (LabsConfig.content.gtCustomContent.enableBlocks)
            LabsMetaBlocks.preInit();

        LabsSounds.register();
        LabsRemappers.preInit();
        LabsRecipeMaps.preInit();
        LabsMultiblocks.preInit();

        if (LabsConfig.modIntegration.enableTOPIntegration && Loader.isModLoaded(LabsValues.TOP_MODID))
            TOPTooltipManager.registerProviders();

        DataFixerHandler.preInit();
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
        if (LabsConfig.content.gtCustomContent.enableMaterials)
            LabsMaterials.init();
    }

    @SubscribeEvent
    public static void materialChanges(PostMaterialEvent event) {
        if (LabsConfig.content.gtCustomContent.enableMaterials)
            LabsMaterials.materialChanges();
    }

    @SubscribeEvent
    public static void createMaterialRegistry(MaterialRegistryEvent event) {
        GregTechAPI.materialManager.createRegistry(LabsValues.LABS_MODID);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (LabsConfig.content.gtCustomContent.enablePerfectGems && LabsOrePrefix.GEM_PERFECT != null)
            PerfectGemsCutterRecipes.initRecipes();
        if (LabsConfig.content.customContent.enableComplexRecipes && LabsItems.HAND_FRAMING_TOOL != null)
            event.getRegistry().register(new HandFramingRecipe(LabsNames.makeLabsName("hand_framing_recipe")));

        //com.nomiceu.nomilabs.recipe.LabsTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LabsValues.LABS_MODID))
            ConfigManager.sync(LabsValues.LABS_MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void missingItemMappings(MissingMappings<Item> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.ITEM);
    }

    @SubscribeEvent
    public static void missingBlockMappings(MissingMappings<Block> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.BLOCK);
    }

    @SubscribeEvent
    public static void missingEntityMappings(MissingMappings<EntityEntry> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.ENTITY);
    }

    @SubscribeEvent
    public static void missingBiomeMappings(MissingMappings<Biome> event) {
        LabsRemappers.remapAndIgnoreEntries(event, Remapper.RemapTypes.BIOME);
    }
}
