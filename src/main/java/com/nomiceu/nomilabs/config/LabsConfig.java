package com.nomiceu.nomilabs.config;

import net.minecraftforge.common.config.Config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.nomiceu.nomilabs.LabsValues;

@SuppressWarnings({ "CanBeFinal", "unused" })
@Config(modid = LabsValues.LABS_MODID, name = LabsValues.LABS_MODID, category = "")
public class LabsConfig {

    @Config.Comment("Content Settings")
    @Config.LangKey("config.nomilabs.content")
    @Config.Name("content")
    public static Content content = new Content();

    @Config.Comment({ "GroovyScript Extensions and Script Helper Settings" })
    @Config.LangKey("config.nomilabs.groovy")
    @Config.Name("groovyscript settings")
    public static GroovyScriptSettings groovyScriptSettings = new GroovyScriptSettings();

    @Config.Comment("Mod Integration Settings")
    @Config.LangKey("config.nomilabs.mod_integration")
    @Config.Name("mod integration")
    public static ModIntegration modIntegration = new ModIntegration();

    @Config.Comment("Advanced Settings")
    @Config.LangKey("config.nomilabs.advanced")
    @Config.Name("advanced")
    public static Advanced advanced = new Advanced();

    public static class Content {

        @Config.Comment("Custom Content Settings")
        @Config.LangKey("config.nomilabs.content.custom_content")
        @Config.Name("custom content")
        @Config.RequiresMcRestart
        public CustomContent customContent = new CustomContent();

        @Config.Comment("GregTech Custom Content Settings")
        @Config.LangKey("config.nomilabs.content.gt_content")
        @Config.Name("gt content")
        @Config.RequiresMcRestart
        public GTCustomContent gtCustomContent = new GTCustomContent();

        public static class CustomContent {

