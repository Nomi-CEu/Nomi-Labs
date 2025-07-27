package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.util.math.BinomialGenerator;
import com.nomiceu.nomilabs.util.math.BinomialMethod;

import gregtech.api.recipes.chance.boost.BoostableChanceEntry;
import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutput;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;

public interface ParallelizedChancedOutputLogic extends ChancedOutputLogic {

    ParallelizedChancedOutputLogic OR = new ParallelizedChancedOutputLogic() {

        @Override
        public @Nullable @Unmodifiable <I,
                T extends ChancedOutput<I>> List<@NotNull T> roll(@NotNull @Unmodifiable List<@NotNull T> chancedEntries,
                                                                  @NotNull ChanceBoostFunction boostFunction,
                                                                  int baseTier, int machineTier) {
            // This is the vanilla GT version. If this function has been called, something has gone wrong
            throw new IllegalStateException(
                    "ParallelizedChancedOutputLogic (OR) has had its vanilla GT function called! Please report this to Nomi-CEu/Nomi Labs devs!");
        }

        @Override
        public @Nullable @Unmodifiable <I,
                T extends ChancedOutput<I>> List<@NotNull T> roll(@NotNull @Unmodifiable List<@NotNull T> chancedEntries,
                                                                  @NotNull ChanceBoostFunction boostFunction,
                                                                  int baseTier, int machineTier, int parallels,
                                                                  BiFunction<T, Integer, T> copyWithAmount,
                                                                  Map<Integer, WeakReference<BinomialMethod>> cache) {
            ImmutableList.Builder<T> result = null;

            for (int i = 0; i < chancedEntries.size(); i++) {
                int chance = getChance(chancedEntries.get(i), boostFunction, baseTier, machineTier);
                int amt = rollBinomial(chance, parallels, cache, i);

                if (amt > 0) {
                    if (result == null) result = ImmutableList.builder();
                    result.add(copyWithAmount.apply(chancedEntries.get(i), amt));
                }
            }

            return result == null ? null : result.build();
        }

        @Override
        public @NotNull String getTranslationKey() {
            return ChancedOutputLogic.OR.getTranslationKey();
        }

        private int rollBinomial(int chance, int parallels, Map<Integer, WeakReference<BinomialMethod>> cache,
                                 int idx) {
            return BinomialGenerator.generate(parallels, chance,
                    cache.get(idx), (mtd) -> cache.put(idx, new WeakReference<>(mtd)));
        }
    };

    // For easy future expansion, store a map of normal chanced logic to new parallel chanced logic
    Map<ChancedOutputLogic, ParallelizedChancedOutputLogic> normalToParallelized = ImmutableMap
            .of(ChancedOutputLogic.OR, OR);

    @Nullable
    @Unmodifiable
    <I, T extends ChancedOutput<I>> List<@NotNull T> roll(@NotNull @Unmodifiable List<@NotNull T> chancedEntries,
                                                          @NotNull ChanceBoostFunction boostFunction, int baseTier,
                                                          int machineTier, int parallels,
                                                          BiFunction<T, Integer, T> copyWithAmount,
                                                          Map<Integer, WeakReference<BinomialMethod>> cache);

    /**
     * From {@link ChancedOutputLogic#getChance(ChancedOutput, ChanceBoostFunction, int, int)}, where it is
     * package-private.
     * 
     * @param entry         the entry to get the complete chance for
     * @param boostFunction the function boosting the entry's chance
     * @param baseTier      the base tier of the recipe
     * @param machineTier   the tier the recipe is run at
     * @return the total chance for the entry
     */
    static int getChance(@NotNull ChancedOutput<?> entry, @NotNull ChanceBoostFunction boostFunction, int baseTier,
                         int machineTier) {
        if (entry instanceof BoostableChanceEntry<?>boostableChanceEntry) {
            return boostFunction.getBoostedChance(boostableChanceEntry, baseTier, machineTier);
        }
        return entry.getChance();
    }
}
