package com.nomiceu.nomilabs.gregtech.recipe;

import net.minecraftforge.fluids.FluidStack;

import com.nomiceu.nomilabs.gregtech.prefix.LabsOrePrefix;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;

public class PerfectGemsCutterRecipes {

    public static void initRecipes() {
        LabsOrePrefix.GEM_PERFECT
                .addProcessingHandler(((orePrefix, material) -> RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                        .input(orePrefix, material)
                        .fluidInputs(new FluidStack(Materials.Lubricant.getFluid(), 100))
                        .output(OrePrefix.gemExquisite, material, 2)
                        .duration(100).EUt(1920).buildAndRegister()));
    }
}
