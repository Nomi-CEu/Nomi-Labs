package com.nomiceu.nomilabs.mixinhelper;

import net.minecraft.world.EnumDifficulty;

public interface DifficultySettableServer {

    void labs$setDifficultyForAllWorldsAndSave(EnumDifficulty difficulty);
}
