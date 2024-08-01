package com.nomiceu.nomilabs.gregtech.recipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipes.recipeproperties.RecipeProperty;

public class DMEDataProperty extends RecipeProperty<DMEDataPropertyData> {

    public static final String KEY = "dmeData";

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
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {}
}
