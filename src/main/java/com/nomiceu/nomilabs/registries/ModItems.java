package com.nomiceu.nomilabs.registries;

import com.nomiceu.nomilabs.item.*;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;

@SuppressWarnings("DuplicatedCode")
public class ModItems {
    private static final String nullTranslationKey = "item.null";

    /* Deprecated Items */
    public static BaseItem BLAZE_POWDER;
    public static BaseItem DARK_RED_COAL;

    /* Coins */
    public static BaseItem NOMICOIN_1;
    public static BaseItem NOMICOIN_5;
    public static BaseItem NOMICOIN_25;
    public static BaseItem NOMICOIN_100;

    /* Widgets */
    public static BaseItem WOOD_WIDGET;
    public static BaseItem WOOD_WIDGET_LEFT;
    public static BaseItem WOOD_WIDGET_RIGHT;
    public static BaseItem STONE_WIDGET;
    public static BaseItem STONE_WIDGET_UP;
    public static BaseItem STONE_WIDGET_DOWN;
    public static BaseItem ALLOY_WIDGET;
    public static BaseItem ENDER_WIDGET;

    /* Space Items */
    public static BaseItem RADIATION_LAYER;
    public static BaseItem PRESSURE_LAYER;
    public static BaseItem CLOTH;
    public static BaseItem THERMAL_CLOTH;
    public static BaseItem UNPREPARED_SPACE_HELMET;
    public static BaseItem UNPREPARED_SPACE_CHESTPIECE;
    public static BaseItem UNPREPARED_SPACE_LEGGINGS;
    public static BaseItem UNPREPARED_SPACE_BOOTS;

    /* Micro Miner Items */
    public static BaseItem QUANTUM_FLUX;
    public static BaseItem GEM_SENSOR;
    public static BaseItem WARP_ENGINE;
    public static BaseItem UNIVERSAL_NAVIGATOR;
    public static BaseItem QUANTUM_FLUXED_ETERNIUM_PLATING;
    public static BaseItem T1_GUIDANCE;
    public static BaseItem T2_GUIDANCE;
    public static BaseItem T1_LASER;
    public static BaseItem T2_LASER;
    public static BaseItem T3_LASER;

    /* Micro Miners */
    public static BaseItem T1_SHIP;
    public static BaseItem T2_SHIP;
    public static BaseItem T3_SHIP;
    public static BaseItem T4_SHIP;
    public static BaseItem T4_HALF_SHIP;
    public static BaseItem T5_SHIP;
    public static BaseItem T6_SHIP;
    public static BaseItem T7_SHIP;
    public static BaseItem T8_SHIP;
    public static BaseItem T8_HALF_SHIP;
    public static BaseItem T9_SHIP;
    public static BaseItem T10_SHIP;

    /* Stabilized Micro Miners */
    public static BaseItem T1_STABILIZED_SHIP;
    public static BaseItem T2_STABILIZED_SHIP;
    public static BaseItem T3_STABILIZED_SHIP;
    public static BaseItem T4_STABILIZED_SHIP;
    public static BaseItem T4_HALF_STABILIZED_SHIP;
    public static BaseItem T5_STABILIZED_SHIP;
    public static BaseItem T6_STABILIZED_SHIP;
    public static BaseItem T7_STABILIZED_SHIP;
    public static BaseItem T8_STABILIZED_SHIP;

    /* Stabilized Matter */
    public static BaseItem T1_STABILIZED_MATTER;
    public static BaseItem T2_STABILIZED_MATTER;
    public static BaseItem T3_STABILIZED_MATTER;
    public static BaseItem T4_STABILIZED_MATTER;
    public static BaseItem T4_HALF_STABILIZED_MATTER;
    public static BaseItem T5_STABILIZED_MATTER;
    public static BaseItem T6_STABILIZED_MATTER;
    public static BaseItem T7_STABILIZED_MATTER;
    public static BaseItem T8_STABILIZED_MATTER;

    /* Data */
    public static BaseItem DRAGON_DATA;
    public static BaseItem WITHER_DATA;
    public static BaseItem IMPOSSIBLE_DATA;
    public static BaseItem UNIVERSE_DATA;
    public static BaseItem STELLAR_DATA;
    public static BaseItem CHAOS_DRAGON_DATA;

    /* Solidified Items */
    public static BaseItem SOLIDIFIED_ARGON;
    public static BaseItem SOLIDIFIED_CHLORINE;
    public static BaseItem SOLIDIFIED_FLUORINE;
    public static BaseItem SOLIDIFIED_HELIUM;
    public static BaseItem SOLIDIFIED_HYDROGEN;
    public static BaseItem SOLIDIFIED_KRYPTON;
    public static BaseItem SOLIDIFIED_MERCURY;
    public static BaseItem SOLIDIFIED_NEON;
    public static BaseItem SOLIDIFIED_NITROGEN;
    public static BaseItem SOLIDIFIED_OXYGEN;
    public static BaseItem SOLIDIFIED_RADON;
    public static BaseItem SOLIDIFIED_XENON;

