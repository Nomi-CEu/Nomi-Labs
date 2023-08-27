package com.nomiceu.nomilabs.gregtech;

import gregtech.api.gui.GuiTextures;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;

public class LabsRecipeMaps {
    public static RecipeMap<SimpleRecipeBuilder> GREENHOUSE_RECIPES;

    public static void preInit() {
        GREENHOUSE_RECIPES = new RecipeMap<>("greenhouse", 4, 9, 1, 0, new SimpleRecipeBuilder(), false)
                .setSlotOverlay(false, false, GuiTextures.SCANNER_OVERLAY).setSlotOverlay(false, true, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(true, false, GuiTextures.SCANNER_OVERLAY).setSlotOverlay(true, true, GuiTextures.SCANNER_OVERLAY)
                .setSound(GTSoundEvents.SAW_TOOL);
    }
}
