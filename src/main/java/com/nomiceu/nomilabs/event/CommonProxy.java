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
import com.nomiceu.nomilabs.recipe.GreenhouseTestRecipes;
import com.nomiceu.nomilabs.recipe.HandFramingRecipe;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.unification.material.event.MaterialEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = LabsValues.LABS_MODID)
@SuppressWarnings("unused")
public class CommonProxy {

    public static void preInit() {
        if (LabsConfig.enableCustomContent) {
            LabsCreativeTabs.preInit();
            LabsItems.preInit();
            LabsBlocks.preInit();
            LabsFluids.preInit();
        }
        if (LabsConfig.enableGTCustomContent) {
            LabsMetaBlocks.preInit();
            LabsRecipeMaps.preInit();
        }
    }

    public static void postInit() {
        if (LabsConfig.enableGTCustomContent)
            LabsMultiblocks.postInit();
        // GreenhouseTestRecipes.postInit();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        if (LabsConfig.enableCustomContent)
            LabsItems.register(registry);
        if (LabsConfig.enableGTCustomContent)
            LabsMetaBlocks.registerItems(registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        if (LabsConfig.enableCustomContent)
            LabsBlocks.register(registry);
        if (LabsConfig.enableGTCustomContent)
            LabsMetaBlocks.registerBlocks(registry);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        if (LabsConfig.enableGTCustomContent) {
            LabsMaterials.init();
            LabsMaterials.materialChanges();
        }
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (LabsConfig.enableCustomContent) {
            event.getRegistry().register(new HandFramingRecipe(Objects.requireNonNull(Item.getByNameOrId("contenttweaker:hand_framing_tool")).getDefaultInstance())
                .setRegistryName(LabsNames.makeLabsName(LabsItems.HAND_FRAMING_TOOL.getRegistryName() + "_recipe")));
        }
    }
}