    /* Stabilized Items */
    public static BaseItem STABILIZED_EINSTEINIUM;
    public static BaseItem STABILIZED_BERKELIUM;
    public static BaseItem STABILIZED_NEPTUNIUM;
    public static BaseItem STABILIZED_PLUTONIUM;
    public static BaseItem STABILIZED_URANIUM;
    public static BaseItem STABILIZED_CURIUM;
    public static BaseItem STABILIZED_CALIFORNIUM;
    public static BaseItem STABILIZED_AMERICIUM;

    /* Endgame Items */
    public static BaseItem HEART_OF_THE_UNIVERSE;
    public static BaseItem CREATIVE_TANK_MOLD;
    public static BaseItem EXOTIC_MATERIALS_CATALYST;
    public static BaseItem ETERNAL_CATALYST;
    public static BaseItem ULTIMATE_GEM;

    /* Misc Items */
    public static ItemHandFramingTool HAND_FRAMING_TOOL;
    public static ItemSmore[] SMORES;

    public static BaseItem GRAINS_OF_INNOCENCE;

    public static BaseItem RADIUM_SALT;
    public static BaseItem MOON_DUST;

    public static BaseItem REDSTONE_ARMOR_PLATE;
    public static BaseItem CARBON_ARMOR_PLATE;
    public static BaseItem LAPIS_ARMOR_PLATE;


    public static BaseItem COMPRESSED_OCTADIC_CAPACITOR;
    public static BaseItem DOUBLE_COMPRESSED_OCTADIC_CAPACITOR;

    // Core and North are part of the Crafting Nether Star mod.
    public static BaseItem NETHER_STAR_SOUTH;
    public static BaseItem NETHER_STAR_EAST;
    public static BaseItem NETHER_STAR_WEST;

    // Hydrogen is part of Solidified Items section.
    public static BaseItem DENSE_HYDROGEN;
    public static BaseItem ULTRA_DENSE_HYDROGEN;

    public static BaseItem MAGNETRON;

    public static void preInit() {
        /* Deprecated Items */
        BLAZE_POWDER = new BaseItem(makeCTName("blazepowder"), ModCreativeTabs.NOMI_CORE);
        DARK_RED_COAL = new BaseItem(makeCTName("dark_red_coal"), ModCreativeTabs.NOMI_CORE);

        /* Coins */
        NOMICOIN_1 = new BaseItem(makeCTName("omnicoin"), ModCreativeTabs.NOMI_CORE);
        NOMICOIN_5 = new BaseItem(makeCTName("omnicoin5"), ModCreativeTabs.NOMI_CORE, EnumRarity.UNCOMMON);
        NOMICOIN_25 = new BaseItem(makeCTName("omnicoin25"), ModCreativeTabs.NOMI_CORE, EnumRarity.RARE);
        NOMICOIN_100 = new BaseItem(makeCTName("omnicoin100"), ModCreativeTabs.NOMI_CORE, EnumRarity.EPIC);

        /* Widgets */
        WOOD_WIDGET = new BaseItem(makeCTName("woodenwidget"), ModCreativeTabs.NOMI_CORE);
        WOOD_WIDGET_LEFT = new BaseItem(makeCTName("woodwidgetleft"), ModCreativeTabs.NOMI_CORE);
        WOOD_WIDGET_RIGHT = new BaseItem(makeCTName("woodwidgetright"), ModCreativeTabs.NOMI_CORE);
        STONE_WIDGET = new BaseItem(makeCTName("stonewidget"), ModCreativeTabs.NOMI_CORE);
        STONE_WIDGET_UP = new BaseItem(makeCTName("stonewidgetup"), ModCreativeTabs.NOMI_CORE);
        STONE_WIDGET_DOWN = new BaseItem(makeCTName("stonewidgetdown"), ModCreativeTabs.NOMI_CORE);
        ALLOY_WIDGET = new BaseItem(makeCTName("alloywidget"), ModCreativeTabs.NOMI_CORE);
        ENDER_WIDGET = new BaseItem(makeCTName("enderwidget"), ModCreativeTabs.NOMI_CORE);

        /* Space Items */
        RADIATION_LAYER = new BaseItem(makeCTName("radiationlayer"), ModCreativeTabs.NOMI_CORE);
        PRESSURE_LAYER = new BaseItem(makeCTName("pressurelayer"), ModCreativeTabs.NOMI_CORE);
        CLOTH = new BaseItem(makeCTName("cloth"), ModCreativeTabs.NOMI_CORE);
        THERMAL_CLOTH = new BaseItem(makeCTName("thermalcloth"), ModCreativeTabs.NOMI_CORE);
        UNPREPARED_SPACE_HELMET = new BaseItem(makeCTName("unpreparedspacehelmet"), ModCreativeTabs.NOMI_CORE);
        UNPREPARED_SPACE_CHESTPIECE = new BaseItem(makeCTName("unpreparedspacechestpiece"), ModCreativeTabs.NOMI_CORE);
        UNPREPARED_SPACE_LEGGINGS = new BaseItem(makeCTName("unpreparedspaceleggings"), ModCreativeTabs.NOMI_CORE);
        UNPREPARED_SPACE_BOOTS = new BaseItem(makeCTName("unpreparedspaceboots"), ModCreativeTabs.NOMI_CORE);

        HAND_FRAMING_TOOL = new ItemHandFramingTool(makeCTName("hand_framing_tool"), ModCreativeTabs.NOMI_CORE);
        SMORES = new ItemSmore[4];
        makeSmores();
    }

