package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.nomiceu.nomilabs.groovy.RecyclingHelper;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.util.EnumValidationResult;

@SuppressWarnings({ "unchecked", "DataFlowIssue" })
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
    public abstract R inputNBT(GTRecipeInput input, NBTMatcher matcher, NBTCondition condition);

    @Shadow
    private static GTRecipeInput ofGroovyIngredient(IIngredient ingredient) {
        return null;
    }

    @Unique
    @SuppressWarnings("unused")
    public R changeRecycling() {
        if (!RecyclingHelper.changeStackRecycling(outputs, inputs))
            recipeStatus = EnumValidationResult.INVALID;

        return (R) (Object) this;
    }

    @Unique
    public R inputNBT(IIngredient ingredient, NBTMatcher matcher, NBTCondition condition) {
        return inputNBT(ofGroovyIngredient(ingredient), matcher, condition);
    }

    @Unique
    @SuppressWarnings("unused")
    public R inputWildNBT(IIngredient ingredient) {
        return inputNBT(ingredient, NBTMatcher.ANY, NBTCondition.ANY);
    }
}
