package com.nomiceu.nomilabs.config;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraftforge.common.config.Config;

@SuppressWarnings({"CanBeFinal", "unused"})
@Config(modid = LabsValues.LABS_MODID, category = "")
public class LabsConfig {
    @Config.Comment("Content Settings")
    @Config.LangKey("config.nomilabs.content")
    @Config.Name("content")
    public static Content content = new Content();

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

    public static class ModIntegration {
        @Config.Comment({"Whether to enable NuclearCraft Integration, which fixes its crash with GTCEu.",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.mod_integration.nuclearcraft")
        @Config.RequiresMcRestart
        public boolean enableNuclearCraftIntegration = true;

        @Config.Comment({"Whether to enable Extra Utilities 2 Integration, which removes frequencies.",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.mod_integration.xu2")
        @Config.RequiresMcRestart
        public boolean enableExtraUtils2Integration = true;

        @Config.Comment({"Whether to enable TOP Integration, which adds some messages to the TOP panel when hovering over certain blocks.",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.mod_integration.top")
        @Config.RequiresMcRestart
        public boolean enableTOPIntegration = true;

        @Config.Comment({"Whether to enable Ender Storage Integration, which allows data fixes to remap Ender Storage Chests' Contents.",
                "If this is in a Nomi-CEu Environment, make sure this stays at true, or your world may break, and items be lost!",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.mod_integration.ender_storage")
        @Config.RequiresMcRestart
        public boolean enableEnderStorageIntegration = true;

        @Config.Comment("Draconic Evolution Integration Settings")
        @Config.LangKey("config.nomilabs.mod_integration.draconicevolution")
        @Config.Name("draconic evolution integration")
        public final DraconicEvolutionIntegration draconicEvolutionIntegration = new DraconicEvolutionIntegration();

        public static class DraconicEvolutionIntegration {
            @Config.Comment({"Whether to enable Draconic Evolution Integration, which adds many features, such as:",
                    "Allowing GregTech Draconium and Awakened Draconium in the reactor and energy core.",
                    "Modifying Energy Core Structure with improvements, such as allowing blocks surrounding the structure.",
                    "Adding a destructor to the energy core.",
                    "Allow changing the speed of the builder.",
                    "If this option is disabled, then energy cores made whilst this was enabled may break!",
                    "[default: true]"})
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.enable")
            @Config.RequiresMcRestart
            public boolean enableDraconicEvolutionIntegration = true;

            @Config.Comment({"The speed of the Builder, in blocks per tick.",
                    "Set this to 0 to have the builder be instant.",
                    "[default: 1]"})
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.auto_builder_speed")
            @Config.RangeInt(min = 0)
            public int autoBuilderSpeed = 1;

            @Config.Comment({"The speed of the Destructor, in blocks per tick.",
                    "Set this to 0 to have the destructor be instant.",
                    "[default: 1]"})
            @Config.LangKey("config.nomilabs.mod_integration.draconicevolution.auto_destructor_speed")
            @Config.RangeInt(min = 0)
            public int autoDestructorSpeed = 1;
        }
    }

    public static class Advanced {
        @Config.Comment({"Whether to allow other pack modes, other than 'normal' and 'expert'.",
                "If this is set to false, the game will crash if other modes are found.",
                "Only set this to false if you are sure of what you are doing.",
                "Beware: many mode specific behaviours will break if other pack modes are used!",
                "[default: false]"})
        @Config.LangKey("config.nomilabs.advanced.allow_other_modes")
        @Config.RequiresMcRestart
        public boolean allowOtherPackModes = false;

        @Config.Comment({"Whether to enable data fixes.",
                "This is used for Nomi-CEu, for players coming from before core-mod.",
                "If this mod is being used in other scenarios, leave this at false, as this may break worlds!",
                "If this is in a Nomi-CEu environment, make sure it is true, and do not change it, or items/blocks may be lost!",
                "This may be used in the future as well, and your world will not be fixed if it is up-to-date.",
                "[default: false]"})
        @Config.LangKey("config.nomilabs.advanced.enable_data_fixes")
        @Config.RequiresWorldRestart
        public boolean enableDataFixes = false;
    }
}
