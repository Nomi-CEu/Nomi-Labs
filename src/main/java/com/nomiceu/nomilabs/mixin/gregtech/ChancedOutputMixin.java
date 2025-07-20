package com.nomiceu.nomilabs.mixin.gregtech;

import org.cicirello.math.rand.Binomial;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleChancedOutput;

import gregtech.api.recipes.chance.output.ChancedOutput;

/**
 * Allows caching binomial generators.
 */
@Mixin(value = ChancedOutput.class, remap = false)
public class ChancedOutputMixin implements AccessibleChancedOutput {

    @Unique
    private Binomial labs$cachedGen;

    @Override
    public @Nullable Binomial labs$getCachedGen() {
        return labs$cachedGen;
    }

    @Override
    public void labs$setCachedGen(Binomial gen) {
        labs$cachedGen = gen;
    }
}
