package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.map.AbstractMapIngredient;
import gregtech.api.recipes.map.MapFluidIngredient;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

public class RecipeMapLogic {

    private static boolean outputBranchesCleared = false;

    private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> outputRoot = new WeakHashMap<>();

    private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> fluidOutputRoot = new WeakHashMap<>();

    private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> chancedOutputRoot = new WeakHashMap<>();

    private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> chancedFluidOutputRoot = new WeakHashMap<>();

    /**
     * Used to clear all maps, to save memory. Done at end of game load, if FAST_DISCARDED_TREE or DISCARDED_TREE is
     * enabled.
     */
    public static void clearAll() {
        // If Mode is DISCARDED_TREE or FAST_DISCARDED_TREE or LINEAR_SEARCH (so that in linear, cached outputs are
        // cleared)
        if (LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.FAST_DISCARDED_TREE ||
                LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                        LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.DISCARDED_TREE ||
                LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                        LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.LINEAR_SEARCH) {
            NomiLabs.LOGGER.info("Clearing Output Branches and Output Maps...");
            outputBranchesCleared = true;
            OutputBranch.clearAll();
            outputRoot.clear();
            fluidOutputRoot.clear();
            chancedOutputRoot.clear();
            chancedFluidOutputRoot.clear();
        }
    }

    public static void add(@NotNull Recipe recipe, @NotNull OutputBranch branch) {
        if (LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.LINEAR_SEARCH || outputBranchesCleared)
            return;
        var list = getOutputFromRecipe(recipe);
        recurseOutputTreeAdd(recipe, list, branch, 0, 0);
    }

    private static void recurseOutputTreeAdd(@NotNull Recipe recipe, @NotNull List<AbstractMapIngredient> outputs,
                                             @NotNull OutputBranch branchMap, int index, int count) {
        if (count >= outputs.size()) return;
        if (index >= outputs.size())
            throw new RuntimeException("Index out of bounds for recurseOutputTreeAdd, should not happen");

        var current = outputs.get(index);
        var targetMap = branchMap.getNodes(current);

        EitherOrBoth<Set<Recipe>, OutputBranch> r = targetMap.compute(current, (k, v) -> {
            if (count == outputs.size() - 1) {
                if (v == null) v = new EitherOrBoth<>();
                v.setLeftIfNoValue(new ObjectLinkedOpenHashSet<>(1));
                // noinspection OptionalGetWithoutIsPresent
                v.getLeft().get().add(recipe);
                return v;
            }
            return v == null ? EitherOrBoth.right(new OutputBranch()) : v.setRightIfNoValue(new OutputBranch());
        });
        if (r.getLeft().isPresent() && r.getLeft().get().contains(recipe)) return;

        // We do not need to check if right exists, because of the setRightIfNoValue call
        // noinspection OptionalGetWithoutIsPresent
        recurseOutputTreeAdd(recipe, outputs, r.getRight().get(), (index + 1) % outputs.size(), count + 1);
    }

    public static void remove(@NotNull Recipe recipe, @NotNull OutputBranch branch) {
        if (LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.LINEAR_SEARCH || outputBranchesCleared)
            return;
        var list = getOutputFromRecipe(recipe);
        recurseOutputTreeRemove(recipe, list, branch);
    }

