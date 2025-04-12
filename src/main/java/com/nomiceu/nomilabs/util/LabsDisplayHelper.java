package com.nomiceu.nomilabs.util;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.config.LabsVersionConfig;

public class LabsDisplayHelper {

    public static String getWindowTitle() {
        return LabsConfig.advanced.windowOverrides.windowTitleOverride
                .replace("{version}", LabsVersionConfig.formattedVersion)
                .replace("{mode}", LabsModeHelper.getFormattedMode());
    }

    public static String formatImagePath(String path) {
        return path.replace("{mode}", LabsModeHelper.getMode());
    }
}
