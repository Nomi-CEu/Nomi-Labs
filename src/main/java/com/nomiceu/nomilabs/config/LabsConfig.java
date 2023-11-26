package com.nomiceu.nomilabs.config;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraftforge.common.config.Config;

@Config(modid = LabsValues.LABS_MODID, category = "")
public class LabsConfig {
    @Config.Comment("Custom Content Settings")
    @Config.LangKey("config.nomilabs.custom_content")
    @Config.Name("custom content")
    public static CustomContent customContent = new CustomContent();

    @Config.Comment("Mod Integration Settings")
    @Config.LangKey("config.nomilabs.mod_integration")
    @Config.Name("mod integration")
    public static ModIntegration modIntegration = new ModIntegration();

    public static class CustomContent {
        @Config.Comment({"Whether to enable Custom Blocks, Items and Fluids.",
                "They will not have recipes.",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.custom_content.enable_custom_content")
        @Config.RequiresMcRestart
        public boolean enableCustomContent = true;

        @Config.Comment({"Whether to enable GT Custom Content.", "Includes Custom Multiblocks, Materials, Material Changes and Meta Blocks.",
                "They will not have recipes, but multiblocks will have recipeMaps.",
                "In Beta.",
                "[default: false]"})
        @Config.LangKey("config.nomilabs.custom_content.enable_gt_custom_content")
        @Config.RequiresMcRestart
        public boolean enableGTCustomContent = false;
    }

    public static class ModIntegration {
        @Config.Comment({"Whether to enable NuclearCraft Integration, which fixes its crash with GTCEu.",
                "[default: true]"})
        @Config.LangKey("config.nomilabs.mod_integration.nuclearcraft")
        @Config.RequiresMcRestart
        public boolean enableNuclearCraftIntegration = true;

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
}