    private static boolean recurseOutputTreeRemove(@NotNull Recipe recipe, @NotNull List<AbstractMapIngredient> outputs,
                                                   @NotNull OutputBranch branchMap) {
        for (var output : outputs) {
            var targetMap = branchMap.getNodesIfExists(output);
            if (targetMap == null) continue;

            EitherOrBoth<Set<Recipe>, OutputBranch> result = targetMap.get(output);

            FoundType found = FoundType.NONE;
            if (result == null) continue;

            if (result.getLeft().isPresent() && result.getLeft().get().contains(recipe)) {
                found = FoundType.RECIPE;
            }
            if (result.getRight().isPresent()) {
                if (recurseOutputTreeRemove(recipe, outputs.subList(1, outputs.size()), result.getRight().get()))
                    found = FoundType.BRANCH;
            }

            if (found != FoundType.NONE) {
                if (outputs.size() == 1 && found == FoundType.RECIPE) {
                    // a recipe was found, and this is the only ingredient, so remove it directly
                    if (result.getLeft().isPresent() && result.getLeft().get().size() <= 1) result.removeLeft();
                    else result.getLeft().get().remove(recipe);
                }
                if (result.getRight().isPresent() && found == FoundType.BRANCH) {
                    OutputBranch branch = result.getRight().get();
                    if (branch.isEmpty()) result.removeRight();
                }
                // noinspection SimplifyOptionalCallChains
                if (!result.getLeft().isPresent() && !result.getRight().isPresent()) targetMap.remove(output);
                return true;
            }
        }
        return false;
    }

    private static List<AbstractMapIngredient> getOutputFromRecipe(Recipe r) {
        List<AbstractMapIngredient> list = new ObjectArrayList<>(
                r.getOutputs().size() + r.getChancedOutputs().getChancedEntries().size() +
                        r.getFluidOutputs().size() + r.getChancedFluidOutputs().getChancedEntries().size());
        for (var output : r.getOutputs()) {
            list.add(getCachedIngredient(
                    new MapOutputItemStackIngredient(output, output.getMetadata(), output.getTagCompound()),
                    outputRoot));
        }
        for (var output : r.getChancedOutputs().getChancedEntries()) {
            list.add(getCachedIngredient(new MapChancedItemStackIngredient(output), chancedOutputRoot));
        }
        for (var output : r.getFluidOutputs()) {
            list.add(getCachedIngredient(new MapFluidIngredient(output), fluidOutputRoot));
        }
        for (var output : r.getChancedFluidOutputs().getChancedEntries()) {
            list.add(getCachedIngredient(new MapChancedFluidIngredient(output), chancedFluidOutputRoot));
        }
        list.sort(Comparator.comparingInt(AbstractMapIngredient::hashCode)); // Help improve speed of lookups
        return list;
    }

    private static AbstractMapIngredient getCachedIngredient(AbstractMapIngredient ingredient,
                                                             WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> cache) {
        WeakReference<AbstractMapIngredient> cached = cache.get(ingredient);
        if (cached != null && cached.get() != null)
            return cached.get();

        cache.put(ingredient, new WeakReference<>(ingredient));
        return ingredient;
    }

    private enum FoundType {
        NONE,
        RECIPE,
        BRANCH
    }

    @Nullable
    public static List<Recipe> find(@NotNull OutputBranch branch, @NotNull RecipeMap<?> map,
                                    @NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                                    @NotNull Collection<ChancedItemOutput> chancedItems,
                                    @NotNull Collection<ChancedFluidOutput> chancedFluids,
                                    @NotNull Predicate<Recipe> predicate) {
        var list = prepareOutputFind(items, fluids, chancedItems, chancedFluids);
        if (list == null) return null;
        if (LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.LINEAR_SEARCH || outputBranchesCleared)
            return linearFind(map, list, predicate);
        return recurseOutputTreeFindRecipe(list, branch, predicate);
    }

    @Nullable
    private static List<Recipe> recurseOutputTreeFindRecipe(@NotNull List<AbstractMapIngredient> outputs,
                                                            @NotNull OutputBranch branchRoot,
                                                            @NotNull Predicate<Recipe> canHandle) {
        List<Recipe> result = new ObjectArrayList<>();
        for (int i = 0; i < outputs.size(); ++i) {
            recurseOutputTreeFindRecipe(outputs, branchRoot, canHandle, i, 0, 1L << i, result);
        }

        return result.isEmpty() ? null : result;
    }

