package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.groovy.LabsVirtualizedRegistries;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecyclingHandler;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.util.EnumValidationResult;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

import static com.nomiceu.nomilabs.util.LabsGroovyHelper.throwOrGroovyLog;

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
        if (outputs.size() != 1 || inputs.isEmpty()) {
            throwOrGroovyLog(new IllegalArgumentException("Cannot change recycling when there is more than one output, or there are no inputs!"));
            recipeStatus = EnumValidationResult.INVALID;
        }
        var output = outputs.get(0);
        LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerOre(output,
                RecyclingHandler.getRecyclingIngredients(inputs, output.getCount()));
        //noinspection unchecked
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
