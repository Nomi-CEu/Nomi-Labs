package com.nomiceu.nomilabs.mixin.gregtech;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.CachedChancedOutputList;
import com.nomiceu.nomilabs.util.math.BinomialMethod;

import gregtech.api.recipes.chance.output.ChancedOutputList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@Mixin(value = ChancedOutputList.class, remap = false)
public class ChancedOutputListMixin implements CachedChancedOutputList {

    @Unique
    private Map<Integer, WeakReference<BinomialMethod>> labs$cache = new Int2ObjectOpenHashMap<>();

    @Override
    public @NotNull Map<Integer, WeakReference<BinomialMethod>> labs$cache() {
        return labs$cache;
    }

    @Override
    public void labs$setExistingCache(Map<Integer, WeakReference<BinomialMethod>> cached) {
        if (cached != null) labs$cache = cached;
    }
}
