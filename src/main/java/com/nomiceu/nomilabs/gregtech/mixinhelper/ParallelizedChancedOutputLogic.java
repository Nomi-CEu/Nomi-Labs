package com.nomiceu.nomilabs.gregtech.mixinhelper;

import static com.nomiceu.nomilabs.util.LabsConstants.MAX_CHANCE;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.cicirello.math.rand.Binomial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.config.LabsConfig;

import gregtech.api.GTValues;
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
                                                                  BiFunction<T, Integer, T> copyWithAmount) {
            ImmutableList.Builder<T> result = null;

            for (T entry : chancedEntries) {
                int chance = getChance(entry, boostFunction, baseTier, machineTier);

                // If our chance is 0 or 10_000, shortcircuit.
                if (chance <= 0) continue;
                if (chance >= MAX_CHANCE) {
                    if (result == null) result = ImmutableList.builder();
                    result.add(copyWithAmount.apply(entry, parallels));
                }

                // Roll chances, using algorithm based on numParallels and config
                int amt;
                if (parallels < LabsConfig.advanced.binomialThreshold)
                    amt = rollXSTR(chance, parallels);
                else
                    amt = rollBinomial(entry, chance, parallels);

                if (amt > 0) {
                    if (result == null) result = ImmutableList.builder();
                    result.add(copyWithAmount.apply(entry, amt));
                }
            }

            return result == null ? null : result.build();
        }

        @Override
        public @NotNull String getTranslationKey() {
            return ChancedOutputLogic.OR.getTranslationKey();
        }

        private int rollXSTR(int chance, int parallels) {
            int successes = 0;
            for (int i = 0; i < parallels; i++) {
                if (GTValues.RNG.nextInt(MAX_CHANCE) <= chance) successes++;
            }
            return successes;
        }

        private int rollBinomial(ChancedOutput<?> entry, int chance, int parallels) {
            // Try to use cache from entry
            Binomial instance = ((AccessibleChancedOutput) entry).labs$getCachedGen();

            if (instance == null || !instance.consistentWith(parallels, chance)) {
                instance = Binomial.getOrCreateInstance(parallels, chance);
                ((AccessibleChancedOutput) entry).labs$setCachedGen(instance);
            }

            return instance.next(GTValues.RNG);
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
                                                          BiFunction<T, Integer, T> copyWithAmount);

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
