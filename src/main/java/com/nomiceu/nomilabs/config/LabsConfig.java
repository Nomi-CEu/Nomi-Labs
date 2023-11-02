package com.nomiceu.nomilabs.config;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraftforge.common.config.Config;

@Config(modid = LabsValues.LABS_MODID)
public class LabsConfig {
    @Config.Comment({"Whether to enable Custom Blocks, Items and Fluids.", "They will not have recipes.", "Default: True"})
    @Config.RequiresMcRestart
    public static boolean enableCustomContent = true;

    @Config.Comment({"Whether to enable GT Custom Content.", "Includes Custom Multiblocks, Materials, Material Changes and Meta Blocks.", "They will not have recipes, but multiblocks will have recipeMaps.", "In Beta.", "Default: False"})
    @Config.RequiresMcRestart
    public static boolean enableGTCustomContent = false;
}
