package com.nomiceu.nomilabs.integration.betterp2p;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.LabsTextures;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetButton;

public class SortWidgetButton extends WidgetButton {

    public SortWidgetButton(@NotNull GuiAdvancedMemoryCard gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);

        setHoverText(ImmutableList.of(getAccessibleGui().labs$getSortMode().getName()));
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button) {
        if (!canPress(mouseX, mouseY)) return false;

        getAccessibleGui().labs$changeSort(button == 0);
        setHoverText(ImmutableList.of(getAccessibleGui().labs$getSortMode().getName()));
        return true;
    }

    @Override
    public void draw(@NotNull Minecraft mc, int mouseX, int mouseY, float partial) {
        var tessellator = Tessellator.getInstance();

        drawBG(tessellator, mouseX, mouseY, partial);

        // Button Icon
        LabsTextures.P2P_SORTING_MODES[getAccessibleGui().labs$getSortMode().ordinal()]
                .draw(x + 1.0, y + 1.0, width - 2, height - 2);
    }

    // From GuiButton#mousePressed
    private boolean canPress(int mouseX, int mouseY) {
        return enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    private AccessibleGuiAdvancedMemoryCard getAccessibleGui() {
        return ((AccessibleGuiAdvancedMemoryCard) (Object) getGui());
    }
}
