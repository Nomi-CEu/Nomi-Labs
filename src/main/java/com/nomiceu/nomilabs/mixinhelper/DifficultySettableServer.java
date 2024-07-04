package com.nomiceu.nomilabs.mixinhelper;

import net.minecraft.world.EnumDifficulty;

public interface DifficultySettableServer {

    void setDifficultyForAllWorldsAndSave(EnumDifficulty difficulty);
}
