package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMultiblockInfoRecipeWrapper;

import gregtech.api.gui.GuiTextures;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.gui.recipes.RecipeLayout;

public class BaseChamberWrapper extends MultiblockInfoRecipeWrapper {

    private static final int ICON_SIZE = 20;
    private static final int RIGHT_PADDING = 5;

    private IDrawable newIcon;
    private final GuiButton buttonPreviousSpecial;
    private final GuiButton buttonNextSpecial;;

    private static MultiblockInfoRecipeWrapper labsLastWrapper;

    public BaseChamberWrapper(@NotNull MetaTileEntityBaseChamber controller, int numBase, int numSpecial) {
        super(controller);

        buttonPreviousSpecial = new GuiButton(0, 176 - ((2 * ICON_SIZE) + RIGHT_PADDING + 1), 90, ICON_SIZE,
                ICON_SIZE, "<");
        buttonNextSpecial = new GuiButton(0, 176 - (ICON_SIZE + RIGHT_PADDING), 90, ICON_SIZE, ICON_SIZE, ">");

        updateBaseButtonState(numBase);
        updateSpecialButtonState(numBase, numSpecial);

        getAccessibleVer().getButtons().put(getAccessibleVer().getButtonPreviousPattern(), () -> {
            getAccessibleVer().callSwitchRenderPage(-1);
            updateBaseButtonState(numBase);
        });
        getAccessibleVer().getButtons().put(getAccessibleVer().getButtonNextPattern(), () -> {
            getAccessibleVer().callSwitchRenderPage(1);
            updateBaseButtonState(numBase);
        });

        getAccessibleVer().getButtons().put(buttonPreviousSpecial, () -> {
            getAccessibleVer().callSwitchRenderPage(-numBase);
            updateSpecialButtonState(numBase, numSpecial);
        });
        getAccessibleVer().getButtons().put(buttonNextSpecial, () -> {
            getAccessibleVer().callSwitchRenderPage(numBase);
            updateSpecialButtonState(numBase, numSpecial);
        });

        getAccessibleVer().getButtonPreviousPattern().y -= ICON_SIZE;
        getAccessibleVer().getButtonNextPattern().y -= ICON_SIZE;
        getAccessibleVer().getNextLayerButton().y -= ICON_SIZE;
    }

    public void updateBaseButtonState(int numBase) {
        getAccessibleVer().getButtonPreviousPattern().enabled = getAccessibleVer().getCurrentRendererPage() % numBase >
                0;
        getAccessibleVer().getButtonNextPattern().enabled = getAccessibleVer().getCurrentRendererPage() % numBase <
                numBase - 1;
    }

    public void updateSpecialButtonState(int numBase, int numSpecial) {
        buttonPreviousSpecial.enabled = getAccessibleVer().getCurrentRendererPage() / numBase > 0;
        buttonNextSpecial.enabled = getAccessibleVer().getCurrentRendererPage() / numBase < numSpecial - 1;
    }

    @Override
    public void setRecipeLayout(RecipeLayout layout, IGuiHelper guiHelper) {
        super.setRecipeLayout(layout, guiHelper);

        getAccessibleVer().setInfoIconToNone();
        newIcon = guiHelper.drawableBuilder(GuiTextures.INFO_ICON.imageLocation, 0, 0, ICON_SIZE, ICON_SIZE)
                .setTextureSize(ICON_SIZE, ICON_SIZE).build();

        IDrawable border = layout.getRecipeCategory().getBackground();
        if (Mouse.getEventDWheel() == 0 || labsLastWrapper != this) {
            labsLastWrapper = this;
            buttonPreviousSpecial.x = border.getWidth() - ((2 * ICON_SIZE) + RIGHT_PADDING + 1);
            buttonNextSpecial.x = border.getWidth() - (ICON_SIZE + RIGHT_PADDING);
        }
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int iconX = recipeWidth - (ICON_SIZE + RIGHT_PADDING);
        int iconY = 49 - ICON_SIZE;
        newIcon.draw(minecraft, iconX, iconY);

        getAccessibleVer().setShouldDrawInfo(iconX <= mouseX && mouseX <= iconX + ICON_SIZE && iconY <= mouseY &&
                mouseY <= iconY + ICON_SIZE);
    }

    public AccessibleMultiblockInfoRecipeWrapper getAccessibleVer() {
        return (AccessibleMultiblockInfoRecipeWrapper) this;
    }
}
