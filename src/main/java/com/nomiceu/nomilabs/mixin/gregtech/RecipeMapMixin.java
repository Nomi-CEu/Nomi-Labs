package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.OutputBranch;
import com.nomiceu.nomilabs.gregtech.mixinhelper.RecipeMapLogic;
import com.nomiceu.nomilabs.groovy.RecyclingHelper;

import crafttweaker.CraftTweakerAPI;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.map.AbstractMapIngredient;
import gregtech.api.recipes.map.Branch;
import gregtech.api.util.ValidationResult;
import gregtech.integration.groovy.GroovyScriptModule;
import gregtech.integration.groovy.VirtualizedRecipeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Allows for lookup with outputs. Allows accessing Virtualized Recipe Map.
 * <p>
 * Precaution to make sure only Recycling Recipes are added during recycling recipe reloading.<br>
 * This is because Arc Smelting sometimes generates non-recycling recipes.
 * <p>
 * Part of the handling for labs' scripting find filters and remove actions.
 */
@Mixin(value = RecipeMap.class, remap = false)
public abstract class RecipeMapMixin implements AccessibleRecipeMap {

    @Shadow
    @Final
    private Object grsVirtualizedRecipeMap;

    @Shadow
    public abstract @Nullable Recipe find(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                                          @NotNull Predicate<Recipe> canHandle);

    @Shadow
    protected abstract @Nullable List<List<AbstractMapIngredient>> prepareRecipeFind(@NotNull Collection<ItemStack> items,
                                                                                     @NotNull Collection<FluidStack> fluids);

    @Shadow
    @Nullable
    protected abstract Recipe recurseIngredientTreeFindRecipe(@NotNull List<List<AbstractMapIngredient>> ingredients,
                                                              @NotNull Branch branchRoot,
                                                              @NotNull Predicate<Recipe> canHandle);

    @Shadow
    @Final
    private Branch lookup;

    @Shadow
    @Final
    public String unlocalizedName;
    @Unique
    private final OutputBranch labs$outputLookup = new OutputBranch();

    @Unique
    @Nullable
    private Predicate<Recipe> labs$scriptFindFilter;

    @Unique
    @Nullable
    private String labs$scriptFindFilterMsg;

    @Unique
    @Nullable
    private Consumer<Pair<ScriptType, Recipe>> labs$scriptRemoveAction;

    @Inject(method = "addRecipe", at = @At("HEAD"), cancellable = true)
    private void addRecipeInRecycling(@NotNull ValidationResult<Recipe> validationResult,
                                      CallbackInfoReturnable<Boolean> cir) {
        if (!RecyclingHelper.isReloadingRecycling()) return;
        // If not in the map returns null, which will never equal the recipe category of the recipe, which is never null
        if (!Objects.equals(
                RecyclingHelper.recyclingMaps.get((RecipeMap<?>) (Object) this),
                validationResult.getResult().getRecipeCategory()))
            cir.setReturnValue(false);
    }

    @Inject(method = "removeAllRecipes", at = @At(value = "HEAD"))
    private void updateOutputLookupClear(CallbackInfo ci) {
        labs$outputLookup.clear();
    }

    @Inject(method = "compileRecipe",
            at = @At(value = "INVOKE",
                     target = "Ljava/util/Map;compute(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;"))
    private void updateOutputLookupAdd(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        RecipeMapLogic.add(recipe, labs$outputLookup);
    }

