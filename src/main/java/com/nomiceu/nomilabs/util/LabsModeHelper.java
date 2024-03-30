package com.nomiceu.nomilabs.util;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import io.sommers.packmode.PMConfig;
import org.apache.commons.lang3.StringUtils;

public class LabsModeHelper {
    private static boolean checked = false;

    public static boolean isNormal() {
        if (!checked)
            throw new IllegalStateException("Cannot access Pack Mode before Post Init!");
        return PMConfig.getPackMode().equals(LabsValues.NORMAL_MODE);
    }

    public static boolean isExpert() {
        if (!checked)
            throw new IllegalStateException("Cannot access Pack Mode before Post Init!");
        return PMConfig.getPackMode().equals(LabsValues.EXPERT_MODE);
    }

    /**
     * Used by Nomi-CEu Rich Presence.
     */
    @SuppressWarnings("unused")
    public static String getFormattedMode() {
        return StringUtils.capitalize(PMConfig.getPackMode());
    }

    public static void check() {
        checked = true;
        if (LabsConfig.advanced.allowOtherPackModes) return;

        if (!(PMConfig.getPackMode().equals(LabsValues.NORMAL_MODE) || PMConfig.getPackMode().equals(LabsValues.EXPERT_MODE))) {
            throw new IllegalArgumentException("[Nomi Labs]: Pack Mode must be either 'normal' or 'expert'." +
                    "If you are sure of what you are doing, you can allow other pack modes in the Nomi Labs Config, but beware: many mode specific behaviours will break.");
        }
    }
}
