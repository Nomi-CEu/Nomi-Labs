package com.nomiceu.nomilabs.integration.nuclearcraft;

import com.nomiceu.nomilabs.NomiLabs;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.MetaItem.MetaValueItem;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.common.items.MetaItems;
import nc.config.NCConfig;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeHelper;
import nc.recipe.ingredient.*;
import nc.util.NCUtil;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Thanks to Exaxxion, in the repo <a href="https://github.com/Exaxxion/NuclearCraft/tree/2.18y-ceu">Exaxxion/NuclearCraft/tree/2.18y-ceu</a>, for the original fixes!
 * This contains cleaned up and modified code, that uses new GT apis, and makes use of native GT recipe checks.
 * All the util methods are private, thus we must add them.
 */
public class GTCEuRecipes {
    public static void addGTCEuRecipe(String recipeName, ProcessorRecipe recipe) {
        RecipeMap<?> recipeMap = null;
        RecipeBuilder<?> builder = null;

        switch (recipeName) {
            case "manufactory" -> {
                recipeMap = RecipeMaps.MACERATOR_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 12, 8);
            }
            case "isotope_separator" -> {
                recipeMap = RecipeMaps.THERMAL_CENTRIFUGE_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 48, 160);
            }
            case "decay_hastener", "irradiator" -> {
                return;
            }
            case "fuel_reprocessor" -> {
                recipeMap = RecipeMaps.CENTRIFUGE_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 24, 60);
            }
            case "alloy_furnace" -> {
                recipeMap = RecipeMaps.ALLOY_SMELTER_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 10);
            }
            case "infuser" -> {
                recipeMap = RecipeMaps.CHEMICAL_BATH_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 12);
            }
            case "melter" -> {
                recipeMap = RecipeMaps.EXTRACTOR_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 32, 16);
            }
            case "supercooler" -> {
                recipeMap = RecipeMaps.VACUUM_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 240, 20);
            }
            case "electrolyser" -> {
                recipeMap = RecipeMaps.ELECTROLYZER_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 16);
            }
            case "ingot_former" -> {
                recipeMap = RecipeMaps.FLUID_SOLIDFICATION_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 1);
            }
            case "pressurizer" -> {
                if (isPlateRecipe(recipe)) {
                    recipeMap = RecipeMaps.BENDER_RECIPES;
                    builder = addStats(recipeMap.recipeBuilder(), recipe, 24, 10);
                    builder.circuitMeta(1);
                } else {
                    recipeMap = RecipeMaps.COMPRESSOR_RECIPES;
                    builder = addStats(recipeMap.recipeBuilder(), recipe, 2, 20);
                }
            }
            case "chemical_reactor" -> {
                recipeMap = RecipeMaps.CHEMICAL_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 30);
            }
            case "salt_mixer" -> {
                recipeMap = RecipeMaps.MIXER_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 12);
            }
            case "crystallizer" -> {
                recipeMap = RecipeMaps.CHEMICAL_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 10);
                builder.circuitMeta(0);
            }
            case "dissolver" -> {
                recipeMap = RecipeMaps.CHEMICAL_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 20);
                builder.circuitMeta(1);
            }
            case "extractor" -> {
                recipeMap = RecipeMaps.EXTRACTOR_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 12);
            }
            case "centrifuge" -> {
                recipeMap = RecipeMaps.CENTRIFUGE_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 80);
                builder.circuitMeta(0);
            }
            case "rock_crusher" -> {
                recipeMap = RecipeMaps.MACERATOR_RECIPES;
                builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 12);
            }
        }

        if (recipeMap == null || builder == null) {
            return;
        }

        List<RecipeBuilder<?>> builders = new ArrayList<>(); // Holds all the recipe variants
        builders.add(builder);

        for (IItemIngredient input : recipe.itemIngredients()) {
            if (input instanceof OreIngredient) {
                for (RecipeBuilder<?> builderVariant : builders) {
                    builderVariant.input(((OreIngredient)input).oreName, ((OreIngredient)input).stackSize);
                }
            }
            else {
                List<String> ingredientOreList = new ArrayList<>(); // Hold the different oreDict names
                List<RecipeBuilder<?>> newBuilders = new ArrayList<>();
                for (ItemStack inputVariant : input.getInputStackList()) {
                    if(inputVariant.isEmpty()) continue;
                    Set<String> variantOreList = OreDictHelper.getOreNames(inputVariant);

                    if (!variantOreList.isEmpty()) { // This variant has oreDict entries
                        //noinspection SlowListContainsAll
                        if (ingredientOreList.containsAll(variantOreList)) {
                            continue;
                        }
                        ingredientOreList.addAll(variantOreList);

                        for (RecipeBuilder<?> recipeBuilder : builders) {
                            newBuilders.add(recipeBuilder.copy().input(variantOreList.iterator().next(), inputVariant.getCount()));
                        }
                    }
                    else {
                        for (RecipeBuilder<?> recipeBuilder : builders) {
                            newBuilders.add(recipeBuilder.copy().inputs(inputVariant));
                        }
                    }
                }
                builders = newBuilders;
            }
        }

        if (recipeMap == RecipeMaps.FLUID_SOLIDFICATION_RECIPES) {
            //noinspection rawtypes
            MetaItem.MetaValueItem mold = getIngotFormerMold(recipe);
            for (RecipeBuilder<?> builderVariant : builders) {
                builderVariant.notConsumable(mold);
            }
        }

        for (IFluidIngredient input : recipe.fluidIngredients()) {
            if (input.getInputStackList().isEmpty()) continue;
            for (RecipeBuilder<?> builderVariant : builders) {
                builderVariant.fluidInputs(input.getInputStackList().get(0));
            }
        }

        for (IItemIngredient output : recipe.itemProducts()) {
            if (output instanceof ChanceItemIngredient) {
                List<ItemStack> outputStackList = output.getOutputStackList();
                if (outputStackList.isEmpty()) continue;
                for (RecipeBuilder<?> builderVariant : builders) {
                    //noinspection UnusedAssignment TODO remove this comment when GTCEu implements chanced fluid outputs
                    builderVariant = builderVariant.chancedOutput(outputStackList.get(0), (int)(((ChanceItemIngredient) output).meanStackSize * 10000.0D), 0);
                }
            } else {
                List<ItemStack> outputStackList = output.getOutputStackList();
                if (outputStackList.isEmpty()) continue;
                for (RecipeBuilder<?> builderVariant : builders) {
                    builderVariant.outputs(outputStackList.get(0));
                }
            }
        }

        for (IFluidIngredient output : recipe.fluidProducts()) {
            //noinspection StatementWithEmptyBody TODO remove this comment when GTCEu implements chanced fluid outputs
            if (output instanceof ChanceFluidIngredient) {
                /* TODO: Eventually when GTCEu implements chanced fluid outputs
                List<FluidStack> outputStackList = output.getOutputStackList();
                for (RecipeBuilder<?> builderVariant : builders) {
                    builderVariant.chancedFluidOutputs(outputStackList.get(0), (int)(((ChanceFluidIngredient) output).meanStackSize * 10000.0D), 0);
                }*/
            } else {
                List<FluidStack> outputStackList = output.getOutputStackList();
                if (outputStackList.isEmpty()) continue;
                for (RecipeBuilder<?> builderVariant : builders) {
                    builderVariant.fluidOutputs(outputStackList.get(0));
                }
            }
        }

        boolean built = false;
        for (RecipeBuilder<?> builderVariant : builders) {
            if (!builderVariant.getInputs().isEmpty() || !builderVariant.getFluidInputs().isEmpty()) {
                builderVariant.buildAndRegister();
                built = true;
            }
        }

        if (built && NCConfig.gtce_recipe_logging) {
            NCUtil.getLogger().info("Injected GTCEu " + recipeMap.unlocalizedName + " recipe: " + RecipeHelper.getRecipeString(recipe));
            NomiLabs.LOGGER.info("This recipe was overrided by Nomi Labs' NC Fixes.");
        }
    }

    /* Added Util Methods from https://github.com/Exaxxion/NuclearCraft/blob/2.18y-ceu/src/main/java/nc/integration/gtce/GTCERecipeHelper.java */

    private static RecipeBuilder<?> addStats(RecipeBuilder<?> builder, ProcessorRecipe recipe, int processPower, int processTime) {
        return builder.EUt(Math.max((int) recipe.getBaseProcessPower(processPower), 1)).duration((int) recipe.getBaseProcessTime(20D*processTime));
    }
    private static boolean isPlateRecipe(ProcessorRecipe recipe) {
        ItemStack output = recipe.itemProducts().get(0).getStack();
        return output != null && OreDictHelper.hasOrePrefix(output, "plate", "plateDense");
    }

    private static MetaValueItem getIngotFormerMold(ProcessorRecipe recipe) {
        ItemStack output = recipe.itemProducts().get(0).getStack();
        if (output != null) {
            if (OreDictHelper.hasOrePrefix(output, "ingot")) return MetaItems.SHAPE_MOLD_INGOT;
            else if (OreDictHelper.hasOrePrefix(output, "block")) return MetaItems.SHAPE_MOLD_BLOCK;
        }
        return MetaItems.SHAPE_MOLD_BALL;
    }
}
