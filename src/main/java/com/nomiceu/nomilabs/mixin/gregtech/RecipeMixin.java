package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;

/**
 * Makes recipes registered when {@link com.nomiceu.nomilabs.util.LabsGroovyHelper#LABS_GROOVY_RUNNING} is true groovy
 * recipes.
 */
@Mixin(value = Recipe.class, remap = false)
public class RecipeMixin {

    @Shadow
    @Mutable
    @Final
    private boolean groovyRecipe;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void setLabsGroovyRecipe(@NotNull List<GTRecipeInput> inputs,
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
}