    public static void register(IForgeRegistry<Item> registry) {
        /* Deprecated Items */
        registerItem(BLAZE_POWDER, registry);
        registerItem(DARK_RED_COAL, registry);

        /* Coins */
        registerItem(NOMICOIN_1, registry);
        registerItem(NOMICOIN_5, registry);
        registerItem(NOMICOIN_25, registry);
        registerItem(NOMICOIN_100, registry);

        /* Widgets */
        registerItem(WOOD_WIDGET, registry);
        registerItem(WOOD_WIDGET_LEFT, registry);
        registerItem(WOOD_WIDGET_RIGHT, registry);
        registerItem(STONE_WIDGET, registry);
        registerItem(STONE_WIDGET_UP, registry);
        registerItem(STONE_WIDGET_DOWN, registry);
        registerItem(ALLOY_WIDGET, registry);
        registerItem(ENDER_WIDGET, registry);

        /* Space */
        registerItem(RADIATION_LAYER, registry);
        registerItem(PRESSURE_LAYER, registry);
        registerItem(CLOTH, registry);
        registerItem(THERMAL_CLOTH, registry);
        registerItem(UNPREPARED_SPACE_HELMET, registry);
        registerItem(UNPREPARED_SPACE_CHESTPIECE, registry);
        registerItem(UNPREPARED_SPACE_LEGGINGS, registry);
        registerItem(UNPREPARED_SPACE_BOOTS, registry);

        registerItem(HAND_FRAMING_TOOL, registry);

        for (ItemSmore smore : SMORES) {
            registerItem(smore, registry);
        }
    }

    public static void registerModels() {
        /* Deprecated Items */
        registerModel(BLAZE_POWDER);
        registerModel(DARK_RED_COAL);

        /* Coins */
        registerModel(NOMICOIN_1);
        registerModel(NOMICOIN_5);
        registerModel(NOMICOIN_25);
        registerModel(NOMICOIN_100);

        /* Widgets */
        registerModel(WOOD_WIDGET);
        registerModel(WOOD_WIDGET_LEFT);
        registerModel(WOOD_WIDGET_RIGHT);
        registerModel(STONE_WIDGET);
        registerModel(STONE_WIDGET_UP);
        registerModel(STONE_WIDGET_DOWN);
        registerModel(ALLOY_WIDGET);
        registerModel(ENDER_WIDGET);

        /* Space */
        registerModel(RADIATION_LAYER);
        registerModel(PRESSURE_LAYER);
        registerModel(CLOTH);
        registerModel(THERMAL_CLOTH);
        registerModel(UNPREPARED_SPACE_HELMET);
        registerModel(UNPREPARED_SPACE_CHESTPIECE);
        registerModel(UNPREPARED_SPACE_LEGGINGS);
        registerModel(UNPREPARED_SPACE_BOOTS);

        registerModel(HAND_FRAMING_TOOL);

        for (ItemSmore smore : SMORES) {
            registerModel(smore);
        }
    }

    private static void registerModel(Item item) {
        ResourceLocation rl = item.getRegistryName();
        assert rl != null;
        ModelBakery.registerItemVariants(item, rl);
        ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
    }

    private static void registerItem(Item item, IForgeRegistry<Item> registry) {
        registry.register(item);
        if (item.getTranslationKey().equals(nullTranslationKey)) {
            ResourceLocation rl = item.getRegistryName();
            assert rl != null;
            item.setTranslationKey(rl.getNamespace() + "." + rl.getPath());
        }
    }

    private static void makeSmores() {
        String [] smores = new String[]{
            "eightsmore",
            "sixteensmore",
            "thirtytwosmore",
            "sixtyfoursmore"
        };
        Potion[] potions = new Potion[]{
            MobEffects.ABSORPTION,
            MobEffects.SPEED,
            MobEffects.HASTE,
            MobEffects.SATURATION,
            MobEffects.HEALTH_BOOST,
            MobEffects.REGENERATION
        };
        EnumRarity[] rarities = EnumRarity.values();

        int heal = 44;
        float saturation = 8.6f;
        int potionDuration = 1200;
        int potionAmplifier = 0;
        int index = 0;

        ItemSmore smore;

        for (String smoreName : smores) {
            heal *= 2;
            heal += 4;

            saturation *= 2;
            saturation++;

            potionDuration *= 2;

            smore = new ItemSmore(heal, saturation, makeCTName(smoreName), ModCreativeTabs.NOMI_CORE)
                    .setRarity(rarities[index]);

            for (Potion potion : potions)
                smore.addPotionEffect(potion, potionDuration, potionAmplifier);

            SMORES[index] = smore;

            potionAmplifier++;
            index++;
        }

    }
}
