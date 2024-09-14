package com.nomiceu.nomilabs.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.Loader;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

public class LabsModeHelper {

    private static boolean checked = false;
    private static boolean postInitPassed = false;
    @Nullable
    private static String modePreCache = null;

    public static boolean isNormal() {
        return getMode().equals(LabsValues.NORMAL_MODE);
    }

    public static boolean isExpert() {
        return getMode().equals(LabsValues.EXPERT_MODE);
    }

    public static String getFormattedMode() {
        return StringUtils.capitalize(getMode());
    }

    /**
     * Get the Mode (at all times)
     */
    public static String getMode() {
        if (!postInitPassed) return getModePre();

        // Reflection:
        // ClassNotFound Exception Thrown if PMConfig not loaded
        // (Just in Case, Prevent Load in Pre)
        try {
            // Prevent loading Static Init, use MC Mod Loader
            var pmCfgClass = Class.forName("io.sommers.packmode.PMConfig", false,
                    Loader.getClassLoader());

            var getPackModeMethod = pmCfgClass.getDeclaredMethod("getPackMode");
            var getCfgMethod = pmCfgClass.getDeclaredMethod("getConfiguration");

            if (getCfgMethod.invoke(null) == null)
                // If no config
                return getModePre();

            // Get Mode Normally (Pack Mode can change in-game)
            String mode = (String) getPackModeMethod.invoke(null);
            if (!checked) check(mode);
            return mode;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            // PackMode not loaded
            // Throw, causes problems with cleanroom if we did this
            throw new RuntimeException(
                    "Tried to get mode when PMConfig class is not loaded! Fatal error, this should not happen! Report to Nomi-Labs devs!");
        }
    }

    /**
     * Get the mode, specifically pre-loading pack mode or pre-loading pack mode cfg.
     */
    private static String getModePre() {
        if (modePreCache != null) return modePreCache;

        File configDir = new File(Launch.minecraftHome, "config");
        File packModeCfgFile = new File(configDir, LabsValues.PACK_MODE_MODID + ".cfg");

        // Return Default if No Mode File
        if (!packModeCfgFile.exists()) return LabsValues.NORMAL_MODE;

        var configuration = new Configuration(packModeCfgFile);
        configuration.load();
        modePreCache = configuration.get("general", "packMode", LabsValues.NORMAL_MODE).getString();

        if (!checked) check(modePreCache);

        return modePreCache;
    }

    /**
     * Public Interface to Check
     */
    public static void check() {
        if (!checked) getMode(); // Will check, whether it uses getModePre or PMConfig.getPackMode
    }

    public static void onPostInit() {
        postInitPassed = true;
    }

    private static void check(String mode) {
        checked = true;
        if (LabsConfig.advanced.allowOtherPackModes) return;

        if (!(mode.equals(LabsValues.NORMAL_MODE) ||
                mode.equals(LabsValues.EXPERT_MODE))) {
            throw new IllegalArgumentException("[Nomi Labs]: Pack Mode must be either 'normal' or 'expert'." +
                    "If you are sure of what you are doing, you can allow other pack modes in the Nomi Labs Config, but beware: many mode specific behaviours will break.");
        }
    }
}
