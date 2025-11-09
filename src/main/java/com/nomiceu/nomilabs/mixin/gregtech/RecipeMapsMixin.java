package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeBuilder;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.FlaggableRecipe;

import crafttweaker.CraftTweakerAPI;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Part of the handling for labs' scripting find filters and remove actions.
 */
@Mixin(value = RecipeMaps.class, remap = false)
public class RecipeMapsMixin {

    @Shadow
    @Final
    public static RecipeMap<SimpleRecipeBuilder> CHEMICAL_RECIPES;

    @Shadow
    @Final
    public static RecipeMap<SimpleRecipeBuilder> LARGE_CHEMICAL_RECIPES;

    @Unique
    private static final String LABS$LCR_IS_CHEM_RECIPE = "LABS$LCR_IS_CHEM_RECIPE";

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setScriptingActions(CallbackInfo ci) {
        if (!LabsConfig.groovyScriptSettings.scriptingChemReactorImprovements)
            return;

        // CR removal syncing
        ((AccessibleRecipeMap) CHEMICAL_RECIPES).labs$setScriptRemoveAction(RecipeMapsMixin::labs$chemOnRemoval);

        // LCR exclusive searching
        CHEMICAL_RECIPES.onRecipeBuild(RecipeMapsMixin::labs$chemOnRecipeBuild);
        ((AccessibleRecipeMap) LARGE_CHEMICAL_RECIPES).labs$setScriptFindFilter(
                (r) -> !LABS$LCR_IS_CHEM_RECIPE.equals(((FlaggableRecipe) r).labs$getFlag()),
                "[Nomi Labs CR & LCR Recipe Handler] Containing Large Chemical Reactor findByOutput to LCR specific recipes ONLY...");
    }

    @Unique
    private static void labs$chemOnRemoval(Pair<AccessibleRecipeMap.ScriptType, Recipe> pair) {
        List<ItemStack> inputs = new ObjectArrayList<>(pair.getRight().getInputs().size());
        for (var input : pair.getRight().getInputs()) {
            inputs.add(input.getInputStacks()[0]);
        }

        List<FluidStack> fluidInputs = new ObjectArrayList<>(pair.getRight().getInputs().size());
        for (var fluidInput : pair.getRight().getFluidInputs()) {
            fluidInputs.add(fluidInput.getInputFluidStack());
        }

        Recipe recipe = ((AccessibleRecipeMap) LARGE_CHEMICAL_RECIPES).labs$rawFind(inputs, fluidInputs,
                (r) -> LABS$LCR_IS_CHEM_RECIPE.equals(((FlaggableRecipe) r).labs$getFlag()) &&
                        r.equals(pair.getRight()));

        // Error: could not find corresponding LCR recipe
        if (recipe == null) {
            if (pair.getLeft() == AccessibleRecipeMap.ScriptType.CT && Loader.isModLoaded(LabsValues.CT_MODID)) {
                labs$logCTError(String.format(
                        "[Nomi Labs CR & LCR Recipe Handler] Error syncing chemical reactor removals to large chemical reactor: could not find recipe corresponding to %s",
                        pair.getRight()));
                return;
            }

            GroovyLog.msg(
                    "[Nomi Labs CR & LCR Recipe Handler] Recipe Error syncing chemical reactor removals to large chemical reactor")
                    .add("could not find recipe corresponding to {}", pair.getRight())
                    .error()
                    .post();
            return;
        }

        String log = "[Nomi Labs CR & LCR Recipe Handler] Syncing chemical reactor recipe removal to large chemical reactor";
        if (pair.getLeft() == AccessibleRecipeMap.ScriptType.CT && Loader.isModLoaded(LabsValues.CT_MODID)) {
            labs$logCTInfo(log);
        } else {
            GroovyLog.msg(log).info().post();
        }

        LARGE_CHEMICAL_RECIPES.removeRecipe(recipe);
    }

    @Unique
    @Optional.Method(modid = LabsValues.CT_MODID)
    private static void labs$logCTInfo(String msg) {
        CraftTweakerAPI.logInfo(msg);
    }

    @Unique
    @Optional.Method(modid = LabsValues.CT_MODID)
    private static void labs$logCTError(String msg) {
        CraftTweakerAPI.logError(msg, new IllegalArgumentException());
    }

    // Mostly same as before, but add a flag, and add to map
    @Unique
    private static void labs$chemOnRecipeBuild(SimpleRecipeBuilder builder) {
        // noinspection unchecked
        ((AccessibleRecipeBuilder<SimpleRecipeBuilder>) builder).labs$invalidateOnBuildAction();
        var newBuilder = RecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder()
                .inputs(builder.getInputs().toArray(new GTRecipeInput[0]))
                .fluidInputs(builder.getFluidInputs())
                .outputs(builder.getOutputs())
                .chancedOutputs(builder.getChancedOutputs())
                .fluidOutputs(builder.getFluidOutputs())
                .chancedFluidOutputs(builder.getChancedFluidOutputs())
                .cleanroom(builder.getCleanroom())
                .duration(builder.getDuration())
                .EUt(builder.getEUt());

        // Future proof: in case LCR gets an on build action added
        // noinspection unchecked
        var buildAction = ((AccessibleRecipeBuilder<SimpleRecipeBuilder>) newBuilder).labs$getOnBuildAction();
        if (buildAction != null) {
            buildAction.accept(newBuilder);
        }

        var result = newBuilder.build();
        ((FlaggableRecipe) result.getResult()).labs$setFlag(LABS$LCR_IS_CHEM_RECIPE);
        RecipeMaps.LARGE_CHEMICAL_RECIPES.addRecipe(result);
    }
}
