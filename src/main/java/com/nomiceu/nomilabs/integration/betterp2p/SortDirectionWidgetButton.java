package com.nomiceu.nomilabs.integration.betterp2p;

import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;
import static com.nomiceu.nomilabs.util.LabsTranslate.translatable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.LabsTextures;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetButton;

public class SortDirectionWidgetButton extends WidgetButton {

    private static final Translatable[] hoverText = new Translatable[] {
            translatable("nomilabs.gui.advanced_memory_card.sort_direction.normal"),
            translatable("nomilabs.gui.advanced_memory_card.sort_direction.reversed")
    };

    public SortDirectionWidgetButton(@NotNull GuiAdvancedMemoryCard gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);

        setHoverText(ImmutableList.of(hoverText[getAccessibleGui().labs$getSortReversed() ? 1 : 0].translate()));
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button) {
        if (!canPress(mouseX, mouseY)) return false;

        getAccessibleGui().labs$swapSortReversed();
        setHoverText(ImmutableList.of(hoverText[getAccessibleGui().labs$getSortReversed() ? 1 : 0].translate()));
        return true;
    }

    @Override
    public void draw(@NotNull Minecraft mc, int mouseX, int mouseY, float partial) {
        var tessellator = Tessellator.getInstance();

        drawBG(tessellator, mouseX, mouseY, partial);

        // Button Icon
        LabsTextures.P2P_SORTING_ICONS[getAccessibleGui().labs$getSortReversed() ? 4 : 3]
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
