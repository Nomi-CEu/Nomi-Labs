package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.Map;

import net.minecraft.client.gui.GuiButton;

public interface AccessibleMultiblockInfoRecipeWrapper {

    GuiButton getButtonPreviousPattern();

    GuiButton getButtonNextPattern();

    GuiButton getNextLayerButton();

    Map<GuiButton, Runnable> getButtons();

    void setInfoIconToNone();

    void setShouldDrawInfo(boolean shouldDrawInfo);

    void callSwitchRenderPage(int amount);

    int getCurrentRendererPage();
}
