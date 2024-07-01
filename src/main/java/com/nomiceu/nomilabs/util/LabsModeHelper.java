package com.nomiceu.nomilabs.util;

import java.io.File;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.StringUtils;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import io.sommers.packmode.PMConfig;

public class LabsModeHelper {

    private static boolean checked = false;

    public static boolean isNormal() {
        if (!checked)
            throw new IllegalStateException("Cannot access Pack Mode before Post Init or Labs Config Load!");
        return PMConfig.getPackMode().equals(LabsValues.NORMAL_MODE);
    }

    public static boolean isExpert() {
        if (!checked)
            throw new IllegalStateException("Cannot access Pack Mode before Post Init or Labs Config Load!");
        return PMConfig.getPackMode().equals(LabsValues.EXPERT_MODE);
    }

    /**
     * Used by Nomi-CEu Rich Presence.
     */
    @SuppressWarnings("unused")
    public static String getFormattedMode() {
        return getFormattedModeFromString(PMConfig.getPackMode());
    }

    /**
     * Used by Window Title Override. Doesn't Access PM Class Because of a ClassNorFoundException.
     */
    public static String getFormattedModePre() {
        File configDir = new File(Launch.minecraftHome, "config");
        File packModeCfgFile = new File(configDir, LabsValues.PACK_MODE_MODID + ".cfg");

        // Return Default if No Mode File
        if (!packModeCfgFile.exists()) return LabsValues.NORMAL_MODE;

        var configuration = new Configuration(packModeCfgFile);
        configuration.load();
        return getFormattedModeFromString(configuration.get("general", "packMode", LabsValues.NORMAL_MODE).getString());
    }

    private static String getFormattedModeFromString(String mode) {
        return StringUtils.capitalize(mode);
    }

    public static void check() {
        checked = true;
        if (LabsConfig.advanced.allowOtherPackModes) return;

        if (!(PMConfig.getPackMode().equals(LabsValues.NORMAL_MODE) ||
                PMConfig.getPackMode().equals(LabsValues.EXPERT_MODE))) {
            throw new IllegalArgumentException("[Nomi Labs]: Pack Mode must be either 'normal' or 'expert'." +
                    "If you are sure of what you are doing, you can allow other pack modes in the Nomi Labs Config, but beware: many mode specific behaviours will break.");
        }
    }
}
