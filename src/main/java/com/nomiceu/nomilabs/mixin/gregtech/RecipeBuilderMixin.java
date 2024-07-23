package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.groovy.RecyclingHelper;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
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

    @Shadow
    protected RecipeMap<R> recipeMap;

    @Shadow
    @Final
    protected List<FluidStack> fluidOutputs;

    @Shadow
    @Final
    protected List<ChancedItemOutput> chancedOutputs;

    @Shadow
    @Final
    protected List<ChancedFluidOutput> chancedFluidOutputs;

    @Shadow
    protected int EUt;

    @Shadow
    protected GTRecipeCategory category;

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

    @Unique
    @SuppressWarnings("unused")
    public R replace(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        (r) -> true),
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceInCategory(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        (r) -> Objects.equals(category, r.getRecipeCategory())),
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceWithVoltage(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findRecipeByOutput(EUt, outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs),
                String.format("voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", EUt, outputs,
                        fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceWithExactVoltage(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findRecipeByOutput(EUt, outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs),
                String.format("exact voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", EUt,
                        outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replace(Predicate<Recipe> canHandle, RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        canHandle),
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    private R replaceForMaps(RecipeMap<?>[] otherMaps, Consumer<RecipeMap<?>> remover) {
        remover.accept(recipeMap);
        Arrays.stream(otherMaps).forEach(remover);
        return (R) (Object) this;
    }

    @Unique
    private void removeOrWarn(RecipeMap<?> currMap, @Nullable List<Recipe> foundRecipes, String components) {
        if (foundRecipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error removing GregTech " + currMap.unlocalizedName + " recipe")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return;
        }

        for (var recipe : foundRecipes) {
            currMap.removeRecipe(recipe);
        }
    }
}
