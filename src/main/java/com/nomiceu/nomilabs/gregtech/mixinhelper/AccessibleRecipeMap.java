package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
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

    /**
     * Skips checking of custom scripting find filters. Only needed to be used in scripting environments.
     * Usually only helpful in defining find filters / removal handlers themselves.
     */
    @Nullable
    Recipe labs$rawFind(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                        @NotNull Predicate<Recipe> canHandle);

    /*
     * Actions called to filter searches or on recipe removal.
     * Only called when these are done through scripting (CT/GrS)
     */

    // Should be a quick check; called first out of any filter.
    void labs$setScriptFindFilter(Predicate<Recipe> recipeFilter, String filterMsg);

    void labs$setScriptRemoveAction(Consumer<Pair<ScriptType, Recipe>> onRecipeRemove);

    @Nullable
    Consumer<Pair<ScriptType, Recipe>> labs$getScriptRemoveAction();

    enum ScriptType {
        CT,
        GRS
    }
}
