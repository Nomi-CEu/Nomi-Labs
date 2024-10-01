package com.nomiceu.nomilabs.gregtech.mixinhelper;

public interface AccessibleRecipeLogic {

    boolean labs$outputFull();

    boolean labs$recipeVoltageTooHigh();

    boolean labs$requiresCleanroom();

    boolean labs$cleanroomDirty();

    boolean labs$wrongCleanroom();

    void labs$invalidate();
}
