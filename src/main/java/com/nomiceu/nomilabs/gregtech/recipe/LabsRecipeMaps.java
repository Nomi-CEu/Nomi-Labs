package com.nomiceu.nomilabs.gregtech.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.LabsSounds;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.util.LabsModeHelper;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;

public class LabsRecipeMaps {

    public static List<RecipeMap<SimpleRecipeBuilder>> MICROVERSE_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> CREATIVE_TANK_RECIPES;
    public static List<RecipeMap<FuelRecipeBuilder>> NAQUADAH_REACTOR_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> ACTUALIZATION_CHAMBER_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> UNIVERSAL_CRYSTALIZER_RECIPES;
    public static RecipeMap<DMESimChamberRecipeMapBuilder> DME_SIM_CHAMBER_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> GROWTH_CHAMBER_RECIPES;
    public static RecipeMap<SimpleRecipeBuilder> ECOSYSTEM_EMULATOR_RECIPES;

    public static void preInit() {
        MICROVERSE_RECIPES = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MICROVERSE_RECIPES.add(i, createMicroverseRecipeMap(i + 1));
        }

        CREATIVE_TANK_RECIPES = new RecipeMap<>("creative_tank_provider", 2, 2, 0, 0, new SimpleRecipeBuilder(),
                !(oldMultis() || LabsModeHelper.isNormal()))
                        .setSlotOverlay(false, false, GuiTextures.ATOMIC_OVERLAY_1)
                        .setSlotOverlay(true, false, GuiTextures.ATOMIC_OVERLAY_2)
                        .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, ProgressWidget.MoveType.HORIZONTAL)
                        .setSound(GTSoundEvents.REPLICATOR);

        NAQUADAH_REACTOR_RECIPES = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            NAQUADAH_REACTOR_RECIPES.add(i, createNaqRecipeMap(i + 1));
        }

        ACTUALIZATION_CHAMBER_RECIPES = new DownExpandingRecipeMap("actualization_chamber", 2, 20, 0, 0,
                new SimpleRecipeBuilder(), !(oldMultis() || LabsModeHelper.isExpert()))
                        .setSlotOverlay(false, false, GuiTextures.MOLECULAR_OVERLAY_1).setSound(GTSoundEvents.MINER)
                        .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressWidget.MoveType.HORIZONTAL);

        UNIVERSAL_CRYSTALIZER_RECIPES = new RecipeMap<>("universal_crystallizer", 9, 1, 1, 0, new SimpleRecipeBuilder(),
                !(oldMultis() || LabsModeHelper.isExpert()))
                        .setSlotOverlay(true, false, GuiTextures.CRYSTAL_OVERLAY).setSound(GTSoundEvents.COMPUTATION)
                        .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, ProgressWidget.MoveType.HORIZONTAL);

        if (Loader.isModLoaded(LabsValues.DME_MODID))
            DME_SIM_CHAMBER_RECIPES = new RecipeMap<>("dme_sim_chamber", 2, 2, 0, 0,
                    new DMESimChamberRecipeMapBuilder(),
                    !(oldMultis() || LabsModeHelper.isNormal()))
                            .setSlotOverlay(false, false, GuiTextures.RESEARCH_STATION_OVERLAY)
                            .setSlotOverlay(true, false, GuiTextures.RESEARCH_STATION_OVERLAY)
                            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT_ASSEMBLER,
                                    ProgressWidget.MoveType.VERTICAL)
                            .setSound(GTSoundEvents.COMPUTATION);

        GROWTH_CHAMBER_RECIPES = new RecipeMap<>("growth_chamber", 4, 9, 1, 0, new SimpleRecipeBuilder(), !newMultis())
                .setSlotOverlay(false, false, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(false, true, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(true, false, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(true, true, GuiTextures.SCANNER_OVERLAY)
                .setSound(GTSoundEvents.SAW_TOOL);

        ECOSYSTEM_EMULATOR_RECIPES = new RecipeMap<>("ecosystem_emulator", 9, 16, 4, 0, new SimpleRecipeBuilder(),
                !newMultis())
                        .setSlotOverlay(false, false, GuiTextures.SCANNER_OVERLAY)
                        .setSlotOverlay(false, true, GuiTextures.SCANNER_OVERLAY)
                        .setSlotOverlay(true, false, GuiTextures.SCANNER_OVERLAY)
                        .setSlotOverlay(true, true, GuiTextures.SCANNER_OVERLAY)
                        .setSound(GTSoundEvents.SAW_TOOL);
    }

    private static RecipeMap<SimpleRecipeBuilder> createMicroverseRecipeMap(int tier) {
        return new DownExpandingRecipeMap("microverse_projector_" + tier, tier == 3 ? 9 : 4, tier == 1 ? 20 : 16,
                tier == 1 ? 1 : 0, 0, new SimpleRecipeBuilder(), !oldMultis())
                        .setProgressBar(LabsTextures.PROGRESS_BAR_ROCKET, ProgressWidget.MoveType.HORIZONTAL)
                        .setSound(LabsSounds.MICROVERSE)
                        .setSlotOverlay(false, false, GuiTextures.IMPLOSION_OVERLAY_1);
    }

    private static RecipeMap<FuelRecipeBuilder> createNaqRecipeMap(int tier) {
        return new RecipeMap<>("naquadah_reactor_" + tier, 1, 1, 0, 0, new FuelRecipeBuilder(), !oldMultis())
                .setSlotOverlay(false, false, GuiTextures.ATOMIC_OVERLAY_1)
                .setSlotOverlay(true, true, GuiTextures.ATOMIC_OVERLAY_1)
                .setSound(GTSoundEvents.TURBINE);
    }

    public static boolean oldMultis() {
        return LabsConfig.content.gtCustomContent.enableOldMultiblocks;
    }

    public static boolean newMultis() {
        return LabsConfig.content.gtCustomContent.enableNewMultiblocks;
    }
}
