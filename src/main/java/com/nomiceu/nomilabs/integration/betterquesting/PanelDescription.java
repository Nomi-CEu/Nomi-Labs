package com.nomiceu.nomilabs.integration.betterquesting;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
public class PanelDescription extends PanelTextBox {

    private final List<DescriptionPart> parts;
    private final List<InteractiveState> interactiveParts;
    private final GuiRectangle refRect;

    private final List<RenderDescriptionPart> renderParts;

    public PanelDescription(IGuiRect rect, String text) {
        super(rect, text, true);

        this.parts = DescriptionPart.splitString(text);

        this.interactiveParts = new ObjectArrayList<>();
        for (var part : parts) {
            if (part instanceof DescriptionPart.DescriptionInteractive interactive) {
                this.interactiveParts.add(interactive.getState());
            }
        }

        this.refRect = new GuiRectangle(0, 0, 0, 0, 0);

        this.renderParts = new ObjectArrayList<>();
    }

    /* Custom Logic */
    public void refreshParts() {
        renderParts.clear();

        // Set New RefRect Values
        refRect.x = getTransform().getX();
        refRect.y = getTransform().getY();
        refRect.w = getTransform().getWidth();

        Deque<DescriptionPart> extractedParts = new ArrayDeque<>();

        for (var interact : interactiveParts) {
            interact.clearParts();
        }

        for (DescriptionPart part : parts) {
            // Add last so FIFO
            extractedParts.addLast(part);
        }

        ((AccessibleGuiRectText) getTransform()).labs$setHeight(addLineOfParts(extractedParts, 0));
        refRect.h = getTransform().getHeight();
    }

    private int addLineOfParts(Deque<DescriptionPart> parts, int yOffset) {
        int widthRemaining = refRect.getWidth();
        int height = fontRenderer().FONT_HEIGHT;

        while (!parts.isEmpty() && widthRemaining > 0) {
            // Peek first so FIFO
            DescriptionPart part = parts.pollFirst();

            String renderText = part.toRenderText();
            int split = fontRenderer().sizeStringToWidth(renderText, widthRemaining);

            if (split >= renderText.length()) {
                RenderDescriptionPart render = part.toRender(refRect.getX() + refRect.getWidth() - widthRemaining,
                        refRect.getY() + yOffset);
                renderParts.add(render);
                widthRemaining -= render.width();
                height = Math.max(height, render.height());
                continue;
            }

            if (split != 0) {
                RenderDescriptionPart render = part.toRender(refRect.getX() + refRect.getWidth() - widthRemaining,
                        refRect.getY() + yOffset, split);
                height = Math.max(height, render.height());
                renderParts.add(render);
            }

            char charAtSplit = renderText.charAt(split);
            if (charAtSplit == '\n' || charAtSplit == ' ') {
                split++;
            }

            if (split < renderText.length()) {
                parts.addFirst(part.allAfter(split));
            }
            break;
        }

        if (!parts.isEmpty())
            return addLineOfParts(parts, yOffset + height);

        return yOffset + height;
    }

    /* We don't need to override original class setting functions, because they are never used for description. */

    // Override Set Text so calculations are not made
    @Override
    public PanelTextBox setText(String text) {
        return this;
    }

    @Override
    public void initPanel() {
        refreshParts();
    }

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        if (!isRectEqual(refRect, getTransform())) {
            refreshParts();
        }

        // Handle Mouse Over
        boolean findMouseOver = refRect.contains(mx, my);
        for (var state : interactiveParts) {
            boolean mouseOver = false;
            if (findMouseOver) {
                // Check to see if any of the state's parts are being hovered over
                for (var part : state.parts) {
                    if (part.isMouseOver(mx, my)) {
                        mouseOver = true;
                        break;
                    }
                }

                // Reassign to findMouseOver, so we can skip mouseOver checks for future parts
                findMouseOver = !mouseOver;
            }
            state.handleMouseOver(mouseOver);
        }

        // Render
        for (var part : renderParts) {
            part.render();
        }
    }

    @Override
    public boolean onMouseClick(int mx, int my, int clickButton) {
        if (!refRect.contains(mx, my) || clickButton != 0) return false;

        for (var state : interactiveParts) {
            boolean mouseOver = false;

            // Check to see if any of the state's parts are being hovered over
            for (var part : state.parts) {
                if (part.isMouseOver(mx, my)) {
                    mouseOver = true;
                    break;
                }
            }

            if (mouseOver) {
                state.handleMouseClick(Minecraft.getMinecraft().currentScreen);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        if (!refRect.contains(mx, my)) return null;

        List<String> result = null;
        boolean findMouseOver = refRect.contains(mx, my);
        for (var state : interactiveParts) {
            boolean mouseOver = false;
            if (findMouseOver) {
                // Check to see if any of the state's parts are being hovered over
                for (var part : state.parts) {
                    if (part.isMouseOver(mx, my)) {
                        mouseOver = true;
                        break;
                    }
                }

                // Reassign to findMouseOver, so we can skip mouseOver checks for future parts
                findMouseOver = !mouseOver;
            }

            var tooltip = state.tooltip(mouseOver);

            if (tooltip != null) {
                // If this tooltip does not require hover, it is very high priority and returned immediately
                if (!mouseOver)
                    return tooltip;

                // This should never override a tooltip, as we cannot have two hover-only tooltips
                result = tooltip;
            }
        }

        return result;
    }

    private boolean isRectEqual(IGuiRect r1, IGuiRect r2) {
        return r1.getX() == r2.getX() && r1.getY() == r2.getY() && r1.getWidth() == r2.getWidth() &&
                r1.getHeight() == r2.getHeight();
    }

    public static FontRenderer fontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }
}
