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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public Recipe find(List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return find((r) -> true, inputs, fluidInputs);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public Recipe find(GTRecipeCategory category, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        return find((r) -> Objects.equals(category, r.getRecipeCategory()), inputs, fluidInputs);
    }

    @SuppressWarnings({ "DuplicatedCode", "AddedMixinMembersNamePattern" })
    @Unique
    public Recipe find(Predicate<Recipe> condition, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        inputs = labs$validateList(inputs);
        fluidInputs = labs$validateList(fluidInputs);
        List<ItemStack> items = inputs.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());

        return recipeMap.find(items, fluids, condition);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByInput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput((r) -> true, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByInput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                String.format("category: %s, items %s, fluids %s", category, items, fluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return labs$removeByInput(condition, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    private boolean labs$removeByInput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
        return findByOutput(voltage, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return findByOutput(items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(GTRecipeCategory category, List<ItemStack> items,
                                     List<FluidStack> fluids) {
        return findByOutput(category, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return findByOutput(condition, items, fluids, null, null);
    }

    @SuppressWarnings({ "DuplicatedCode", "AddedMixinMembersNamePattern" })
    @Unique
    @Nullable
    public List<Recipe> findByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return findByOutput((r) -> r.getEUt() == voltage, items, fluids, chancedItems, chancedFluids);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return findByOutput((r) -> true, items, fluids, chancedItems, chancedFluids);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    public List<Recipe> findByOutput(GTRecipeCategory category, List<ItemStack> items,
                                     List<FluidStack> fluids,
                                     List<ChancedItemOutput> chancedItems,
                                     List<ChancedFluidOutput> chancedFluids) {
        return findByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids, chancedItems,
                chancedFluids);
    }

    @SuppressWarnings({ "DuplicatedCode", "AddedMixinMembersNamePattern" })
    @Unique
    @Nullable
    public List<Recipe> findByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                     List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        items = labs$validateList(items);
        fluids = labs$validateList(fluids);
        chancedItems = labs$validateList(chancedItems);
        chancedFluids = labs$validateList(chancedFluids);

        List<ItemStack> filteredItems = items.stream().filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> filteredFluids = fluids.stream().filter((f) -> f != null && f.amount != 0)
                .collect(Collectors.toList());

        return labs$getAccessibleRecipeMap().findByOutput(filteredItems, filteredFluids, chancedItems, chancedFluids,
                condition);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
        return removeByOutput(voltage, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return removeByOutput(items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids) {
        return removeByOutput(category, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids) {
        return removeByOutput(condition, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput((r) -> r.getEUt() == voltage, items, fluids, chancedItems, chancedFluids,
                String.format("voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", voltage,
                        items, fluids,
                        chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput((r) -> true, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(GTRecipeCategory category, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                chancedItems, chancedFluids,
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        items, fluids, chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
                                  List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids) {
        return labs$removeByOutput(condition, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @Unique
    private boolean labs$removeByOutput(Predicate<Recipe> condition, List<ItemStack> items, List<FluidStack> fluids,
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilder<?> changeByInput(long voltage, List<ItemStack> items, List<FluidStack> fluids) {
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilder<?> changeByInput(List<ItemStack> items, List<FluidStack> fluids) {
        return labs$changeByInput((r) -> true, items, fluids,
                String.format("items: %s, fluids: %s", items, fluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilder<?> changeByInput(GTRecipeCategory category, List<ItemStack> items,
                                                List<FluidStack> fluids) {
        return labs$changeByInput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                String.format("category: %s, items %s, fluids %s", category, items, fluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilder<?> changeByInput(Predicate<Recipe> condition, List<ItemStack> items,
                                                List<FluidStack> fluids) {
        return labs$changeByInput(condition, items, fluids, String.format("items: %s, fluids: %s", items, fluids));
    }

    @Unique
    private ChangeRecipeBuilder<?> labs$changeByInput(Predicate<Recipe> condition, List<ItemStack> items,
                                                      List<FluidStack> fluids,
                                                      String components) {
        Recipe recipe = find(condition, items, fluids);
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(long voltage, List<ItemStack> items,
                                                           List<FluidStack> fluids) {
        return changeByOutput(voltage, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(List<ItemStack> items, List<FluidStack> fluids) {
        return changeByOutput(items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(GTRecipeCategory category, List<ItemStack> items,
                                                           List<FluidStack> fluids) {
        return changeByOutput(category, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(Predicate<Recipe> condition, List<ItemStack> items,
                                                           List<FluidStack> fluids) {
        return changeByOutput(condition, items, fluids, null, null);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(long voltage, List<ItemStack> items, List<FluidStack> fluids,
                                                           List<ChancedItemOutput> chancedItems,
                                                           List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput((r) -> r.getEUt() == voltage, items, fluids, chancedItems, chancedFluids,
                String.format("voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", voltage,
                        items, fluids,
                        chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(List<ItemStack> items, List<FluidStack> fluids,
                                                           List<ChancedItemOutput> chancedItems,
                                                           List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput((r) -> true, items, fluids, chancedItems, chancedFluids,
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", items, fluids,
                        chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(GTRecipeCategory category, List<ItemStack> items,
                                                           List<FluidStack> fluids,
                                                           List<ChancedItemOutput> chancedItems,
                                                           List<ChancedFluidOutput> chancedFluids) {
        return labs$changeByOutput((r) -> Objects.equals(r.getRecipeCategory(), category), items, fluids,
                chancedItems, chancedFluids,
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        items, fluids, chancedItems, chancedFluids));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeByOutput(Predicate<Recipe> condition, List<ItemStack> items,
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
        List<Recipe> recipes = findByOutput(condition, items, fluids, chancedItems, chancedFluids);
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeAllRecipes() {
        return changeAllRecipes((r) -> true);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeAllRecipes(Predicate<Recipe> condition) {
        return fromStream(recipeMap.getRecipeList().stream()
                .filter(condition)
                .map((r) -> new ChangeRecipeBuilder<>(r, recipeMap)));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeAllRecipes(GTRecipeCategory category) {
        return changeAllRecipes(category, (r) -> true);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ChangeRecipeBuilderCollection<?> changeAllRecipes(GTRecipeCategory category, Predicate<Recipe> condition) {
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
