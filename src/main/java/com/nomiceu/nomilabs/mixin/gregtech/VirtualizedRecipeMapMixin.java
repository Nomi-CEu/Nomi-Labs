package com.nomiceu.nomilabs.mixin.gregtech;

import static com.nomiceu.nomilabs.groovy.ChangeRecipeBuilderCollection.fromStream;

import java.util.ArrayList;
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
import com.nomiceu.nomilabs.groovy.ChangeRecipeBuilder;
import com.nomiceu.nomilabs.groovy.ChangeRecipeBuilderCollection;
import com.nomiceu.nomilabs.groovy.DummyChangeRecipeBuilder;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.integration.groovy.VirtualizedRecipeMap;

/**
 * Allows calling of new util functions (derivatives of find, remove, and change) via
 * `mods.gregtech.&lt;RECIPE_MAP_NAME&gt;` calls in Groovy.
 */
@Mixin(value = VirtualizedRecipeMap.class, remap = false)
@SuppressWarnings("unused")
public abstract class VirtualizedRecipeMapMixin {

    @Shadow
    @Final
    private RecipeMap<?> recipeMap;

    @Shadow
    public abstract String getName();

    @Shadow
    public abstract Recipe find(long voltage, List<ItemStack> items, List<FluidStack> fluids);

    @Unique
    @Nullable
    public Recipe labs$find(List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return labs$find((r) -> true, inputs, fluidInputs);
    }

    @Unique
    @Nullable
    public Recipe labs$find(GTRecipeCategory category, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return labs$find((r) -> Objects.equals(category, r.getRecipeCategory()), inputs, fluidInputs);
    }

