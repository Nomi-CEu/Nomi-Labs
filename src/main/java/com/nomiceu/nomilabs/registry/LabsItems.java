package com.nomiceu.nomilabs.registry;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;

public class LabsItems {
    private static final String nullTranslationKey = "item.null";

    private static final List<Item> ITEMS = new ArrayList<>();

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

    /* Custom Behaviour Items (aka items not extending BaseItem) */
    public static ItemHandFramingTool HAND_FRAMING_TOOL;
    public static ItemSmore[] SMORES;

    public static void preInit() {
        /* Deprecated Items */
        BLAZE_POWDER = createItem(new BaseItem(makeCTName("blazepowder"), LabsCreativeTabs.TAB_NOMI_LABS));
        DARK_RED_COAL = createItem(new BaseItem(makeCTName("dark_red_coal"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Coins */
        NOMICOIN_1 = createItem(new BaseItem(makeCTName("omnicoin"), LabsCreativeTabs.TAB_NOMI_LABS));
        NOMICOIN_5 = createItem(new BaseItem(makeCTName("omnicoin5"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        NOMICOIN_25 = createItem(new BaseItem(makeCTName("omnicoin25"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        NOMICOIN_100 = createItem(new BaseItem(makeCTName("omnicoin100"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));

        /* Widgets */
        WOOD_WIDGET = createItem(new BaseItem(makeCTName("woodenwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_LEFT = createItem(new BaseItem(makeCTName("woodwidgetleft"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_RIGHT = createItem(new BaseItem(makeCTName("woodwidgetright"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET = createItem(new BaseItem(makeCTName("stonewidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_UP = createItem(new BaseItem(makeCTName("stonewidgetup"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_DOWN = createItem(new BaseItem(makeCTName("stonewidgetdown"), LabsCreativeTabs.TAB_NOMI_LABS));
        ALLOY_WIDGET = createItem(new BaseItem(makeCTName("alloywidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        ENDER_WIDGET = createItem(new BaseItem(makeCTName("enderwidget"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Space Items */
        RADIATION_LAYER = createItem(new BaseItem(makeCTName("radiationlayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        PRESSURE_LAYER = createItem(new BaseItem(makeCTName("pressurelayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        CLOTH = createItem(new BaseItem(makeCTName("cloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        THERMAL_CLOTH = createItem(new BaseItem(makeCTName("thermalcloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_HELMET = createItem(new BaseItem(makeCTName("unpreparedspacehelmet"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_CHESTPIECE = createItem(new BaseItem(makeCTName("unpreparedspacechestpiece"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_LEGGINGS = createItem(new BaseItem(makeCTName("unpreparedspaceleggings"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_BOOTS = createItem(new BaseItem(makeCTName("unpreparedspaceboots"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Micro Miner Items */
        QUANTUM_FLUX = createItem(new BaseItem(makeCTName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(new BaseItem(makeCTName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(new BaseItem(makeCTName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(new BaseItem(makeCTName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new BaseItem(makeCTName("quantumfluxedeterniumplating"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(new BaseItem(makeCTName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(new BaseItem(makeCTName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(new BaseItem(makeCTName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new BaseItem(makeCTName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new BaseItem(makeCTName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));

        /* Micro Miners */
        T1_SHIP = createItem(new BaseItem(makeCTName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T2_SHIP = createItem(new BaseItem(makeCTName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T3_SHIP = createItem(new BaseItem(makeCTName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_SHIP = createItem(new BaseItem(makeCTName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_HALF_SHIP = createItem(new BaseItem(makeCTName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T5_SHIP = createItem(new BaseItem(makeCTName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T6_SHIP = createItem(new BaseItem(makeCTName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T7_SHIP = createItem(new BaseItem(makeCTName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T8_SHIP = createItem(new BaseItem(makeCTName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_HALF_SHIP = createItem(new BaseItem(makeCTName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T9_SHIP = createItem(new BaseItem(makeCTName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T10_SHIP = createItem(new BaseItem(makeCTName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Stabilized Micro Miners */
        T1_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tieroneship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tiertwoship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tierthreeship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tierfourship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tierfourandhalfship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tierfiveship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tiersixship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tiersevenship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_SHIP = createItem(new BaseItem(makeCTName("tiereightship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Stabilized Matter */
        T1_STABILIZED_MATTER= createItem(new BaseItem(makeCTName("tieroneship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tiertwoship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tierthreeship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tierfourship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tierfourandhalfship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tierfiveship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tiersixship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tiersevenship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_MATTER = createItem(new BaseItem(makeCTName("tiereightship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));

        /* Data */
        DRAGON_DATA = createItem(new BaseItem(makeCTName("dragonlairdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WITHER_DATA = createItem(new BaseItem(makeCTName("witherrealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        IMPOSSIBLE_DATA = createItem(new BaseItem(makeCTName("impossiblerealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        UNIVERSE_DATA = createItem(new BaseItem(makeCTName("universecreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        STELLAR_DATA = createItem(new BaseItem(makeCTName("stellarcreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        CHAOS_DRAGON_DATA = createItem(new BaseItem(makeCTName("lairofthechaosguardiandata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));

        /* Solidified Items */
        SOLIDIFIED_ARGON = createItem(new BaseItem(makeCTName("solidifiedargon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_CHLORINE = createItem(new BaseItem(makeCTName("solidifiedchlorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_FLUORINE = createItem(new BaseItem(makeCTName("solidifiedfluorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HELIUM = createItem(new BaseItem(makeCTName("solidifiedhelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HYDROGEN = createItem(new BaseItem(makeCTName("solidifiedhydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_KRYPTON = createItem(new BaseItem(makeCTName("solidifiedkrypton"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_MERCURY = createItem(new BaseItem(makeCTName("solidifiedmercury"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NEON = createItem(new BaseItem(makeCTName("solidifiedneon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NITROGEN = createItem(new BaseItem(makeCTName("solidifiednitrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_OXYGEN = createItem(new BaseItem(makeCTName("solidifiedoxygen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_RADON = createItem(new BaseItem(makeCTName("solidifiedradon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_XENON = createItem(new BaseItem(makeCTName("solidifiedxenon"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Stabilized Items */
        STABILIZED_EINSTEINIUM = createItem(new BaseItem(makeCTName("stabilizedeinsteinium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_BERKELIUM = createItem(new BaseItem(makeCTName("stabilizedberkelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_NEPTUNIUM = createItem(new BaseItem(makeCTName("stabilizedneptunium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_PLUTONIUM = createItem(new BaseItem(makeCTName("stabilizedplutonium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_URANIUM = createItem(new BaseItem(makeCTName("stabilizeduranium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CURIUM = createItem(new BaseItem(makeCTName("stabilizedcurium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CALIFORNIUM = createItem(new BaseItem(makeCTName("stabilizedcalifornium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_AMERICIUM = createItem(new BaseItem(makeCTName("stabilizedamericium"), LabsCreativeTabs.TAB_NOMI_LABS));

        /* Endgame Items */
        HEART_OF_THE_UNIVERSE = createItem(new BaseItem(makeCTName("heartofauniverse"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        CREATIVE_TANK_MOLD = createItem(new BaseItem(makeCTName("creativeportabletankmold"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        EXOTIC_MATERIALS_CATALYST = createItem(new BaseItem(makeCTName("exoticmaterialscatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        ETERNAL_CATALYST = createItem(new BaseItem(makeCTName("eternalcatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        ULTIMATE_GEM = createItem(new BaseItem(makeCTName("ultimate_gem"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));

        /* Misc Items */
        GRAINS_OF_INNOCENCE = createItem(new BaseItem(makeCTName("grainsofinnocence"), LabsCreativeTabs.TAB_NOMI_LABS));

        RADIUM_SALT = createItem(new BaseItem(makeCTName("radiumsalt"), LabsCreativeTabs.TAB_NOMI_LABS));
        MOON_DUST = createItem(new BaseItem(makeCTName("moondust"), LabsCreativeTabs.TAB_NOMI_LABS));

        REDSTONE_ARMOR_PLATE = createItem(new BaseItem(makeCTName("redstonearmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        CARBON_ARMOR_PLATE = createItem(new BaseItem(makeCTName("carbonarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        LAPIS_ARMOR_PLATE = createItem(new BaseItem(makeCTName("lapisarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));

        COMPRESSED_OCTADIC_CAPACITOR = createItem(new BaseItem(makeCTName("compressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));
        DOUBLE_COMPRESSED_OCTADIC_CAPACITOR = createItem(new BaseItem(makeCTName("doublecompressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Core and North are part of the Crafting Nether Star mod.
        NETHER_STAR_SOUTH = createItem(new BaseItem(makeCTName("netherstarsouth"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_EAST = createItem(new BaseItem(makeCTName("netherstareast"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_WEST = createItem(new BaseItem(makeCTName("netherstarwest"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Hydrogen is part of Solidified Items section.
        DENSE_HYDROGEN = createItem(new BaseItem(makeCTName("densehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        ULTRA_DENSE_HYDROGEN = createItem(new BaseItem(makeCTName("ultradensehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));

        MAGNETRON = createItem(new BaseItem(makeCTName("magnetron"), LabsCreativeTabs.TAB_NOMI_LABS));

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
        ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
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
