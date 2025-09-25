package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.nomiceu.nomilabs.util.math.BinomialMethod;

import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutput;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ParallelizedChancedOutputList<I, T extends ChancedOutput<I>> extends ChancedOutputList<I, T> {

    private final ParallelizedChancedOutputLogic logic;
    private final BiFunction<T, Integer, T> copyWithMultipliedAmount;
    private final int parallels;

    public ParallelizedChancedOutputList(@NotNull ParallelizedChancedOutputLogic logic, @NotNull List<T> entries,
                                         int parallels, BiFunction<T, Integer, T> copyWithMultipliedAmount,
                                         Map<Integer, WeakReference<BinomialMethod>> cache) {
        super(logic, entries);
        this.logic = logic;
        this.parallels = parallels;
        this.copyWithMultipliedAmount = copyWithMultipliedAmount;

        ((CachedChancedOutputList) this).labs$setExistingCache(cache);
    }

    @Override
    public @Nullable @Unmodifiable List<T> roll(@NotNull ChanceBoostFunction boostFunction, int baseTier,
                                                int machineTier) {
        return logic.roll(getChancedEntries(), boostFunction, baseTier, machineTier, parallels,
                copyWithMultipliedAmount, ((CachedChancedOutputList) this).labs$cache());
    }

    public List<T> getTrueValues() {
        List<T> result = new ObjectArrayList<>();

        for (var entry : getChancedEntries()) {
            result.add(copyWithMultipliedAmount.apply(entry, parallels));
        }

        return result;
    }
}