            @Config.Comment({
                    "Enable Custom Items.",
                    "They will not have recipes.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.custom_content.items")
            @Config.RequiresMcRestart
            public boolean enableItems = true;

            @Config.Comment({
                    "Enable Custom Blocks.",
                    "They will not have recipes.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.custom_content.blocks")
            @Config.RequiresMcRestart
            public boolean enableBlocks = true;

            @Config.Comment({
                    "Enable Custom Fluids.",
                    "They will not have recipes.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.custom_content.fluids")
            @Config.RequiresMcRestart
            public boolean enableFluids = true;

            @Config.Comment({
                    "Enable Custom Complex Recipes.",
                    "Currently Just Contains the Hand Framing Recipe.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.custom_content.complex_recipes")
            @Config.RequiresMcRestart
            public boolean enableComplexRecipes = true;

            @Config.Comment({
                    "Enable a Custom Void Dimension.",
                    "By default, has no blocks, and there is no way to reach it.",
                    "DimensionalEdibles (Nomifactory Edition) can allow transport to it, as well as provide a spawn obsidian platform.",
                    "[default: false]"
            })
            @Config.LangKey("config.nomilabs.content.custom_content.custom_void")
            @Config.RequiresMcRestart
            public boolean enableVoidDimension = false;
        }

        public static class GTCustomContent {

            @Config.Comment({
                    "Enable Custom Materials.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.gt_content.materials")
            @Config.RequiresMcRestart
            public boolean enableMaterials = true;

            @Config.Comment({
                    "Enable Perfect Gems.",
                    "They will have cutter recipes turning them into 2 Exquisite Gems.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.gt_content.perfect_gems")
            @Config.RequiresMcRestart
            public boolean enablePerfectGems = true;

            @Config.Comment({
                    "Enable Custom GT Blocks.",
                    "In Beta.",
                    "[default: false]"
            })
            @Config.LangKey("config.nomilabs.content.gt_content.blocks")
            @Config.RequiresMcRestart
            public boolean enableBlocks = false;

            @Config.Comment({
                    "Enable Old Multiblocks.",
                    "These are NOT new to this Core Mod. They exist in pre-core-mod versions of Nomi-CEu.",
                    "They have been improved.",
                    "[default: true]"
            })
            @Config.LangKey("config.nomilabs.content.gt_content.old_multiblocks")
            @Config.RequiresMcRestart
            public boolean enableOldMultiblocks = true;

            @Config.Comment({
                    "Enable New Multiblocks.",
                    "These are new to this Core Mod, as in they do not exist in pre-core-mod versions of Nomi-CEu.",
                    "Will not work properly if Custom GT Blocks is turned off.",
                    "In Beta.",
                    "[default: false]"
            })
            @Config.LangKey("config.nomilabs.content.gt_content.new_multiblocks")
            @Config.RequiresMcRestart
            public boolean enableNewMultiblocks = false;
        }
    }

    public static class GroovyScriptSettings {

        @Config.Comment({ "Whether to enable Labs' GroovyScript Hand Additions.",
                "[default: false]" })
        @Config.LangKey("config.nomilabs.groovy.hand")
        public boolean enableGroovyHandAdditions = false;

        @Config.Comment({ "Mode to Use for GT Recipe Output Searching.",
                "'LINEAR_SEARCH' browses each recipe sequentially, 'FAST_TREE' navigates a tree structure and stops at the first match, while 'TREE' explores the entire tree structure before concluding.",
                "Because of the extra generated tree, if no removals occur, TREE and FAST_TREE have a slightly longer launch time. They also have slightly higher memory usage (around 20-50MB in limited testing)",
                "If a small amount of removals occur, game launching is around the same for all three, but TREE or FAST_TREE has the lowest script reload time.",
                "With a moderate-high amount of removals, game launching and script reloading is much faster with FAST_TREE or TREE, and FAST_TREE does consistently out perform TREE in time.",
                "TREE is a safer option if all recipes need to be grabbed, but FAST_TREE has not failed to grab any recipes in the limited testing.",
                "If some recipes are left over, try using TREE mode.",
                "FAST_DISCARDED_TREE and DISCARDED_TREE are similar to FAST_TREE and TREE, but the tree is discarded after initial load. This saves memory during gameplay, making this a good option for non-dev instances.",
                "Once the tree is discarded, if search by output is needed, linear search is used.",
                "Recipe Output Searching is used when replacing ABS recipes and Mixer Recipes in Composition Replacements, and in Recipe Output Searching or Removing.",
                "[default: FAST_TREE]" })
        @Config.LangKey("config.nomilabs.groovy.recipe_search_mode")
        @Config.RequiresMcRestart
        public GTRecipeSearchMode gtRecipeSearchMode = GTRecipeSearchMode.FAST_TREE;

        public enum GTRecipeSearchMode {
            LINEAR_SEARCH,
            FAST_TREE,
            TREE,
            FAST_DISCARDED_TREE,
            DISCARDED_TREE,
        }
    }

    public static class ModIntegration {

        @Config.Comment({ "Whether to enable NuclearCraft Integration, which fixes its crash with GTCEu.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.nuclearcraft")
        @Config.RequiresMcRestart
        public boolean enableNuclearCraftIntegration = true;

        @Config.Comment({ "Whether to enable Extra Utilities 2 Integration, which removes frequencies.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.xu2")
        @Config.RequiresMcRestart
        public boolean enableExtraUtils2Integration = true;

        @Config.Comment({
                "Whether to enable The One Probe Integration, which adds some messages to the TOP panel when hovering over certain blocks.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.top")
        @Config.RequiresMcRestart
        public boolean enableTOPIntegration = true;

        @Config.Comment({
                "Whether to enable Ender Storage Integration, which allows data fixes to remap Ender Storage Chests' Contents.",
                "If this is in a Nomi-CEu Environment, make sure this stays at true, or your world may break, and items be lost!",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.ender_storage")
        @Config.RequiresMcRestart
        public boolean enableEnderStorageIntegration = true;

        @Config.Comment({
                "Whether to enable Ender IO Integration, which adds a tooltip beneath capacitors displaying their level.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.ender_io")
        @Config.RequiresMcRestart
        public boolean enableEnderIOIntegration = true;

        @Config.Comment("Draconic Evolution Integration Settings")
        @Config.LangKey("config.nomilabs.mod_integration.draconicevolution")
        @Config.Name("draconic evolution integration")
        public final DraconicEvolutionIntegration draconicEvolutionIntegration = new DraconicEvolutionIntegration();

        @Config.Comment({
                "Whether to enable Advanced Rocketry Integration, which fixes Advanced Rocketry registering items for Fluid Blocks.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.advanced_rocketry")
        @Config.RequiresMcRestart
        public boolean enableAdvancedRocketryIntegration = true;

        @Config.Comment({
                "Whether to enable ArchitectureCraft Integration, which adds new slope variants, improves the GUI of the Sawbench, fixes the Sawbench Particle Texture, and fixes Shapes' Harvest Tools and Levels in The One Probe.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.architecture_craft")
        @Config.RequiresMcRestart
        public boolean enableArchitectureCraftIntegration = true;

        @Config.Comment({
                "Whether to enable Default World Generator Port Integration, which fixes scaling issues, and adds a cancel button.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.default_world_gen")
        @Config.RequiresMcRestart
        public boolean enableDefaultWorldGenIntegration = true;

        @Config.Comment("Effortless Building Integration Settings")
        @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding")
        @Config.Name("effortless building integration")
        public final EffortlessBuildingIntegration effortlessBuildingIntegration = new EffortlessBuildingIntegration();

        @Config.Comment({
                "Whether to enable FTB Utilities Integration. Makes Status Messages more consistent, translatable, and fixes issues relating to Ghost Items.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.ftb_utils")
        @Config.RequiresMcRestart
        public boolean enableFTBUtilsIntegration = true;

        @Config.Comment({
                "Whether to enable TOP Addons Integration. Fixes Error Messages with ArchitectureCraft 3.108.",
                "[default: true]" })
        @Config.LangKey("config.nomilabs.mod_integration.top_addons")
        @Config.RequiresMcRestart
        public boolean enableTopAddonsIntegration = true;

        @Config.Comment({
                "Whether to add a Empty Line between any Crafting Recipe Output Tooltips in JEI.",
                "Examples of Crafting Recipe Output Tooltips are `Recipe By <MOD_ID>` and `Recipe ID: <RECIPE_ID>`.",
                "[default: true]",
        })
        @Config.LangKey("config.nomilabs.mod_integration.jei_crafting_output_empty_line")
        public boolean addJEICraftingOutputEmptyLine = true;

        @Config.Comment({
                "Whether to enable Better Questing Fluid Task Fixes.",
                "Fixes detection of Fluids in Stacked Fluid Containers, and in Simple Fluid Containers.",
                "Causes over-consuming of fluids in Stacked Simple Fluid Containers, due to limitations with emptying them.",
                "[default: true]",
        })
        @Config.LangKey("config.nomilabs.mod_integration.bqu_fluid_task_fixes")
        public boolean enableBQuFluidTaskFixes = true;

        @Config.Comment("AE2 Terminal Options")
        @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal")
        @Config.Name("ae2 terminal options")
        public final AE2TerminalOptions ae2TerminalOptions = new AE2TerminalOptions();

        public static class AE2TerminalOptions {

            @Config.Comment({ "Whether to Auto-Focus the Fluid Terminal.", "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal.fluid")
            public boolean autoFocusFluid = true;

            @Config.Comment({ "Whether to Auto-Focus the Interface Terminal.", "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal.interface")
            public boolean autoFocusInterface = true;

            @Config.Comment({ "Whether to Auto-Focus the Interface Configuration Terminal.", "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal.cfg_interface")
            public boolean autoFocusConfigInterface = true;

            @Config.Comment({ "Whether to Auto-Focus the Fluid Interface Configuration Terminal.", "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal.cfg_fluid_interface")
            public boolean autoFocusConfigFluidInterface = true;

            @Config.Comment({
                    "Whether to Save Serach Strings in the Interface Configuration Terminals (Item and Fluid).",
                    "Default AE2 Behaviour is to Save.", "[default: false]" })
            @Config.LangKey("config.nomilabs.mod_integration.ae2_terminal.cfg_interface_save")
            public boolean saveConfigInterfaceSearch = false;
        }

        public static class EffortlessBuildingIntegration {

            @Config.Comment({ "Whether to enable Effortless Building Integration, which splits the parts of reach.",
                    "Also fixes various Dupe and Transmutation Bugs, and fixes allowing Placing Blocks in FTB Utils Claimed Chunks.",
                    "None of the below options work if this config is set to false.",
                    "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.enable")
            @Config.RequiresMcRestart
            public boolean enableEffortlessBuildingIntegration = true;

            @Config.Comment({ "Max Reach Per Axis Without Upgrades.",
                    "[default: 8]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.axis.0")
            public int axisReach0 = 8;

            @Config.Comment({ "Max Reach Per Axis With 1 Upgrade.",
                    "[default: 16]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.axis.1")
            public int axisReach1 = 16;

            @Config.Comment({ "Max Reach Per Axis With 2 Upgrades.",
                    "[default: 32]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.axis.2")
            public int axisReach2 = 32;

            @Config.Comment({ "Max Reach Per Axis With 3 Upgrades.",
                    "[default: 64]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.axis.3")
            public int axisReach3 = 64;

            @Config.Comment({ "Max Reach Per Axis In Creative.",
                    "[default: 2048]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.axis.creative")
            public int axisReachCreative = 2048;

            @Config.Comment({ "Max Blocks Placed at Once Without Upgrades.",
                    "[default: 256]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.blocks.0")
            public int blocksPlaced0 = 256;

            @Config.Comment({ "Max Blocks Placed at Once With 1 Upgrade.",
                    "[default: 2048]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.blocks.1")
            public int blocksPlaced1 = 2048;

            @Config.Comment({ "Max Blocks Placed at Once With 2 Upgrades.",
                    "[default: 16384]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.blocks.2")
            public int blocksPlaced2 = 16384;

            @Config.Comment({ "Max Blocks Placed at Once With 3 Upgrades.",
                    "[default: 131072]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.blocks.3")
            public int blocksPlaced3 = 131072;

            @Config.Comment({ "Max Blocks Placed at Once In Creative.",
                    "[default: 1048576]" })
            @Config.LangKey("config.nomilabs.mod_integration.effortlessbuilding.blocks.creative")
            public int blocksPlacedCreative = 1048576;
        }

        public static class DraconicEvolutionIntegration {

            @Config.Comment({ "Whether to enable Draconic Evolution Integration, which adds many features, such as:",
                    "Allowing GregTech Draconium and Awakened Draconium in the reactor and energy core.",
                    "Modifying Energy Core Structure with improvements, such as allowing blocks surrounding the structure.",
                    "Adding a destructor to the energy core.",
                    "Allow changing the speed of the builder.",
                    "Allow disabling Fusion Recipes for Chaotic Upgrades, which are empty as none of the tools support it.",
                    "If this option is disabled, then energy cores made whilst this was enabled may break!",
                    "None of the below options work if this config is set to false.",
                    "[default: true]" })
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.enable")
            @Config.RequiresMcRestart
            public boolean enableDraconicEvolutionIntegration = true;

            @Config.Comment({ "The speed of the Builder, in blocks per tick.",
                    "Set this to 0 to have the builder be instant.",
                    "[default: 1]" })
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.auto_builder_speed")
            @Config.RangeInt(min = 0)
            public int autoBuilderSpeed = 1;

            @Config.Comment({ "The speed of the Destructor, in blocks per tick.",
                    "Set this to 0 to have the destructor be instant.",
                    "[default: 1]" })
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.auto_destructor_speed")
            @Config.RangeInt(min = 0)
            public int autoDestructorSpeed = 1;

            @Config.Comment({
                    "Rough time in Ticks, Required for the Charging Phase of Fusion Injectors, for each tier.",
                    "Charging Time is assuming Maximum Energy is given to the Injector Each Tick.",
                    "Should have 4 Values: Basic, Wyvern, Draconic, Chaotic.",
                    "Set this to 1 to Disable Energy Limits, and have the Fusion Injector take in as much power as provided.",
                    "[default: 300, 220, 140, 60]" })
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.fusion_charging_time")
            @Config.RangeInt(min = 1)
            @Config.RequiresWorldRestart
            public int[] fusionChargingTime = new int[] { 300, 220, 140, 60 };
        }
    }

    public static class Advanced {

        @Config.Comment({ "Whether to disable Anvil XP Scaling.",
                "[default: false]" })
        @Config.LangKey("config.nomilabs.advanced.disable_xp_scaling")
        public boolean disableXpScaling = false;

        @Config.Comment({ "Amount of XP Per Level, for Linear XP Scaling.",
                "Used for Linear XP Scaling in Actually Additions, EIO and Thermal Items/Machines.",
                "Note that for Thermal, XP fixes are only applied for the Tome of Knowledge, not for any machines associated with Essence of Knowledge.",
                "MUST be used in conjunction with UT's Linear XP Scaling Config, else weird issues may happen!",
                "Enter a value of 0 for default.",
                "[default: 0]" })
        @Config.LangKey("config.nomilabs.advanced.other_mods_linear_xp")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0)
        public int otherModsLinearXp = 0;

        @Config.Comment({ "Whether to disable the Narrator.",
                "Fixes Crashes in Arm Macs, in some very specific environments.",
                "If your game is crashing, try enabling this!",
                "[default: false]" })
        @Config.LangKey("config.nomilabs.advanced.disable_narrator")
        public boolean disableNarrator = false;

        @Config.Comment({ "Whether to enable Nomi-CEu data fixes.",
                "This is used for Nomi-CEu, for players coming from before core-mod.",
                "If this mod is being used in other scenarios, leave this at false, as this may break worlds!",
                "If this is in a Nomi-CEu environment, make sure it is true, and do not change it, or items/blocks may be lost!",
                "[default: false]" })
        @Config.LangKey("config.nomilabs.advanced.enable_nomi_ceu_data_fixes")
        @Config.RequiresWorldRestart
        public boolean enableNomiCEuDataFixes = false;

        @Config.Comment("Fluid Registry Settings")
        @Config.LangKey("config.nomilabs.advanced.fluid_registry")
        @Config.Name("fluid registry")
        public final FluidRegistry fluidRegistrySettings = new FluidRegistry();

        @Config.Comment({ "Tier Detectors, which get the Tier a Player is On based on Quest Completion.",
                "Currently only used in Nomi-CEu for Rich Presence." })
        @Config.LangKey("config.nomilabs.advanced.tiers")
        @Config.Name("tier settings")
        public final TierSettings tierSettings = new TierSettings();

        @Config.Comment({ "Control Menu Tooltip Settings, which are used to help with default keybind overrides.",
                "The actual override setting occurs in GroovyScript!" })
        @Config.LangKey("config.nomilabs.advanced.controls_tooltips")
        @Config.Name("control menu tooltip settings")
        public final ControlMenuTooltipSettings controlMenuTooltipSettings = new ControlMenuTooltipSettings();

        @Config.Comment({ "Overrides for the Minecraft Window." })
        @Config.LangKey("config.nomilabs.advanced.window")
        @Config.Name("minecraft window overrides")
        public final WindowOverrides windowOverrides = new WindowOverrides();

        @Config.Comment({ "List of Regex Patterns to ignore if they are included in the ITEM missing registry list.",
                "Do not change unless you know what you are doing!",
                "This can be very inefficient with lots of patterns and lots of missing registries. Try to condense it into one pattern!",
                "This is an OR, so if an item matches ANY of the patterns, it is ignored.",
                "An item is only ignored if its ENTIRE Resource Location matches the pattern.",
                "Example: `minecraft:.*` (Ignores all Minecraft Items)",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.ignore_items")
        @Config.RequiresMcRestart
        public String[] ignoreItems = new String[0];

        @Config.Comment({ "List of Regex Patterns to ignore if they are included in the BLOCK missing registry list.",
                "Do not change unless you know what you are doing!",
                "This can be very inefficient with lots of patterns and lots of missing registries. Try to condense it into one pattern!",
                "This is an OR, so if a block matches ANY of the patterns, it is ignored.",
                "A block is only ignored if its ENTIRE Resource Location matches the pattern.",
                "Example: `minecraft:.*` (Ignores all Minecraft Blocks)",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.ignore_blocks")
        @Config.RequiresMcRestart
        public String[] ignoreBlocks = new String[0];

        @Config.Comment({ "List of Regex Patterns to ignore if they are included in the ENTITY missing registry list.",
                "Do not change unless you know what you are doing!",
                "This can be very inefficient with lots of patterns and lots of missing registries. Try to condense it into one pattern!",
                "This is an OR, so if an entity matches ANY of the patterns, it is ignored.",
                "An entity is only ignored if its ENTIRE Resource Location matches the pattern.",
                "Example: `minecraft:.*` (Ignores all Minecraft Entities)",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.ignore_entities")
        @Config.RequiresMcRestart
        public String[] ignoreEntities = new String[0];

        @Config.Comment({ "List of Regex Patterns to ignore if they are included in the BIOME missing registry list.",
                "Do not change unless you know what you are doing!",
                "This can be very inefficient with lots of patterns and lots of missing registries. Try to condense it into one pattern!",
                "This is an OR, so if a biome matches ANY of the patterns, it is ignored.",
                "A biome is only ignored if its ENTIRE Resource Location matches the pattern.",
                "Example: `minecraft:.*` (Ignores all Minecraft Biomes)",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.ignore_biomes")
        @Config.RequiresMcRestart
        public String[] ignoreBiomes = new String[0];

        @Config.Comment({ "List of Fields to be client side only, acting as @SideOnly(Side.CLIENT).",
                "DOES NOT WORK WITH CLASSES FROM MINECRAFT OR FORGE!",
                "Does not work with classes loaded before Nomi Labs' Static Init!",
                "Do not change unless you know what you are doing!",
                "Helps with fixing GrS errors Server Side.",
                "Format: <CLASS>@<FIELD>",
                "Example: gregtech/api/recipes/recipeproperties/TemperatureProperty@KEY",
                "Accepts Obfuscated Fields, but they must be under the Obfuscated Name.",
                "Many Client Side Only modifications, on the same class, may be inefficient.",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.client_side_fields")
        @Config.RequiresMcRestart
        public String[] clientSideFields = new String[0];

        @Config.Comment({ "List of Methods to be client side only, acting as @SideOnly(Side.CLIENT).",
                "DOES NOT WORK WITH CLASSES FROM MINECRAFT OR FORGE!",
                "Does not work with classes loaded before Nomi Labs' Static Init!",
                "Do not change unless you know what you are doing!",
                "Helps with fixing GrS errors Server Side.",
                "Format: <CLASS>@<METHOD>@<DESC>",
                "Example: gregtech/api/recipes/recipeproperties/TemperatureProperty@drawInfo@(Lnet/minecraft/client/Minecraft;IIILjava/lang/Object;)V",
                "Accepts Obfuscated Methods, but they must be under the Obfuscated Name.",
                "Many Client Side Only modifications, on the same class, may be inefficient.",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.client_side_methods")
        @Config.RequiresMcRestart
        public String[] clientSideMethods = new String[0];

        @Config.Comment({ "List of Fields to be server side only, acting as @SideOnly(Side.SERVER).",
                "DOES NOT WORK WITH CLASSES FROM MINECRAFT OR FORGE!",
                "Does not work with classes loaded before Nomi Labs' Static Init!",
                "Do not change unless you know what you are doing!",
                "Helps with fixing GrS errors Client Side.",
                "Format: <CLASS>@<FIELD>",
                "Example: gregtech/api/recipes/recipeproperties/TemperatureProperty@KEY",
                "Accepts Obfuscated Fields, but they must be under the Obfuscated Name.",
                "Many Server Side Only modifications, on the same class, may be inefficient.",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.server_side_fields")
        @Config.RequiresMcRestart
        public String[] serverSideFields = new String[0];

        @Config.Comment({ "List of Methods to be server side only, acting as @SideOnly(Side.SERVER).",
                "DOES NOT WORK WITH CLASSES FROM MINECRAFT OR FORGE!",
                "Does not work with classes loaded before Nomi Labs' Static Init!",
                "Do not change unless you know what you are doing!",
                "Helps with fixing GrS errors Client Side.",
                "Format: <CLASS>@<METHOD>@<DESC>",
                "Example: gregtech/api/recipes/recipeproperties/TemperatureProperty@drawInfo@(Lnet/minecraft/client/Minecraft;IIILjava/lang/Object;)V",
                "Accepts Obfuscated Methods, but they must be under the Obfuscated Name.",
                "Many Server Side Only modifications, on the same class, may be inefficient.",
                "[default: ]" })
        @Config.LangKey("config.nomilabs.advanced.server_side_methods")
        @Config.RequiresMcRestart
        public String[] serverSideMethods = new String[0];

        @Config.Comment({ "How to Modify the Language Tab in Minecraft Options.",
                "LABS or NOMI adds buttons and text for lanugage pack download.",
                "[default: LABS]" })
        @Config.LangKey("config.nomilabs.advanced.language_modify_option")
        public LanguageModifyOption languageModifyOption = LanguageModifyOption.LABS;

        @Config.Comment({ "Whether to enable Substitutions for the Server Properties MOTD.",
                "Substitutions: {version} for the Modpack Formatted Version (from 'nomilabs-version.cfg')",
                "Note: Only the First Substitution in the String is Replaced!",
                "[default: false]" })
        @Config.RequiresMcRestart
        @Config.LangKey("config.nomilabs.advanced.server_motd_substitutions")
        public boolean serverMotdSubstitutions = false;

        public static class WindowOverrides {

            @Config.Comment({ "Override for the Minecraft Window Title.",
                    "Leave Empty for the Default.",
                    "Substitutions: {version} for the Modpack Formatted Version (from 'nomilabs-version.cfg')",
                    "Note: Only the First Substitution in the String is Replaced!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.window.window_title")
            @Config.RequiresMcRestart
            public String windowTitleOverride = "";

            @Config.Comment({ "Override for the Minecraft Window Logo (16x).",
                    "This should be a Path, Relative to the Base Instance Folder.",
                    "16x, 32x and 256x must be set for logo overrides to apply!",
                    "Leave Empty for the Default.",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.window.window_logo_16x")
            @Config.RequiresMcRestart
            public String windowLogo16xOverride = "";

            @Config.Comment({ "Override for the Minecraft Window Logo (32x).",
                    "This should be a Path, Relative to the Base Instance Folder.",
                    "16x, 32x and 256x must be set for logo overrides to apply!",
                    "Leave Empty for the Default.",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.window.window_logo_32x")
            @Config.RequiresMcRestart
            public String windowLogo32xOverride = "";

            @Config.Comment({ "Override for the Minecraft Window Logo (256x).",
                    "This should be a Path, Relative to the Base Instance Folder.",
                    "16x, 32x and 256x must be set for logo overrides to apply!",
                    "Leave Empty for the Default.",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.window.window_logo_256x")
            @Config.RequiresMcRestart
            public String windowLogo256xOverride = "";
        }

        public enum LanguageModifyOption {
            NONE,
            LABS,
            NOMI
        }

        public static class FluidRegistry {

            @Config.Comment({ "List of Regex Patterns to be Default Fluids.",
                    "Fluids are picked based on a Hierarchy System.",
                    "For Example: If the config is set to ['gregtech:.*', 'advancedrocketry:.*'], and the candidates for Oxygen are 'gregtech:oxygen' and 'advancedrocketry:oxygen', the GregTech one will be picked.",
                    "This is only applied to conflicting fluids.",
                    "This can be very inefficient with lots of patterns and lots of conflicting fluids. Try to condense it into one pattern where possible!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.fluid_registry.default_fluids")
            @Config.RequiresMcRestart
            public String[] defaultFluids = new String[0];

            @Config.Comment({ "Whether to Log Conflicting Fluids, for use in setting default fluids correctly.",
                    "[default: false]" })
            @Config.LangKey("config.nomilabs.advanced.fluid_registry.log_conflicting_fluids")
            @Config.RequiresMcRestart
            public boolean logConflictingFluids = false;
        }

        public static class TierSettings {

            @Config.Comment({ "Better Questing QB IDs for Normal Mode Tiers.",
                    "These are checked in reverse, so later quests in the list, and in progression, have higher priority",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.normal_qb_ids")
            @Config.RequiresMcRestart
            public int[] normalModeQuestIds = new int[0];

            @Config.Comment({ "Non-Formatted Names for Normal Mode Tiers.",
                    "Each Tier must have the same index as the corresponding Quest ID!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.normal_slugs")
            @Config.RequiresMcRestart
            public String[] normalModeSlugs = new String[0];

            @Config.Comment({ "Formatted Names for Normal Mode Tiers.",
                    "Each Tier must have the same index as the corresponding Quest ID!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.normal_formatted")
            @Config.RequiresMcRestart
            public String[] normalModeFormatted = new String[0];

            @Config.Comment({ "Better Questing QB IDs for Expert Mode Tiers.",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.expert_qb_ids")
            @Config.RequiresMcRestart
            public int[] expertModeQuestIds = new int[0];

            @Config.Comment({ "Non-Formatted Names for Expert Mode Tiers.",
                    "Each Tier must have the same index as the corresponding Quest ID!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.expert_slugs")
            @Config.RequiresMcRestart
            public String[] expertModeSlugs = new String[0];

            @Config.Comment({ "Formatted Names for Expert Mode Tiers.",
                    "Each Tier must have the same index as the corresponding Quest ID!",
                    "[default: ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.expert_formatted")
            @Config.RequiresMcRestart
            public String[] expertModeFormatted = new String[0];

            @Config.Comment({ "Non-Formatted Name for Default Tier (Before Any Quests are Completed).",
                    "[default: pre-lv ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.default_slug")
            @Config.RequiresMcRestart
            public String defaultSlug = "pre-lv";

            @Config.Comment({ "Formatted Name for Default Tier (Before Any Quests are Completed).",
                    "[default: Before LV ]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.default_formatted")
            @Config.RequiresMcRestart
            public String defaultFormatted = "Before LV";

            @Config.Comment({ "Which lists to default to if the mode is not Normal or Expert.",
                    "[default: NORMAL]" })
            @Config.LangKey("config.nomilabs.advanced.tiers.default")
            @Config.RequiresMcRestart
            public DefaultModeType defaultMode = DefaultModeType.NORMAL;

            public enum DefaultModeType {
                NORMAL,
                EXPERT
            }
        }

        public static class ControlMenuTooltipSettings {

            @Config.Comment({ "Whether to show the keybind ID, if hovering over a keybind and pressing SHIFT.",
                    "The ID is used in default keybind overriding, as the ID specifies which keybind to override.",
                    "[default: true]" })
            @Config.LangKey("config.nomilabs.advanced.controls_tooltips.show_id")
            public boolean showID = true;

            @Config.Comment({ "Whether to show the keybind's Class, if hovering over a keybind and pressing CTRL.",
                    "If the class is not 'net.minecraft.client.settings.KeyBinding', default keybind overriding may not work for that keybind!",
                    "[default: false]" })
            @Config.LangKey("config.nomilabs.advanced.controls_tooltips.show_class")
            public boolean showClass = false;
        }
    }

    static {
        ConfigAnytime.register(LabsConfig.class);
    }
}