    @SuppressWarnings("DuplicatedCode")
    @Unique
    public Recipe labs$find(Predicate<Recipe> condition, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        inputs = labs$validateList(inputs);
        fluidInputs = labs$validateList(fluidInputs);
        List<ItemStack> items = inputs.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());
        return recipeMap.find(items, fluids, condition);
    }

    @Unique
    public boolean labs$removeByInput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput((r) -> true, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    public boolean labs$removeByInput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                String.format("category: %s, items %s, fluids %s", category, items, fluids));
    }

    @Unique
    public boolean labs$removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput(condition, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    private boolean labs$removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                       String components) {
        Recipe recipe = labs$find(condition, items, fluids);
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
    public List<Recipe> labs$findByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$findByOutput(voltage, items, fluids, null, null);
    }

    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$findByOutput(items, fluids, null, null);
    }

    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(GTRecipeCategory category, List<ItemStack> items,
                                          List<FluidStack> fluids) {
        return labs$findByOutput(category, items, fluids, null, null);
    }

    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$findByOutput(condition, items, fluids, null, null);
    }

    @SuppressWarnings("DuplicatedCode")
    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                          List<ChancedItemOutput> chancedItems,
                                          List<ChancedFluidOutput> chancedFluids) {
        items = labs$validateList(items);
        fluids = labs$validateList(fluids);
        chancedItems = labs$validateList(chancedItems);
        chancedFluids = labs$validateList(chancedFluids);

        List<ItemStack> filteredItems = items.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> filteredFluids = fluids.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());
        return labs$getAccessibleRecipeMap().labs$findRecipeByOutput(voltage, filteredItems, filteredFluids,
                chancedItems,
                chancedFluids);
    }

    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                          List<ChancedItemOutput> chancedItems,
                                          List<ChancedFluidOutput> chancedFluids) {
        return labs$findByOutput((r) -> true, items, fluids, chancedItems, chancedFluids);
    }

    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(GTRecipeCategory category, List<ItemStack> items,
                                          List<FluidStack> fluids,
                                          List<ChancedItemOutput> chancedItems,
                                          List<ChancedFluidOutput> chancedFluids) {
        return labs$findByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids, chancedItems,
                chancedFluids);
    }

    @SuppressWarnings("DuplicatedCode")
    @Unique
    @Nullable
    public List<Recipe> labs$findByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                          List<ChancedItemOutput> chancedItems,
                                          List<ChancedFluidOutput> chancedFluids) {
        items = labs$validateList(items);
        fluids = labs$validateList(fluids);
        chancedItems = labs$validateList(chancedItems);
        chancedFluids = labs$validateList(chancedFluids);

        List<ItemStack> filteredItems = items.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> filteredFluids = fluids.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());
        return labs$getAccessibleRecipeMap().labs$findByOutput(filteredItems, filteredFluids, chancedItems,
                chancedFluids,
                condition);
    }

    @Unique
    public boolean labs$removeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByOutput(voltage, items, fluids, null, null);
    }

    @Unique
    public boolean labs$removeByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByOutput(items, fluids, null, null);
    }

    @Unique
    public boolean labs$removeByOutput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByOutput(category, items, fluids, null, null);
    }

    @Unique
    public boolean labs$removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByOutput(condition, items, fluids, null, null);
    }

    @Unique
    public boolean labs$removeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                       List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        List<Recipe> recipes = labs$findByOutput(voltage, items, fluids, chancedItems, chancedFluids);
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
    public boolean labs$removeByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                       List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput((r) -> true, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    public boolean labs$removeByOutput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids,
                                       List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                chancedItems, chancedFluids,
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        items, fluids, chancedItems, chancedFluids));
    }

    @Unique
    public boolean labs$removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                       List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput(condition, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    private boolean labs$removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                        List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids,
                                        String components) {
        List<Recipe> recipes = labs$findByOutput(condition, items, fluids, chancedItems, chancedFluids);
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
    public ChangeRecipeBuilder<?> labs$changeByInput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
        Recipe recipe = find(voltage, items, fluids);
        if (recipe == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error changing GregTech " + getName() + " recipe")
                        .add("could not find recipe for: voltage {}, items {}, fluids {}", voltage, items, fluids)
                        .error()
                        .post();
            }
            return new DummyChangeRecipeBuilder<>(recipeMap);
        }
        return new ChangeRecipeBuilder<>(recipe, recipeMap);
    }

    @Unique
    public ChangeRecipeBuilder<?> labs$changeByInput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$changeByInput((r) -> true, items, fluids,
                String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    public ChangeRecipeBuilder<?> labs$changeByInput(GTRecipeCategory category, List<ItemStack> items,
                                                     List<FluidStack> fluids) {
        return labs$changeByInput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                String.format("category: %s, items %s, fluids %s", category, items, fluids));
    }

    @Unique
    public ChangeRecipeBuilder<?> labs$changeByInput(Predicate<Recipe> condition, List<ItemStack> items,
                                                     List<FluidStack> fluids) {
        return labs$changeByInput(condition, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    private ChangeRecipeBuilder<?> labs$changeByInput(Predicate<Recipe> condition, List<ItemStack> items,
                                                      List<FluidStack> fluids,
                                                      String components) {
        Recipe recipe = labs$find(condition, items, fluids);
        if (recipe == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error changing GregTech " + getName() + " recipe")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return new DummyChangeRecipeBuilder<>(recipeMap);
        }
        return new ChangeRecipeBuilder<>(recipe, recipeMap);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(long voltage, List<ItemStack> items,
                                                                List<FluidStack> fluids) {
        return labs$changeByOutput(voltage, items, fluids, null, null);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$changeByOutput(items, fluids, null, null);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(GTRecipeCategory category, List<ItemStack> items,
                                                                List<FluidStack> fluids) {
        return labs$changeByOutput(category, items, fluids, null, null);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(Predicate<Recipe> condition, List<ItemStack> items,
                                                                List<FluidStack> fluids) {
        return labs$changeByOutput(condition, items, fluids, null, null);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(long voltage, List<ItemStack> items,
                                                                List<FluidStack> fluids,
                                                                List<ChancedItemOutput> chancedItems,
                                                                List<ChancedFluidOutput> chancedFluids) {
        List<Recipe> recipes = labs$findByOutput(voltage, items, fluids, chancedItems, chancedFluids);
        if (recipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error changing GregTech " + getName() + " recipe")
                        .add("could not find recipe for: voltage {}, items: {}, fluids: {}, chanced items: {}, chanced fluids: {}",
                                voltage, items, fluids, chancedItems, chancedFluids)
                        .error()
                        .post();
            }
            return new ChangeRecipeBuilderCollection<>();
        }
        return fromStream(recipes.stream().map((r) -> new ChangeRecipeBuilder<>(r, recipeMap)));
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                                                List<ChancedItemOutput> chancedItems,
                                                                List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput((r) -> true, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(GTRecipeCategory category, List<ItemStack> items,
                                                                List<FluidStack> fluids,
                                                                List<ChancedItemOutput> chancedItems,
                                                                List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                chancedItems, chancedFluids,
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        items, fluids, chancedItems, chancedFluids));
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeByOutput(Predicate<Recipe> condition, List<ItemStack> items,
                                                                List<FluidStack> fluids,
                                                                List<ChancedItemOutput> chancedItems,
                                                                List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput(condition, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    private ChangeRecipeBuilderCollection<?> labs$changeByOutput(Predicate<Recipe> condition, List<ItemStack> items,
                                                                 List<FluidStack> fluids,
                                                                 List<ChancedItemOutput> chancedItems,
                                                                 List<ChancedFluidOutput> chancedFluids,
                                                                 String components) {
        List<Recipe> recipes = labs$findByOutput(condition, items, fluids, chancedItems, chancedFluids);
        if (recipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error changing GregTech " + getName() + " recipes by output")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return new ChangeRecipeBuilderCollection<>();
        }
        return fromStream(recipes.stream().map((r) -> new ChangeRecipeBuilder<>(r, recipeMap)));
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeAllRecipes() {
        return labs$changeAllRecipes((r) -> true);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeAllRecipes(Predicate<Recipe> condition) {
        return fromStream(recipeMap.getRecipeList().stream()
                .filter(condition)
                .map((r) -> new ChangeRecipeBuilder<>(r, recipeMap)));
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeAllRecipes(GTRecipeCategory category) {
        return labs$changeAllRecipes(category, (r) -> true);
    }

    @Unique
    public ChangeRecipeBuilderCollection<?> labs$changeAllRecipes(GTRecipeCategory category,
                                                                  Predicate<Recipe> condition) {
        return fromStream(recipeMap.getRecipesByCategory()
                .getOrDefault(category, new ArrayList<>()).stream()
                .filter(condition)
                .map((r) -> new ChangeRecipeBuilder<>(r, recipeMap)));
    }

    @Unique
    private AccessibleRecipeMap labs$getAccessibleRecipeMap() {
        return (AccessibleRecipeMap) recipeMap;
    }

    @Unique
    @NotNull
    private <T> List<T> labs$validateList(@Nullable List<T> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list;
    }
}
