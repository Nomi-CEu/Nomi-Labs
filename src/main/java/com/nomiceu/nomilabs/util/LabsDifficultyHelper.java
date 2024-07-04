package com.nomiceu.nomilabs.util;

import net.minecraft.world.EnumDifficulty;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.config.LabsConfig;

public class LabsDifficultyHelper {

    /**
     * Get the Difficulty to lock worlds in. Returns null if none.
     */
    @Nullable
    public static EnumDifficulty getLockedDifficulty() {
        if (LabsModeHelper.isNormal()) {
            if (LabsConfig.advanced.difficultyOverrides.overrideDifficultyNormal)
                return LabsConfig.advanced.difficultyOverrides.difficultyNormal;
            else return null;
        }
        if (LabsModeHelper.isExpert()) {
            if (LabsConfig.advanced.difficultyOverrides.overrideDifficultyExpert)
                return LabsConfig.advanced.difficultyOverrides.difficultyExpert;
            else return null;
        }
        return null;
    }
}
