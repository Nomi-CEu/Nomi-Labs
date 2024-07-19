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

    @Config.Comment({
            "Manual Fix Version. Increment this to have Data Fixers be POTENTIALLY applied.",
            "Note that if a chunk or inventory is loaded on the same fix version multiple times, it will only be fixed the first time.",
            "Note that no warning will be displayed before game load.",
            "The data fix version itself will be this config + the Labs internal fix version.",
            "[default: 0]" })
    @Config.LangKey("config.nomilabs.version.manual-fix-version")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public static int manualFixVersion = 0;

    static {
        ConfigAnytime.register(LabsVersionConfig.class);
    }
}
