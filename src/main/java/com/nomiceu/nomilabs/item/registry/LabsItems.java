package com.nomiceu.nomilabs.item.registry;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import com.nomiceu.nomilabs.item.*;
import com.nomiceu.nomilabs.item.registry.register.*;

import gregtech.common.blocks.MetaBlocks;

@SuppressWarnings("unused")
public class LabsItems {

    private static final String nullTranslationKey = "item.null";

    private static final List<Item> ITEMS = new ArrayList<>();

    private static final Set<Item> NO_MODEL_HANDLING_ITEMS = new HashSet<>();

    /**
     * Coins
     */
    public static ItemBase NOMICOIN_1;
    public static ItemBase NOMICOIN_5;
    public static ItemBase NOMICOIN_25;
    public static ItemBase NOMICOIN_100;

    /**
     * Widgets
     */
    public static ItemBase WOOD_WIDGET;
    public static ItemBase WOOD_WIDGET_LEFT;
    public static ItemBase WOOD_WIDGET_RIGHT;
    public static ItemBase STONE_WIDGET;
    public static ItemBase STONE_WIDGET_UP;
    public static ItemBase STONE_WIDGET_DOWN;
    public static ItemBase ALLOY_WIDGET;
    public static ItemBase ENDER_WIDGET;

    /**
     * Space Items
     */
    public static ItemBase RADIATION_LAYER;
    public static ItemBase PRESSURE_LAYER;
    public static ItemBase CLOTH;
    public static ItemBase THERMAL_CLOTH;
    public static ItemBase UNPREPARED_SPACE_HELMET;
    public static ItemBase UNPREPARED_SPACE_CHESTPIECE;
    public static ItemBase UNPREPARED_SPACE_LEGGINGS;
    public static ItemBase UNPREPARED_SPACE_BOOTS;

    /**
     * Micro Miner Items
     */
    public static ItemBase QUANTUM_FLUX;
    public static ItemBase GEM_SENSOR;
    public static ItemBase WARP_ENGINE;
    public static ItemBase UNIVERSAL_NAVIGATOR;
    public static ItemBase QUANTUM_FLUXED_ETERNIUM_PLATING;
    public static ItemBase T1_GUIDANCE;
    public static ItemBase T2_GUIDANCE;
    public static ItemBase T1_LASER;
    public static ItemBase T2_LASER;
    public static ItemBase T3_LASER;

    /**
     * Micro Miners
     */
    public static ItemBase T1_SHIP;
    public static ItemBase T2_SHIP;
    public static ItemBase T3_SHIP;
    public static ItemBase T4_SHIP;
    public static ItemBase T4_HALF_SHIP;
    public static ItemBase T5_SHIP;
    public static ItemBase T6_SHIP;
    public static ItemBase T7_SHIP;
    public static ItemBase T8_SHIP;
    public static ItemBase T8_HALF_SHIP;
    public static ItemBase T9_SHIP;
    public static ItemBase T10_SHIP;

    /**
     * Stabilized Micro Miners
     */
    public static ItemBase T1_STABILIZED_SHIP;
    public static ItemBase T2_STABILIZED_SHIP;
    public static ItemBase T3_STABILIZED_SHIP;
    public static ItemBase T4_STABILIZED_SHIP;
    public static ItemBase T4_HALF_STABILIZED_SHIP;
    public static ItemBase T5_STABILIZED_SHIP;
    public static ItemBase T6_STABILIZED_SHIP;
    public static ItemBase T7_STABILIZED_SHIP;
    public static ItemBase T8_STABILIZED_SHIP;

    /**
     * Stabilized Matter
     */
    public static ItemBase T1_STABILIZED_MATTER;
    public static ItemBase T2_STABILIZED_MATTER;
    public static ItemBase T3_STABILIZED_MATTER;
    public static ItemBase T4_STABILIZED_MATTER;
    public static ItemBase T4_HALF_STABILIZED_MATTER;
    public static ItemBase T5_STABILIZED_MATTER;
    public static ItemBase T6_STABILIZED_MATTER;
    public static ItemBase T7_STABILIZED_MATTER;
    public static ItemBase T8_STABILIZED_MATTER;

