package com.nomiceu.nomilabs.gregtech.mixinhelper;

import org.cicirello.math.rand.Binomial;
import org.jetbrains.annotations.Nullable;

public interface AccessibleChancedOutput {

    @Nullable
    Binomial labs$getCachedGen();

    void labs$setCachedGen(Binomial gen);
}