    private static boolean recurseOutputTreeFindRecipe(@NotNull List<AbstractMapIngredient> outputs,
                                                       @NotNull OutputBranch branchRoot,
                                                       @NotNull Predicate<Recipe> canHandle, int index, int count,
                                                       long skip, List<Recipe> foundRecipes) {
        if (count == outputs.size()) return false;
        var current = outputs.get(index);
        var targetMap = branchRoot.getNodesIfExists(current);
        if (targetMap == null) return false;

        EitherOrBoth<Set<Recipe>, OutputBranch> result = targetMap.get(current);
        if (result != null) {
            if (result.getLeft().isPresent() && count == outputs.size() - 1) {
                // noinspection SimplifyStreamApiCallChains
                var found = result.getLeft().get().stream().filter(canHandle).collect(Collectors.toList());
                if (!found.isEmpty()) {
                    foundRecipes.addAll(found);
                    if (LabsConfig.groovyScriptSettings.gtRecipeSearchMode ==
                            LabsConfig.GroovyScriptSettings.GTRecipeSearchMode.FAST_TREE)
                        return true;
                }
            }
            if (result.getRight().isPresent()) {
                return diveIngredientTreeFindRecipe(outputs, result.getRight().get(), canHandle, index, count, skip,
                        foundRecipes);
            }
        }
        return false;
    }

    private static boolean diveIngredientTreeFindRecipe(@NotNull List<AbstractMapIngredient> outputs,
                                                        @NotNull OutputBranch map, @NotNull Predicate<Recipe> canHandle,
                                                        int currentIndex, int count, long skip,
                                                        List<Recipe> foundRecipes) {
        for (int i = (currentIndex + 1) % outputs.size(); i != currentIndex; i = (i + 1) % outputs.size()) {
            if ((skip & 1L << i) == 0L) {
                var found = recurseOutputTreeFindRecipe(outputs, map, canHandle, i, count + 1, skip | 1L << i,
                        foundRecipes);
                if (found) return true;
            }
        }
        return false;
    }

    @Nullable
    private static List<AbstractMapIngredient> prepareOutputFind(@NotNull Collection<ItemStack> items,
                                                                 @NotNull Collection<FluidStack> fluids,
                                                                 @NotNull Collection<ChancedItemOutput> chancedItems,
                                                                 @NotNull Collection<ChancedFluidOutput> chancedFluids) {
        if (items.size() != Integer.MAX_VALUE && fluids.size() != Integer.MAX_VALUE) {
            if (items.isEmpty() && fluids.isEmpty() && chancedItems.isEmpty() && chancedFluids.isEmpty()) {
                return null;
            } else {
                List<AbstractMapIngredient> list = new ObjectArrayList<>(items.size() + fluids.size());
                for (var output : items) {
                    list.add(getCachedIngredient(
                            new MapOutputItemStackIngredient(output, output.getMetadata(), output.getTagCompound()),
                            outputRoot));
                }
                for (var output : chancedItems) {
                    list.add(getCachedIngredient(new MapChancedItemStackIngredient(output), chancedOutputRoot));
                }
                for (var output : fluids) {
                    list.add(getCachedIngredient(new MapFluidIngredient(output), fluidOutputRoot));
                }
                for (var output : chancedFluids) {
                    list.add(getCachedIngredient(new MapChancedFluidIngredient(output), chancedFluidOutputRoot));
                }

                list.sort(Comparator.comparingInt(AbstractMapIngredient::hashCode)); // Help improve speed of lookups

                return list.isEmpty() ? null : list;
            }
        } else {
            return null;
        }
    }

    @Nullable
    private static List<Recipe> linearFind(@NotNull RecipeMap<?> map,
                                           @NotNull List<AbstractMapIngredient> list,
                                           @NotNull Predicate<Recipe> predicate) {
        List<Recipe> result = new ArrayList<>();
        for (var recipe : map.getRecipeList()) {
            var recipeOutputs = getOutputFromRecipe(recipe);
            // This compares lists with order in mind, but since they are sorted by hashcode already, should be fine
            if (recipeOutputs.equals(list) && predicate.test(recipe)) result.add(recipe);
        }
        return result.isEmpty() ? null : result;
    }
}
