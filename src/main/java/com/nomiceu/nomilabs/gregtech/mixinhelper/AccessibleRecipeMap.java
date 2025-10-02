package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;

public interface AccessibleRecipeMap {

    @Nullable
    List<Recipe> findByOutput(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                              @NotNull Collection<ChancedItemOutput> chancedItems,
                              @NotNull Collection<ChancedFluidOutput> chancedFluids,
                              @NotNull Predicate<Recipe> canHandle);

    @Nullable
    List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                    List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids);

    @Nullable
    List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                    List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids,
                                    boolean exactVoltage);
}
