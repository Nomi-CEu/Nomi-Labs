package com.nomiceu.nomilabs.gregtech.mixinhelper;

import org.jetbrains.annotations.Nullable;

public interface FlaggableRecipe {

    void labs$setFlag(String flag);

    @Nullable
    String labs$getFlag();
}
