package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.integration.groovy.VirtualizedRecipeMap;

/**
 * Allows calling of `removeByOutput` via `mods.gregtech.&lt;RECIPE_MAP_NAME&gt;` calls in Groovy.
 */
@Mixin(value = VirtualizedRecipeMap.class, remap = false)
@SuppressWarnings("unused")
public abstract class VirtualizedRecipeMapMixin {

    @Shadow
    @Final
    private RecipeMap<?> recipeMap;

    @Shadow
    public abstract String getName();

    @Unique
    @Nullable
    public Recipe find(List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return find((r) -> true, inputs, fluidInputs);
    }

    @Unique
    @Nullable
    public Recipe find(GTRecipeCategory category, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return find((r) -> Objects.equals(category, r.getRecipeCategory()), inputs, fluidInputs);
    }

    @SuppressWarnings("DuplicatedCode")
    @Unique
    public Recipe find(Predicate<Recipe> condition, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        inputs = validateList(inputs);
        fluidInputs = validateList(fluidInputs);
        List<ItemStack> items = inputs.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());
        return recipeMap.find(items, fluids, condition);
    }

    @Unique
    public boolean removeByInput(List<ItemStack> items, List<FluidStack> fluids) {
        return removeByInput((r) -> true, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    public boolean removeByInput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids) {
        return removeByInput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                String.format("category: %s, items %s, fluids %s", category, items, fluids));
    }

    @Unique
    public boolean removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return removeByInput(condition, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    private boolean removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                  String components) {
        Recipe recipe = find(condition, items, fluids);
        if (recipe == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error removing GregTech " + getName() + " recipe")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return false;
        }
        recipeMap.removeRecipe(recipe);
        return true;
    }

    @Unique
    @Nullable
    public List<Recipe> findByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        inputs = validateList(inputs);
        fluidInputs = validateList(fluidInputs);
        chancedItems = validateList(chancedItems);
        chancedFluids = validateList(chancedFluids);
        return getAccessibleRecipeMap().findRecipeByOutput(voltage, inputs, fluidInputs, chancedItems, chancedFluids);
    }

    @Unique
    @Nullable
    public List<Recipe> findByOutput(List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return findByOutput((r) -> true, inputs, fluidInputs, chancedItems, chancedFluids);
    }

    @Unique
    @Nullable
    public List<Recipe> findRecipeByOutput(GTRecipeCategory category, List<ItemStack> inputs,
                                           List<FluidStack> fluidInputs,
                                           List<ChancedItemOutput> chancedItems,
                                           List<ChancedFluidOutput> chancedFluids) {
        return findByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), inputs, fluidInputs, chancedItems,
                chancedFluids);
    }

    @Unique
    public List<Recipe> findByOutput(Predicate<Recipe> condition, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        inputs = validateList(inputs);
        fluidInputs = validateList(fluidInputs);
        chancedItems = validateList(chancedItems);
        chancedFluids = validateList(chancedFluids);
        List<ItemStack> items = inputs.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());
        return getAccessibleRecipeMap().findByOutput(items, fluids, chancedItems, chancedFluids, condition);
    }

    @Unique
    public boolean removeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        List<Recipe> recipes = findByOutput(voltage, items, fluids, chancedItems, chancedFluids);
        if (recipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error removing GregTech " + getName() + " recipe")
                        .add("could not find recipe for: voltage {}, items: {}, fluids: {}, chanced items: {}, chanced fluids: {}",
                                voltage, items, fluids, chancedItems, chancedFluids)
                        .error()
                        .post();
            }
            return false;
        }
        for (var recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
        return true;
    }

    @Unique
    public boolean removeByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return removeByOutput((r) -> true, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    public boolean removeByOutput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return removeByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                chancedItems, chancedFluids,
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        items, fluids, chancedItems, chancedFluids));
    }

    @Unique
    public boolean removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return removeByOutput(condition, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    private boolean removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                   List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids,
                                   String components) {
        List<Recipe> recipes = findByOutput(condition, items, fluids, chancedItems, chancedFluids);
        if (recipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error removing GregTech " + getName() + " recipes by output")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return false;
        }
        for (var recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
        return true;
    }

    @Unique
    private AccessibleRecipeMap getAccessibleRecipeMap() {
        return (AccessibleRecipeMap) recipeMap;
    }

    @Unique
    @NotNull
    private <T> List<T> validateList(@Nullable List<T> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list;
    }
}
