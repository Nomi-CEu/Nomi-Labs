package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface AccessibleRecipeMap {
    List<Recipe> findByOutput(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                              @NotNull Collection<ChancedItemOutput> chancedItems, @NotNull Collection<ChancedFluidOutput> chancedFluids,
                              @NotNull Predicate<Recipe> canHandle);

    List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                    List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids);

    List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                    List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids, boolean exactVoltage);
}