    @Inject(method = "removeRecipe",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/integration/groovy/GroovyScriptModule;isCurrentlyRunning()Z"))
    private void updateOutputLookupRemove(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        RecipeMapLogic.remove(recipe, labs$outputLookup);
    }

    /* Public Interface-Visible Methods */
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Nullable
    @Override
    public List<Recipe> findByOutput(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                                     @NotNull Collection<ChancedItemOutput> chancedItems,
                                     @NotNull Collection<ChancedFluidOutput> chancedFluids,
                                     @NotNull Predicate<Recipe> condition) {
        if (GroovyScriptModule.isCurrentlyRunning() && labs$scriptFindFilter != null) {
            GroovyLog.msg(labs$scriptFindFilterMsg).info().post();
            condition = labs$scriptFindFilter.and(condition);
        }

        List<Recipe> recipes = RecipeMapLogic.find(
                labs$outputLookup, (RecipeMap<?>) (Object) this, items, fluids, chancedItems,
                chancedFluids, condition);

        if (recipes == null || !GroovyScriptModule.isCurrentlyRunning())
            return recipes;

        // Filter away hidden recipes
        // Do this outside of the predicate so we can report how many hidden recipes we skipped
        int foundHidden = 0;
        List<Recipe> result = new ObjectArrayList<>();
        for (var recipe : recipes) {
            if (recipe.isHidden()) {
                foundHidden++;
                continue;
            }

            result.add(recipe);
        }

        if (foundHidden != 0) {
            GroovyLog
                    .msg("[Find By Output] Skipping {} hidden recipes in {}...", foundHidden, unlocalizedName)
                    .info()
                    .post();
        }

        return result;
    }

    @Unique
    @Override
    public @Nullable Recipe labs$rawFind(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                                         @NotNull Predicate<Recipe> canHandle) {
        List<List<AbstractMapIngredient>> list = prepareRecipeFind(items, fluids);
        // couldn't build any inputs to use for search, so no recipe could be found
        if (list == null) return null;
        return recurseIngredientTreeFindRecipe(list, lookup, canHandle);
    }

    @SuppressWarnings({ "AddedMixinMembersNamePattern", "unused" })
    @Unique
    public VirtualizedRecipeMap getVirtualized() {
        return (VirtualizedRecipeMap) grsVirtualizedRecipeMap;
    }

    /* Scripting Actions Handling */

    @WrapOperation(method = "ctFindRecipe",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/api/recipes/RecipeMap;findRecipe(JLjava/util/List;Ljava/util/List;Z)Lgregtech/api/recipes/Recipe;"),
                   require = 1)
    private @Nullable Recipe filterCTSearch(RecipeMap<?> instance, long voltage, List<ItemStack> inputs,
                                            List<FluidStack> fluidInputs, boolean exactVoltage,
                                            Operation<Recipe> original) {
        if (labs$scriptFindFilter == null) {
            return original.call(instance, voltage, inputs, fluidInputs, exactVoltage);
        }

        final List<ItemStack> items = inputs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        final List<FluidStack> fluids = fluidInputs.stream().filter(f -> f != null && f.amount != 0)
                .collect(Collectors.toList());

        if (Loader.isModLoaded(LabsValues.CT_MODID)) {
            labs$logCTInfo(labs$scriptFindFilterMsg);
        }

        return find(items, fluids, recipe -> {
            if (!labs$scriptFindFilter.test(recipe)) {
                return false;
            }

            if (recipe.getEUt() != voltage) {
                // exact voltage IS required, the recipe is not considered valid if not equal
                return false;
            }

            return recipe.matches(false, inputs, fluidInputs);
        });
    }

    @WrapOperation(method = "find",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/api/recipes/RecipeMap;recurseIngredientTreeFindRecipe(Ljava/util/List;Lgregtech/api/recipes/map/Branch;Ljava/util/function/Predicate;)Lgregtech/api/recipes/Recipe;"),
                   require = 1)
    private @Nullable Recipe grsFilterSearch(RecipeMap<?> instance, @NotNull List<List<AbstractMapIngredient>> ing,
                                             @NotNull Branch branch,
                                             @NotNull Predicate<Recipe> condition, Operation<Recipe> original,
                                             @Local(argsOnly = true, ordinal = 0) Collection<ItemStack> items,
                                             @Local(argsOnly = true, ordinal = 1) Collection<FluidStack> fluids) {
        if (GroovyScriptModule.isCurrentlyRunning() && labs$scriptFindFilter != null) {
            GroovyLog.msg(labs$scriptFindFilterMsg).info().post();
            condition = labs$scriptFindFilter.and(condition);
        }

        return original.call(instance, ing, branch, condition);
    }

    @Inject(method = "removeRecipe",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/integration/groovy/VirtualizedRecipeMap;addBackup(Ljava/lang/Object;)V"))
    private void invokeGrSRemoveAction(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        if (labs$scriptRemoveAction != null) {
            labs$scriptRemoveAction.accept(Pair.of(ScriptType.GRS, recipe));
        }
    }

    @Unique
    @Override
    public void labs$setScriptFindFilter(Predicate<Recipe> recipeFilter, @NotNull String filterMsg) {
        labs$scriptFindFilter = recipeFilter;
        labs$scriptFindFilterMsg = filterMsg;
    }

    @Unique
    @Override
    public void labs$setScriptRemoveAction(Consumer<Pair<ScriptType, Recipe>> onRecipeRemove) {
        labs$scriptRemoveAction = onRecipeRemove;
    }

    @Unique
    @Override
    public @Nullable Consumer<Pair<ScriptType, Recipe>> labs$getScriptRemoveAction() {
        return labs$scriptRemoveAction;
    }

    @Unique
    @Optional.Method(modid = LabsValues.CT_MODID)
    private static void labs$logCTInfo(String msg) {
        CraftTweakerAPI.logInfo(msg);
    }
}
