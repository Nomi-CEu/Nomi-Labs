package com.nomiceu.nomilabs.groovy;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleIntCircuitIngredient;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.recipes.recipeproperties.RecipeProperty;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ChangeRecipeBuilder<R extends RecipeBuilder<R>> {

    private final Recipe originalRecipe;
    private final RecipeMap<R> originalRecipeMap;
    private final R builder;

    public ChangeRecipeBuilder(Recipe originalRecipe, RecipeMap<R> map) {
        this.originalRecipe = originalRecipe;
        this.originalRecipeMap = map;
        this.builder = map.recipeBuilder();

        // Make sure to Copy EVERYTHING
        for (var input : originalRecipe.getInputs()) {
            builder.input(copy(input));
        }
        for (var input : originalRecipe.getFluidInputs()) {
            builder.fluidInputs(copy(input));
        }
        for (var output : originalRecipe.getOutputs()) {
            builder.outputs(output.copy());
        }
        for (var output : originalRecipe.getFluidOutputs()) {
            builder.fluidOutputs(output.copy());
        }
        for (var output : originalRecipe.getChancedOutputs().getChancedEntries()) {
            builder.chancedOutput(output.getIngredient().copy(), output.getChance(), output.getChanceBoost());
        }
        for (var output : originalRecipe.getChancedFluidOutputs().getChancedEntries()) {
            builder.chancedFluidOutput(output.getIngredient().copy(), output.getChance(), output.getChanceBoost());
        }

        if (originalRecipe.isHidden())
            builder.hidden();

        builder.chancedOutputLogic(originalRecipe.getChancedOutputs().getChancedOutputLogic());
        builder.chancedFluidOutputLogic(originalRecipe.getChancedFluidOutputs().getChancedOutputLogic());
        builder.duration(originalRecipe.getDuration());
        builder.EUt(originalRecipe.getEUt());
    }

    public ChangeRecipeBuilder<R> builder(Consumer<R> builderChanger) {
        builderChanger.accept(builder);
        return this;
    }

    /**
     * Returns -1 if no Circuit Ingredient.
     */
    private int getCircuitMeta(List<GTRecipeInput> inputs, boolean remove) {
        for (var input : inputs) {
            if (input instanceof IntCircuitIngredient circuit) {
                if (remove) inputs.remove(input);
                return ((AccessibleIntCircuitIngredient) circuit).getMeta();
            }
        }
        return -1;
    }

    public ChangeRecipeBuilder<R> clearCircuitMeta() {
        getCircuitMeta(builder.getInputs(), true);
        return this;
    }

    /**
     * Change Circuit. If there is no circuit, the function gets an input of -1.
     */
    public ChangeRecipeBuilder<R> changeCircuitMeta(Function<Integer, Integer> circuitMeta) {
        clearCircuitMeta();

        // No need to copy, we are not changing the list, just searching
        builder.circuitMeta(circuitMeta.apply(getCircuitMeta(originalRecipe.getInputs(), false)));
        return this;
    }

    public ChangeRecipeBuilder<R> changeEUt(Function<Integer, Integer> eut) {
        builder.EUt(eut.apply(originalRecipe.getEUt()));
        return this;
    }

    public ChangeRecipeBuilder<R> changeDuration(Function<Integer, Integer> duration) {
        builder.duration(duration.apply(originalRecipe.getDuration()));
        return this;
    }

    public ChangeRecipeBuilder<R> changeInputs(Consumer<List<GTRecipeInput>> itemChanger) {
        builder.clearInputs();

        var newInputs = originalRecipe.getInputs()
                .stream().map(ChangeRecipeBuilder::copy).collect(Collectors.toList());
        itemChanger.accept(newInputs);
        builder.inputIngredients(newInputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachInput(Function<GTRecipeInput, GTRecipeInput> itemChanger) {
        builder.clearInputs();
        for (var origInput : originalRecipe.getInputs()) {
            var newInput = itemChanger.apply(copy(origInput));

            if (newInput != null)
                builder.input(newInput);
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeFluidInputs(Consumer<List<GTRecipeInput>> fluidChanger) {
        builder.clearFluidInputs();

        var newInputs = originalRecipe.getFluidInputs()
                .stream().map(ChangeRecipeBuilder::copy).collect(Collectors.toList());
        fluidChanger.accept(newInputs);
        builder.fluidInputs(newInputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachFluidInput(Function<GTRecipeInput, GTRecipeInput> fluidChanger) {
        builder.clearFluidInputs();
        for (var origInput : originalRecipe.getFluidInputs()) {
            var newInput = fluidChanger.apply(copy(origInput));

            if (newInput != null)
                builder.fluidInputs(newInput);
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeOutputs(Consumer<List<ItemStack>> itemChanger) {
        builder.clearOutputs();

        var newOutputs = originalRecipe.getOutputs()
                .stream().map(ItemStack::copy).collect(Collectors.toList());
        itemChanger.accept(newOutputs);
        builder.outputs(newOutputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachOutput(Function<ItemStack, ItemStack> itemChanger) {
        builder.clearOutputs();
        for (var origOutput : originalRecipe.getOutputs()) {
            var newOutput = itemChanger.apply(origOutput.copy());

            if (newOutput != null)
                builder.outputs(newOutput);
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeFluidOutputs(Consumer<List<FluidStack>> fluidChanger) {
        builder.clearFluidOutputs();

        var newOutputs = originalRecipe.getFluidOutputs()
                .stream().map(FluidStack::copy).collect(Collectors.toList());
        fluidChanger.accept(newOutputs);
        builder.fluidOutputs(newOutputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachFluidOutput(Function<FluidStack, FluidStack> fluidChanger) {
        builder.clearFluidOutputs();
        for (var origOutput : originalRecipe.getFluidOutputs()) {
            var newOutput = fluidChanger.apply(origOutput.copy());

            if (newOutput != null)
                builder.fluidOutputs(newOutput);
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeChancedOutputs(Consumer<List<ChancedItemOutput>> itemChanger) {
        builder.clearChancedOutput();

        var newOutputs = originalRecipe.getChancedOutputs().getChancedEntries()
                .stream().map(ChancedItemOutput::copy).collect(Collectors.toList());
        itemChanger.accept(newOutputs);
        builder.chancedOutputs(newOutputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachChancedOutput(Function<ChancedItemOutput, ChancedItemOutput> itemChanger) {
        builder.clearChancedOutput();
        for (var origOutput : originalRecipe.getChancedOutputs().getChancedEntries()) {
            var newOutput = itemChanger.apply(origOutput);

            if (newOutput != null)
                builder.chancedOutput(newOutput.getIngredient().copy(), newOutput.getChance(),
                        newOutput.getChanceBoost());
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeChancedFluidOutputs(Consumer<List<ChancedFluidOutput>> fluidChanger) {
        builder.clearChancedFluidOutputs();

        var newOutputs = originalRecipe.getChancedFluidOutputs().getChancedEntries()
                .stream().map(ChancedFluidOutput::copy).collect(Collectors.toList());
        fluidChanger.accept(newOutputs);
        builder.chancedFluidOutputs(newOutputs);
        return this;
    }

    public ChangeRecipeBuilder<R> changeEachChancedFluidOutput(Function<ChancedFluidOutput, ChancedFluidOutput> fluidChanger) {
        builder.clearChancedFluidOutputs();
        for (var origOutput : originalRecipe.getChancedFluidOutputs().getChancedEntries()) {
            var newOutput = fluidChanger.apply(origOutput);

            if (newOutput != null)
                builder.chancedFluidOutput(newOutput.getIngredient().copy(), newOutput.getChance(),
                        newOutput.getChanceBoost());
        }
        return this;
    }

    public ChangeRecipeBuilder<R> changeChancedOutputLogic(Function<ChancedOutputLogic, ChancedOutputLogic> logicChanger) {
        builder.chancedOutputLogic(logicChanger.apply(originalRecipe.getChancedOutputs().getChancedOutputLogic()));
        return this;
    }

    public ChangeRecipeBuilder<R> changeChancedFluidOutputLogic(Function<ChancedOutputLogic, ChancedOutputLogic> logicChanger) {
        builder.chancedFluidOutputLogic(
                logicChanger.apply(originalRecipe.getChancedFluidOutputs().getChancedOutputLogic()));
        return this;
    }

    public ChangeRecipeBuilder<R> copyProperties(RecipeProperty<?>... properties) {
        for (var property : properties) {
            var prop = originalRecipe.getProperty(property, null);
            if (prop == null) {
                GroovyLog.get().error("Could not find property {} in recipe {}!", property.getKey(), originalRecipe);
                continue;
            }

            builder.applyProperty(property, prop);
        }
        return this;
    }

    /**
     * It is important that you, somehow, make a copy of the input property, and not modify the property itself!<br>
     * Otherwise, reloading may not work correctly, and can cause modifications to be applied on top of each other!
     */
    public <T> ChangeRecipeBuilder<R> changeProperty(RecipeProperty<T> property, Function<T, T> changer) {
        T prop = originalRecipe.getProperty(property, null);
        if (prop == null) {
            GroovyLog.get().error("Could not find property {} in recipe {}!", property.getKey(), originalRecipe);
            return this;
        }

        builder.applyProperty(property, changer.apply(prop));
        return this;
    }

    public void buildAndRegister() {
        builder.buildAndRegister();
    }

    public void replaceAndRegister() {
        originalRecipeMap.removeRecipe(originalRecipe);
        buildAndRegister();
    }

    private static GTRecipeInput copy(GTRecipeInput in) {
        return ((AccessibleGTRecipeInput) in).accessibleCopy();
    }
}
