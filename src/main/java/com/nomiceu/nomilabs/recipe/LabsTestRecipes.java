package com.nomiceu.nomilabs.recipe;

import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import net.minecraft.init.Blocks;

public class LabsTestRecipes {
    public static void postInit() {
        assert Blocks.SAPLING != null;
        assert Blocks.LOG != null;
        LabsRecipeMaps.GREENHOUSE_RECIPES.recipeBuilder().duration(1200).EUt(40)
                .input(Blocks.SAPLING)
                .output(Blocks.LOG)
                .buildAndRegister();

        for (var recipeBuilder : LabsRecipeMaps.MICROVERSE_RECIPES) {
            recipeBuilder.recipeBuilder().duration(1200).EUt(40)
                .input(LabsItems.T1_SHIP)
                .output(Blocks.REDSTONE_ORE, 64)
                .buildAndRegister();
        }
    }
}
