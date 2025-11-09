package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.FlaggableRecipe;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import gregtech.api.util.GTUtility;

/**
 * Makes recipes registered when {@link LabsGroovyHelper#LABS_GROOVY_RUNNING} have groovy
 * recipe status. Also, removes unneeded processing when calculating recipe outputs.
 * Also, allows recipes to be 'flagged'.
 */
@Mixin(value = Recipe.class, remap = false)
public abstract class RecipeMixin implements FlaggableRecipe {

    @Shadow
    @Mutable
    @Final
    private boolean groovyRecipe;

    @Shadow
    public abstract ChancedOutputList<ItemStack, ChancedItemOutput> getChancedOutputs();

    @Shadow
    public abstract NonNullList<ItemStack> getOutputs();

    @Shadow
    public abstract List<FluidStack> getFluidOutputs();

    @Shadow
    public abstract ChancedOutputList<FluidStack, ChancedFluidOutput> getChancedFluidOutputs();

    @Unique
    private String labs$flag = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setLabsGroovyRecipe(@NotNull List<GTRecipeInput> inputs,
                                     List<ItemStack> outputs,
                                     @NotNull ChancedOutputList<ItemStack, ChancedItemOutput> chancedOutputs,
                                     List<GTRecipeInput> fluidInputs,
                                     List<FluidStack> fluidOutputs,
                                     @NotNull ChancedOutputList<FluidStack, ChancedFluidOutput> chancedFluidOutputs,
                                     int duration,
                                     int EUt,
                                     boolean hidden,
                                     boolean isCTRecipe,
                                     IRecipePropertyStorage recipePropertyStorage,
                                     @NotNull GTRecipeCategory recipeCategory,
                                     CallbackInfo ci) {
        if (LabsGroovyHelper.LABS_GROOVY_RUNNING) {
            groovyRecipe = true;
        }
    }

    @Inject(method = "getResultItemOutputs", at = @At("HEAD"), cancellable = true)
    private void newItemLogic(int recipeTier, int machineTier, RecipeMap<?> recipeMap,
                              CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> outputs = new ArrayList<>(GTUtility.copyStackList(getOutputs()));
        ChanceBoostFunction function = recipeMap.getChanceFunction();
        List<ChancedItemOutput> chancedOutputs = getChancedOutputs().roll(function, recipeTier, machineTier);

        if (chancedOutputs != null) {
            for (var output : chancedOutputs) {
                outputs.add(output.getIngredient().copy());
            }
        }
        cir.setReturnValue(outputs);
    }

    @Inject(method = "getResultFluidOutputs", at = @At("HEAD"), cancellable = true)
    private void newFluidLogic(int recipeTier, int machineTier, RecipeMap<?> recipeMap,
                               CallbackInfoReturnable<List<FluidStack>> cir) {
        List<FluidStack> outputs = new ArrayList<>(GTUtility.copyFluidList(getFluidOutputs()));
        ChanceBoostFunction function = recipeMap.getChanceFunction();
        List<ChancedFluidOutput> chancedOutputs = getChancedFluidOutputs().roll(function, recipeTier, machineTier);

        if (chancedOutputs != null) {
            for (var output : chancedOutputs) {
                outputs.add(output.getIngredient().copy());
            }
        }
        cir.setReturnValue(outputs);
    }

    @Unique
    @Override
    public void labs$setFlag(String flag) {
        labs$flag = flag;
    }

    @Unique
    @Override
    public @Nullable String labs$getFlag() {
        return labs$flag;
    }
}
