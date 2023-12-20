package com.nomiceu.nomilabs.gregtech;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;

import java.util.ArrayList;
import java.util.List;

public class LabsRecipeMaps {
    public static List<RecipeMap<SimpleRecipeBuilder>> MICROVERSE_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> CREATIVE_TANK_RECIPES;
    public static List<RecipeMap<FuelRecipeBuilder>> NAQUADAH_REACTOR_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> ACTUALIZATION_CHAMBER_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> UNIVERSAL_CRYSTALIZER_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> GREENHOUSE_RECIPES;

    public static void preInit() {
        MICROVERSE_RECIPES = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MICROVERSE_RECIPES.add(i, createMicroverseRecipeMap(i + 1));
        }

        CREATIVE_TANK_RECIPES = new RecipeMap<>("creative_tank_provider", 2, 2, 0, 0, new SimpleRecipeBuilder(), false)
                .setSlotOverlay(false, false, GuiTextures.ATOMIC_OVERLAY_1).setSlotOverlay(true, false, GuiTextures.ATOMIC_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, ProgressWidget.MoveType.HORIZONTAL).setSound(GTSoundEvents.REPLICATOR);

        NAQUADAH_REACTOR_RECIPES = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            NAQUADAH_REACTOR_RECIPES.add(i, createNaqRecipeMap(i + 1));
        }

        ACTUALIZATION_CHAMBER_RECIPES = new RecipeMap<>("actualization_chamber", 2, 16, 0, 0, new SimpleRecipeBuilder(), false)
                .setSlotOverlay(false, false, GuiTextures.MOLECULAR_OVERLAY_1).setSound(GTSoundEvents.MINER)
                .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressWidget.MoveType.HORIZONTAL);

        UNIVERSAL_CRYSTALIZER_RECIPES = new RecipeMap<>("universal_crystallizer", 9, 1, 1, 0, new SimpleRecipeBuilder(), false)
                .setSlotOverlay(true, false, GuiTextures.CRYSTAL_OVERLAY).setSound(GTSoundEvents.COMPUTATION)
                .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressWidget.MoveType.HORIZONTAL);

        GREENHOUSE_RECIPES = new RecipeMap<>("greenhouse", 4, 9, 1, 0, new SimpleRecipeBuilder(), false)
                .setSlotOverlay(false, false, GuiTextures.SCANNER_OVERLAY).setSlotOverlay(false, true, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(true, false, GuiTextures.SCANNER_OVERLAY).setSlotOverlay(true, true, GuiTextures.SCANNER_OVERLAY)
                .setSound(GTSoundEvents.SAW_TOOL);
    }

    private static RecipeMap<SimpleRecipeBuilder> createMicroverseRecipeMap(int tier) {
        return new RecipeMap<>("microverse_projector_" + tier, tier == 3 ? 9 : 4, 16, tier == 1 ? 1 : 0, 0, new SimpleRecipeBuilder(), false)
                .setProgressBar(LabsTextures.PROGRESS_BAR_ROCKET, ProgressWidget.MoveType.HORIZONTAL).setSound(LabsSounds.MICROVERSE)
                .setSlotOverlay(false, false, GuiTextures.IMPLOSION_OVERLAY_1);
    }

    private static RecipeMap<FuelRecipeBuilder> createNaqRecipeMap(int tier) {
        return new RecipeMap<>("naquadah_reactor_" + tier, 1, 1, 0, 0, new FuelRecipeBuilder(), false)
                .setSlotOverlay(false, false, GuiTextures.ATOMIC_OVERLAY_1).setSlotOverlay(false, true, GuiTextures.ATOMIC_OVERLAY_1)
                .setSlotOverlay(true, false, GuiTextures.ATOMIC_OVERLAY_1).setSlotOverlay(true, true, GuiTextures.ATOMIC_OVERLAY_1)
                .setSound(GTSoundEvents.TURBINE);
    }
}
