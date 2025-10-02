package com.nomiceu.nomilabs.integration.jei.recipe;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.jei.recipe.ChargerRecipeHandler.ChargerRecipe;
import com.nomiceu.nomilabs.util.LabsNames;
import com.nomiceu.nomilabs.util.LabsTranslate;

import appeng.core.AppEng;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

public class ChargerCategory implements IRecipeCategory<ChargerRecipe> {

    public static final String UID = LabsValues.AE2_MODID + ":charger";

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;

    private static final ResourceLocation GUI_TEXTURE = LabsNames
            .makeLabsName("textures/gui/charger/jei.png");

    private final IDrawable background;
    private final IDrawable progress;

    public ChargerCategory(IGuiHelper gui) {
        background = gui.drawableBuilder(GUI_TEXTURE, 0, 0, 97, 25).setTextureSize(97, 50).build();
        progress = gui.drawableBuilder(GUI_TEXTURE, 0, 25, 50, 25).setTextureSize(97, 50)
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
        return LabsTranslate.translate("tile.appliedenergistics2.charger.name");
    }

    @Override
    @NotNull
    public String getModName() {
        return AppEng.MOD_NAME;
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        progress.draw(minecraft, 18, 4);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull ChargerRecipe recipeWrapper,
                          @NotNull IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(SLOT_INPUT, true, 0, 4);
        itemStacks.init(SLOT_OUTPUT, false, 75, 4);

        itemStacks.set(ingredients);
    }
}
