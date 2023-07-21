package com.nomiceu.nomilabs.registry;

import com.nomiceu.nomilabs.item.*;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;

public class LabsItems {
    private static final String nullTranslationKey = "item.null";

    private static final List<Item> ITEMS = new ArrayList<>();

    /* Deprecated Items */
    public static ItemBase BLAZE_POWDER;
    public static ItemBase DARK_RED_COAL;

    /* Coins */
    public static ItemBase NOMICOIN_1;
    public static ItemBase NOMICOIN_5;
    public static ItemBase NOMICOIN_25;
    public static ItemBase NOMICOIN_100;

    /* Widgets */
    public static ItemBase WOOD_WIDGET;
    public static ItemBase WOOD_WIDGET_LEFT;
    public static ItemBase WOOD_WIDGET_RIGHT;
    public static ItemBase STONE_WIDGET;
    public static ItemBase STONE_WIDGET_UP;
    public static ItemBase STONE_WIDGET_DOWN;
    public static ItemBase ALLOY_WIDGET;
    public static ItemBase ENDER_WIDGET;

    /* Space Items */
    public static ItemBase RADIATION_LAYER;
    public static ItemBase PRESSURE_LAYER;
    public static ItemBase CLOTH;
    public static ItemBase THERMAL_CLOTH;
    public static ItemBase UNPREPARED_SPACE_HELMET;
    public static ItemBase UNPREPARED_SPACE_CHESTPIECE;
    public static ItemBase UNPREPARED_SPACE_LEGGINGS;
    public static ItemBase UNPREPARED_SPACE_BOOTS;

    /* Micro Miner Items */
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

    /* Micro Miners */
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

    /* Stabilized Micro Miners */
    public static ItemBase T1_STABILIZED_SHIP;
    public static ItemBase T2_STABILIZED_SHIP;
    public static ItemBase T3_STABILIZED_SHIP;
    public static ItemBase T4_STABILIZED_SHIP;
    public static ItemBase T4_HALF_STABILIZED_SHIP;
    public static ItemBase T5_STABILIZED_SHIP;
    public static ItemBase T6_STABILIZED_SHIP;
    public static ItemBase T7_STABILIZED_SHIP;
    public static ItemBase T8_STABILIZED_SHIP;

    /* Stabilized Matter */
    public static ItemBase T1_STABILIZED_MATTER;
    public static ItemBase T2_STABILIZED_MATTER;
    public static ItemBase T3_STABILIZED_MATTER;
    public static ItemBase T4_STABILIZED_MATTER;
    public static ItemBase T4_HALF_STABILIZED_MATTER;
    public static ItemBase T5_STABILIZED_MATTER;
    public static ItemBase T6_STABILIZED_MATTER;
    public static ItemBase T7_STABILIZED_MATTER;
    public static ItemBase T8_STABILIZED_MATTER;

    /* Data */
    public static ItemBase DRAGON_DATA;
    public static ItemBase WITHER_DATA;
    public static ItemBase IMPOSSIBLE_DATA;
    public static ItemBase UNIVERSE_DATA;
    public static ItemBase STELLAR_DATA;
    public static ItemBase CHAOS_DRAGON_DATA;

    /* Solidified Items */
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

    /* Stabilized Items */
    public static ItemBase STABILIZED_EINSTEINIUM;
    public static ItemBase STABILIZED_BERKELIUM;
    public static ItemBase STABILIZED_NEPTUNIUM;
    public static ItemBase STABILIZED_PLUTONIUM;
    public static ItemBase STABILIZED_URANIUM;
    public static ItemBase STABILIZED_CURIUM;
    public static ItemBase STABILIZED_CALIFORNIUM;
    public static ItemBase STABILIZED_AMERICIUM;

    /* Endgame Items */
    public static ItemBase HEART_OF_THE_UNIVERSE;
    public static ItemBase CREATIVE_TANK_MOLD;
    public static ItemBase EXOTIC_MATERIALS_CATALYST;
    public static ItemBase ETERNAL_CATALYST;
    public static ItemBase ULTIMATE_GEM;

    /* Misc Items */
    public static ItemBase GRAINS_OF_INNOCENCE;

    public static ItemBase RADIUM_SALT;
    public static ItemBase MOON_DUST;

    public static ItemBase REDSTONE_ARMOR_PLATE;
    public static ItemBase CARBON_ARMOR_PLATE;
    public static ItemBase LAPIS_ARMOR_PLATE;


    public static ItemBase COMPRESSED_OCTADIC_CAPACITOR;
    public static ItemBase DOUBLE_COMPRESSED_OCTADIC_CAPACITOR;

    // Core and North are part of the Crafting Nether Star mod.
    public static ItemBase NETHER_STAR_SOUTH;
    public static ItemBase NETHER_STAR_EAST;
    public static ItemBase NETHER_STAR_WEST;

    // Hydrogen is part of Solidified Items section.
    public static ItemBase DENSE_HYDROGEN;
    public static ItemBase ULTRA_DENSE_HYDROGEN;

    public static ItemBase MAGNETRON;

