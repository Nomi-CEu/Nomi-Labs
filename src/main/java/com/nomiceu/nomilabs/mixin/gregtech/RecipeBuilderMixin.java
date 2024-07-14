package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.groovy.RecyclingHelper;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.util.EnumValidationResult;

@Mixin(value = RecipeBuilder.class, remap = false)
public abstract class RecipeBuilderMixin<R extends RecipeBuilder<R>> {

    @Shadow
    @Final
    protected List<ItemStack> outputs;

    @Shadow
    @Final
    protected List<GTRecipeInput> inputs;

    @Shadow
    protected EnumValidationResult recipeStatus;

    @Shadow
    public abstract RecipeBuilder<R> inputNBT(GTRecipeInput input, NBTMatcher matcher, NBTCondition condition);

    @Unique
    @SuppressWarnings("unused")
    public RecipeBuilder<R> changeRecycling() {
        if (!RecyclingHelper.changeStackRecycling(outputs, inputs))
            recipeStatus = EnumValidationResult.INVALID;

        // noinspection unchecked
        return (RecipeBuilder<R>) (Object) this;
    }

    @Unique
    @SuppressWarnings("unused")
    public RecipeBuilder<R> inputWildNBT(ItemStack stack) {
        return inputNBT(stack, NBTMatcher.ANY, NBTCondition.ANY);
    }

    @Unique
    @SuppressWarnings("unused")
    public RecipeBuilder<R> inputNBT(ItemStack stack, NBTMatcher matcher, NBTCondition condition) {
        return inputNBT(new GTRecipeItemInput(stack), matcher, condition);
    }
}
