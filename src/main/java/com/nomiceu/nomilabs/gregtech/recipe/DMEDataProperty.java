package com.nomiceu.nomilabs.gregtech.recipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipes.recipeproperties.RecipeProperty;

public class DMEDataProperty extends RecipeProperty<DMEDataPropertyData> {

    public static final String KEY = "research";

    private static DMEDataProperty INSTANCE;

    private DMEDataProperty() {
        super(KEY, DMEDataPropertyData.class);
    }

    @NotNull
    public static DMEDataProperty getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DMEDataProperty();
        }
        return INSTANCE;
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int x, int y, int color, Object value) {
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.research"), x, y, color);
    }
}
