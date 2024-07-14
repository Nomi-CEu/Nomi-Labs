package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.OutputBranch;
import com.nomiceu.nomilabs.gregtech.mixinhelper.RecipeMapLogic;
import com.nomiceu.nomilabs.groovy.RecyclingHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.util.ValidationResult;

/**
 * Allows for lookup with outputs.
 * <p>
 * Precaution to make sure only Recycling Recipes are added during recycling recipe reloading.<br>
 * This is because Arc Smelting sometimes generates non-recycling recipes.
 */
@Mixin(value = RecipeMap.class, remap = false)
public abstract class RecipeMapMixin implements AccessibleRecipeMap {

    @Unique
    private final OutputBranch outputLookup = new OutputBranch();

    @Inject(method = "addRecipe", at = @At("HEAD"), cancellable = true)
    public void addRecipeInRecycling(@NotNull ValidationResult<Recipe> validationResult,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (!RecyclingHelper.isReloadingRecycling()) return;
        // If not in the map returns null, which will never equal the recipe category of the recipe, which is never null
        if (!Objects.equals(RecyclingHelper.recyclingMaps.get((RecipeMap<?>) (Object) this),
                validationResult.getResult().getRecipeCategory()))
            cir.setReturnValue(false);
    }

    @Inject(method = "compileRecipe",
            at = @At(value = "INVOKE",
                     target = "Ljava/util/Map;compute(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;"))
    private void updateOutputLookupAdd(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        RecipeMapLogic.add(recipe, outputLookup);
    }

    @Inject(method = "removeRecipe",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/integration/groovy/GroovyScriptModule;isCurrentlyRunning()Z"))
    private void updateOutputLookupRemove(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        RecipeMapLogic.remove(recipe, outputLookup);
    }

    /* Public Interface-Visible Methods */
    @Unique
    @Nullable
    @Override
    public List<Recipe> findByOutput(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids,
                                     @NotNull Collection<ChancedItemOutput> chancedItems,
                                     @NotNull Collection<ChancedFluidOutput> chancedFluids,
                                     @NotNull Predicate<Recipe> canHandle) {
        return RecipeMapLogic.find(outputLookup, (RecipeMap<?>) (Object) this, items, fluids, chancedItems,
                chancedFluids, canHandle);
    }

    @Unique
    @Nullable
    @Override
    public List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                           List<ChancedItemOutput> chancedItems,
                                           List<ChancedFluidOutput> chancedFluids) {
        return findRecipeByOutput(voltage, inputs, fluidInputs, chancedItems, chancedFluids, false);
    }

    @Unique
    @Nullable
    @Override
    public List<Recipe> findRecipeByOutput(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs,
                                           List<ChancedItemOutput> chancedItems, List<ChancedFluidOutput> chancedFluids,
                                           boolean exactVoltage) {
        List<ItemStack> items = inputs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter(f -> f != null && f.amount != 0)
                .collect(Collectors.toList());

        return findByOutput(items, fluids, chancedItems, chancedFluids, (recipe) -> {
            if (exactVoltage && recipe.getEUt() != voltage) {
                // if exact voltage is required, the recipe is not considered valid
                return false;
            }
            // there is not enough voltage to consider the recipe valid
            return recipe.getEUt() <= voltage;
        });
    }
}