    /* Custom Behaviour Items (aka items not extending BaseItem) */
    public static ItemHandFramingTool HAND_FRAMING_TOOL;
    public static ItemSmore[] SMORES;

    public static void preInit() {
        /* Deprecated Items */
        BLAZE_POWDER = createItem(new ItemBase(makeCTName("blazepowder"), LabsCreativeTabs.TAB_NOMI_LABS));
        DARK_RED_COAL = createItem(new ItemBase(makeCTName("dark_red_coal"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Coins */
        NOMICOIN_1 = createItem(new ItemBase(makeCTName("omnicoin"), LabsCreativeTabs.TAB_NOMI_LABS));
        NOMICOIN_5 = createItem(new ItemBase(makeCTName("omnicoin5"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        NOMICOIN_25 = createItem(new ItemBase(makeCTName("omnicoin25"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        NOMICOIN_100 = createItem(new ItemBase(makeCTName("omnicoin100"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));

        /* Widgets */
        WOOD_WIDGET = createItem(new ItemBase(makeCTName("woodenwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_LEFT = createItem(new ItemBase(makeCTName("woodwidgetleft"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_RIGHT = createItem(new ItemBase(makeCTName("woodwidgetright"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET = createItem(new ItemBase(makeCTName("stonewidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_UP = createItem(new ItemBase(makeCTName("stonewidgetup"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_DOWN = createItem(new ItemBase(makeCTName("stonewidgetdown"), LabsCreativeTabs.TAB_NOMI_LABS));
        ALLOY_WIDGET = createItem(new ItemBase(makeCTName("alloywidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        ENDER_WIDGET = createItem(new ItemBase(makeCTName("enderwidget"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Space Items */
        RADIATION_LAYER = createItem(new ItemBase(makeCTName("radiationlayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        PRESSURE_LAYER = createItem(new ItemBase(makeCTName("pressurelayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        CLOTH = createItem(new ItemBase(makeCTName("cloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        THERMAL_CLOTH = createItem(new ItemBase(makeCTName("thermalcloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_HELMET = createItem(new ItemBase(makeCTName("unpreparedspacehelmet"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_CHESTPIECE = createItem(new ItemBase(makeCTName("unpreparedspacechestpiece"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_LEGGINGS = createItem(new ItemBase(makeCTName("unpreparedspaceleggings"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_BOOTS = createItem(new ItemBase(makeCTName("unpreparedspaceboots"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Micro Miner Items */
        QUANTUM_FLUX = createItem(new ItemBase(makeCTName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(new ItemBase(makeCTName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(new ItemBase(makeCTName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(new ItemBase(makeCTName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new ItemBase(makeCTName("quantumfluxedeterniumplating"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(new ItemBase(makeCTName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(new ItemBase(makeCTName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(new ItemBase(makeCTName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new ItemBase(makeCTName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new ItemBase(makeCTName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));

        /* Micro Miners */
        T1_SHIP = createItem(new ItemBase(makeCTName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T2_SHIP = createItem(new ItemBase(makeCTName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T3_SHIP = createItem(new ItemBase(makeCTName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_SHIP = createItem(new ItemBase(makeCTName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_HALF_SHIP = createItem(new ItemBase(makeCTName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T5_SHIP = createItem(new ItemBase(makeCTName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T6_SHIP = createItem(new ItemBase(makeCTName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T7_SHIP = createItem(new ItemBase(makeCTName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T8_SHIP = createItem(new ItemBase(makeCTName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_HALF_SHIP = createItem(new ItemBase(makeCTName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T9_SHIP = createItem(new ItemBase(makeCTName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T10_SHIP = createItem(new ItemBase(makeCTName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Stabilized Micro Miners */
        T1_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tieroneship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiertwoship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierthreeship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfourship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfourandhalfship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfiveship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiersixship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiersevenship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiereightship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Stabilized Matter */
        T1_STABILIZED_MATTER= createItem(new ItemBase(makeCTName("tieroneship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiertwoship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierthreeship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfourship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfourandhalfship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfiveship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiersixship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiersevenship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiereightship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Data */
        DRAGON_DATA = createItem(new ItemBase(makeCTName("dragonlairdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WITHER_DATA = createItem(new ItemBase(makeCTName("witherrealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        IMPOSSIBLE_DATA = createItem(new ItemBase(makeCTName("impossiblerealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        UNIVERSE_DATA = createItem(new ItemBase(makeCTName("universecreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        STELLAR_DATA = createItem(new ItemBase(makeCTName("stellarcreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        CHAOS_DRAGON_DATA = createItem(new ItemBase(makeCTName("lairofthechaosguardiandata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));

        /* Solidified Items */
        SOLIDIFIED_ARGON = createItem(new ItemBase(makeCTName("solidifiedargon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_CHLORINE = createItem(new ItemBase(makeCTName("solidifiedchlorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_FLUORINE = createItem(new ItemBase(makeCTName("solidifiedfluorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HELIUM = createItem(new ItemBase(makeCTName("solidifiedhelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HYDROGEN = createItem(new ItemBase(makeCTName("solidifiedhydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_KRYPTON = createItem(new ItemBase(makeCTName("solidifiedkrypton"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_MERCURY = createItem(new ItemBase(makeCTName("solidifiedmercury"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NEON = createItem(new ItemBase(makeCTName("solidifiedneon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NITROGEN = createItem(new ItemBase(makeCTName("solidifiednitrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_OXYGEN = createItem(new ItemBase(makeCTName("solidifiedoxygen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_RADON = createItem(new ItemBase(makeCTName("solidifiedradon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_XENON = createItem(new ItemBase(makeCTName("solidifiedxenon"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Stabilized Items */
        STABILIZED_EINSTEINIUM = createItem(new ItemBase(makeCTName("stabilizedeinsteinium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_BERKELIUM = createItem(new ItemBase(makeCTName("stabilizedberkelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_NEPTUNIUM = createItem(new ItemBase(makeCTName("stabilizedneptunium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_PLUTONIUM = createItem(new ItemBase(makeCTName("stabilizedplutonium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_URANIUM = createItem(new ItemBase(makeCTName("stabilizeduranium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CURIUM = createItem(new ItemBase(makeCTName("stabilizedcurium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CALIFORNIUM = createItem(new ItemBase(makeCTName("stabilizedcalifornium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_AMERICIUM = createItem(new ItemBase(makeCTName("stabilizedamericium"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Endgame Items */
        HEART_OF_THE_UNIVERSE = createItem(new ItemBase(makeCTName("heartofauniverse"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        CREATIVE_TANK_MOLD = createItem(new ItemBase(makeCTName("creativeportabletankmold"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        EXOTIC_MATERIALS_CATALYST = createItem(new ItemBase(makeCTName("exoticmaterialscatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        ETERNAL_CATALYST = createItem(new ItemBase(makeCTName("eternalcatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        ULTIMATE_GEM = createItem(new ItemBase(makeCTName("ultimate_gem"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));

        /* Misc Items */
        GRAINS_OF_INNOCENCE = createItem(new ItemBase(makeCTName("grainsofinnocence"), LabsCreativeTabs.TAB_NOMI_LABS));

        RADIUM_SALT = createItem(new ItemBase(makeCTName("radiumsalt"), LabsCreativeTabs.TAB_NOMI_LABS));
        MOON_DUST = createItem(new ItemBase(makeCTName("moondust"), LabsCreativeTabs.TAB_NOMI_LABS));

        REDSTONE_ARMOR_PLATE = createItem(new ItemBase(makeCTName("redstonearmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        CARBON_ARMOR_PLATE = createItem(new ItemBase(makeCTName("carbonarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        LAPIS_ARMOR_PLATE = createItem(new ItemBase(makeCTName("lapisarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));

        COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemBase(makeCTName("compressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));
        DOUBLE_COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemBase(makeCTName("doublecompressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Core and North are part of the Crafting Nether Star mod.
        NETHER_STAR_SOUTH = createItem(new ItemBase(makeCTName("netherstarsouth"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_EAST = createItem(new ItemBase(makeCTName("netherstareast"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_WEST = createItem(new ItemBase(makeCTName("netherstarwest"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Hydrogen is part of Solidified Items section.
        DENSE_HYDROGEN = createItem(new ItemBase(makeCTName("densehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        ULTRA_DENSE_HYDROGEN = createItem(new ItemBase(makeCTName("ultradensehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));

        MAGNETRON = createItem(new ItemBase(makeCTName("magnetron"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Custom Behaviour Items */
        HAND_FRAMING_TOOL = createItem(new ItemHandFramingTool(makeCTName("hand_framing_tool"), LabsCreativeTabs.TAB_NOMI_LABS));
        SMORES = new ItemSmore[4];
        createSmores();
    }

    public static void register(IForgeRegistry<Item> registry) {
        /* Register All Items in ITEMS */
        for (Item item : ITEMS) {
            registerItem(item, registry);
        }

        /* Register Smores */
        for (ItemSmore smore : SMORES) {
            registerItem(smore, registry);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        /* Register Models of All Items in ITEMS */
        for (Item item : ITEMS) {
            registerModel(item);
        }

        /* Register Models of Smores */
        for (ItemSmore smore : SMORES) {
            registerModel(smore);
        }
    }

    public static <T extends Item> T createItem(T item) {
        ITEMS.add(item);
        return item;
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

    private static void createSmores() {
        String [] smores = new String[]{
            "eightsmore",
            "sixteensmore",
            "thirtytwosmore",
            "sixtyfoursmore"
        };
        Potion[] potions = new Potion[]{
            MobEffects.ABSORPTION,
            MobEffects.SPEED,
            MobEffects.JUMP_BOOST,
            MobEffects.HASTE,
            MobEffects.SATURATION,
            MobEffects.HEALTH_BOOST,
            MobEffects.REGENERATION,
            MobEffects.STRENGTH
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

            smore = new ItemSmore(heal, saturation, makeCTName(smoreName), LabsCreativeTabs.TAB_NOMI_LABS)
                    .setRarity(rarities[index]);

            for (Potion potion : potions)
                smore.addPotionEffect(potion, potionDuration, potionAmplifier);

            SMORES[index] = smore;

            potionAmplifier++;
            index++;
        }

    }
}
