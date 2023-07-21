package com.nomiceu.nomilabs.recipe;

import com.nomiceu.nomilabs.registry.LabsRecipeMaps;
import net.minecraft.init.Blocks;

public class GreenhouseTestRecipes {
    public static void postInit() {
        assert Blocks.SAPLING != null;
        assert Blocks.LOG != null;
        LabsRecipeMaps.GREENHOUSE_RECIPES.recipeBuilder().duration(1200).EUt(40)
                .input(Blocks.SAPLING)
                .output(Blocks.LOG)
                .buildAndRegister();
    }
}
