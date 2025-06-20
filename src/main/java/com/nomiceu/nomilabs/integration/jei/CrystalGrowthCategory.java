package com.nomiceu.nomilabs.integration.jei;

import net.bdew.ae2stuff.AE2StuffTags;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.util.LabsNames;
import com.nomiceu.nomilabs.util.LabsTranslate;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

public class CrystalGrowthCategory implements IRecipeCategory<CrystalGrowthRecipeWrapper> {

    public static final String UID = LabsValues.AE2_STUFF_MODID + ":crystal_growth_chamber";

    // Fill the middle slot first
    private static final int SLOT_INPUT_MIDDLE = 0;
    private static final int SLOT_INPUT_TOP = 1;
    private static final int SLOT_INPUT_BOTTOM = 2;
    private static final int SLOT_OUTPUT = 3;

    private static final ResourceLocation GUI_TEXTURE = LabsNames
            .makeLabsName("textures/gui/crystal_growth_chamber/jei.png");

    private final IDrawable background;
    private final IDrawable progress;

    public CrystalGrowthCategory(IGuiHelper gui) {
        background = gui.drawableBuilder(GUI_TEXTURE, 0, 0, 97, 64).setTextureSize(97, 128).build();
        progress = gui.drawableBuilder(GUI_TEXTURE, 0, 64, 50, 50).setTextureSize(97, 128)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    @NotNull
    public String getUid() {
        return UID;
    }

    @Override
    @NotNull
    public String getTitle() {
        return LabsTranslate.translate("tile.ae2stuff.grower.name");
    }

    @Override
    @NotNull
    public String getModName() {
        return AE2StuffTags.MODNAME;
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        progress.draw(minecraft, 18, 7);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull CrystalGrowthRecipeWrapper recipeWrapper,
                          @NotNull IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(SLOT_INPUT_MIDDLE, true, 0, 23);
        itemStacks.init(SLOT_INPUT_TOP, true, 0, 0);
        itemStacks.init(SLOT_INPUT_BOTTOM, true, 0, 46);
        itemStacks.init(SLOT_OUTPUT, false, 75, 23);

        itemStacks.set(ingredients);
    }
}