    /**
     * Data
     */
    public static ItemBase DRAGON_DATA;
    public static ItemBase WITHER_DATA;
    public static ItemBase IMPOSSIBLE_DATA;
    public static ItemBase UNIVERSE_DATA;
    public static ItemBase STELLAR_DATA;
    public static ItemBase CHAOS_DRAGON_DATA;

    /**
     * Solidified Items
     */
    public static ItemBase SOLIDIFIED_ARGON;
    public static ItemBase SOLIDIFIED_CHLORINE;
    public static ItemBase SOLIDIFIED_FLUORINE;
    public static ItemBase SOLIDIFIED_HELIUM;
    public static ItemBase SOLIDIFIED_HYDROGEN;
    public static ItemBase SOLIDIFIED_KRYPTON;
    public static ItemBase SOLIDIFIED_MERCURY;
    public static ItemBase SOLIDIFIED_NEON;
    public static ItemBase SOLIDIFIED_NITROGEN;
    public static ItemBase SOLIDIFIED_OXYGEN;
    public static ItemBase SOLIDIFIED_RADON;
    public static ItemBase SOLIDIFIED_XENON;

    /**
     * Stabilized Items
     */
    public static ItemBase STABILIZED_EINSTEINIUM;
    public static ItemBase STABILIZED_BERKELIUM;
    public static ItemBase STABILIZED_NEPTUNIUM;
    public static ItemBase STABILIZED_PLUTONIUM;
    public static ItemBase STABILIZED_URANIUM;
    public static ItemBase STABILIZED_CURIUM;
    public static ItemBase STABILIZED_CALIFORNIUM;
    public static ItemBase STABILIZED_AMERICIUM;

    /**
     * Endgame Items
     */
    public static ItemBase HEART_OF_THE_UNIVERSE;
    public static ItemBase CREATIVE_TANK_MOLD;
    public static ItemBase EXOTIC_MATERIALS_CATALYST;
    public static ItemBase ETERNAL_CATALYST;
    public static ItemBase ULTIMATE_GEM;

    /**
     * Misc Items
     */
    public static ItemBase GRAINS_OF_INNOCENCE;

    public static ItemBase RADIUM_SALT;
    public static ItemBase MOON_DUST;

    public static ItemBase REDSTONE_ARMOR_PLATE;
    public static ItemBase CARBON_ARMOR_PLATE;
    public static ItemBase LAPIS_ARMOR_PLATE;

    // Core and North are part of the Crafting Nether Star mod.
    public static ItemBase NETHER_STAR_SOUTH;
    public static ItemBase NETHER_STAR_EAST;
    public static ItemBase NETHER_STAR_WEST;

    // Hydrogen is part of Solidified Items section.
    public static ItemBase DENSE_HYDROGEN;
    public static ItemBase ULTRA_DENSE_HYDROGEN;

    public static ItemBase INDUSTRIAL_REBREATHER_KIT;

    public static ItemBase MAGNETRON;

    public static ItemTinyCoke TINY_COKE;

    public static ItemInfo INFO_ITEM;

    public static ItemBase PULSATING_DUST;
    public static ItemBase PULSATING_MESH;

    /**
     * Custom Behavior Items
     * These are Items not extending BaseItem
     */
    public static ItemSmore[] SMORES;

    /**
     * Mod Specific Items
     * These are items that are only loaded if a certain mod is loaded.
     */
    public static ItemCapacitor COMPRESSED_OCTADIC_CAPACITOR;
    public static ItemCapacitor DOUBLE_COMPRESSED_OCTADIC_CAPACITOR;
    public static ItemHandFramingTool HAND_FRAMING_TOOL;

