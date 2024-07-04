package com.nomiceu.nomilabs.config;

import net.minecraftforge.common.config.Config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.nomiceu.nomilabs.LabsValues;

@SuppressWarnings({ "CanBeFinal", "unused" })
@Config(modid = LabsValues.LABS_MODID, name = LabsValues.LABS_MODID + "-version")
public class LabsVersionConfig {

    @Config.Comment({
            "Formatted Version of the Modpack. Currently just used as a Substitution in the Window Title Override.",
            "[default: v1.0.0]" })
    @Config.LangKey("config.nomilabs.version.formatted-version")
    @Config.RequiresMcRestart
    public static String formattedVersion = "v1.0.0";

    static {
        ConfigAnytime.register(LabsVersionConfig.class);
    }
}
