package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.util.math.BinomialMethod;

public interface CachedChancedOutputList {

    @NotNull
    Map<Integer, WeakReference<BinomialMethod>> labs$cache();

    void labs$setExistingCache(@Nullable Map<Integer, WeakReference<BinomialMethod>> cached);
}