    public static void preInit() {
        /* Coins */
        LabsCoinsWidgets.initCoins();

        /* Widgets */
        LabsCoinsWidgets.initWidgets();

        /* Space Items */
        LabsSpace.initSpace();

        /* Micro Miner Items */
        LabsMicroMiners.initMicroMinerItems();

        /* Micro Miners */
        LabsMicroMiners.initMicroMiners();

        /* Stabilized Micro Miners */
        LabsMicroMiners.initStabilizedMicroMiners();

        /* Stabilized Matter */
        LabsMicroMiners.initStabilizedMatter();

        /* Data */
        LabsData.initData();

        /* Solidified Items */
        LabsSolidified.initSolidified();

        /* Stabilized Items */
        LabsSolidified.initStabilized();

        /* Endgame Items */
        LabsEndgame.initEndgame();

        /* Misc Items */
        LabsMisc.initMisc();

        /* Custom Behavior Items */
        LabsMisc.initCustomBehavior();

        /* Mod Specific Items */
        LabsModSpecific.initModSpecific();
    }

    /* HELPER FUNCTIONS */
    public static void register(IForgeRegistry<Item> registry) {
        for (Item item : ITEMS) {
            registerItem(item, registry);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for (Item item : ITEMS) {
            if (NO_MODEL_HANDLING_ITEMS.contains(item)) continue; // Skip Model Handling for these items
            registerModel(item);
        }

        registerCustomModels();
    }

    private static void registerCustomModels() {
        /* Add an Animated Version of Blaze Powder. Used in quest icons, so it fits in with the other Thermal Quests. */

        // Need to set a custom model resource location for meta 0, otherwise that appears as null.
        var mrlBlaze0 = new ModelResourceLocation("blaze_powder", "inventory");
        ModelLoader.setCustomModelResourceLocation(Items.BLAZE_POWDER, 0, mrlBlaze0);

        // Add Animated Model
        var mrlBlaze1 = new ModelResourceLocation("blaze_powder_animated", "inventory");
        ModelLoader.setCustomModelResourceLocation(Items.BLAZE_POWDER, 1, mrlBlaze1);

        // Add the same model for all metas in ItemInfo
        var rlInfo = Objects.requireNonNull(INFO_ITEM.getRegistryName());
        ModelBakery.registerItemVariants(INFO_ITEM, rlInfo);
        var mrlInfo = new ModelResourceLocation(rlInfo, "inventory");
        ModelLoader.setCustomModelResourceLocation(INFO_ITEM, 0, mrlInfo);

        for (var meta : INFO_ITEM.getSubMetas()) {
            ModelLoader.setCustomModelResourceLocation(INFO_ITEM, meta, mrlInfo);
        }
    }

    public static <T extends Item> T createItem(T item) {
        ITEMS.add(item);
        return item;
    }

    public static <T extends Item> T createItemWithoutModelHandling(T item) {
        NO_MODEL_HANDLING_ITEMS.add(item);
        return createItem(item);
    }

    private static void registerItem(Item item, IForgeRegistry<Item> registry) {
        registry.register(item);
        if (item.getTranslationKey().equals(nullTranslationKey)) {
            ResourceLocation rl = item.getRegistryName();
            assert rl != null;
            item.setTranslationKey(rl.getNamespace() + "." + rl.getPath());
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        ResourceLocation rl = item.getRegistryName();
        assert rl != null;
        ModelBakery.registerItemVariants(item, rl);
        ModelResourceLocation mrl;

        // Block model registering
        if (item instanceof ItemBlock itemBlock) {
            Block block = itemBlock.getBlock();
            for (IBlockState state : block.getBlockState().getValidStates()) {
                mrl = new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()),
                        MetaBlocks.statePropertiesToString(state.getProperties()));
                ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(state), mrl);
            }
            return;
        }

        mrl = new ModelResourceLocation(rl, "inventory");
        assert item.getCreativeTab() != null;

        // Item does not have subtypes
        if (!item.getHasSubtypes()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
            return;
        }

        // Register each sub item's model
        NonNullList<ItemStack> subItems = NonNullList.create();
        item.getSubItems(item.getCreativeTab(), subItems);

        for (ItemStack stack : subItems)
            ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), mrl);
    }
}
