package com.nomiceu.nomilabs.integration.betterquesting;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.mixinhelper.GuiCustomConfirmOpenLink;
import com.nomiceu.nomilabs.util.LabsTranslate;

import betterquesting.api2.client.gui.controls.io.ValueFuncIO;
import betterquesting.api2.client.gui.resources.colors.GuiColorStatic;
import betterquesting.api2.client.gui.resources.colors.GuiColorTransition;
import betterquesting.api2.client.gui.resources.colors.IGuiColor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
public abstract class InteractiveState {

    protected final List<RenderDescriptionPart.RenderDescriptionInteractive> parts;

    public InteractiveState() {
        parts = new ObjectArrayList<>();
    }

    public void addPart(RenderDescriptionPart.RenderDescriptionInteractive part) {
        parts.add(part);
    }

    public void clearParts() {
        parts.clear();
    }

    public abstract String format();

    public abstract GuiColorTransition color();

    public abstract List<String> tooltip(boolean mouseOver);

    public abstract void handleMouseOver(boolean mouseOver);

    public abstract void handleMouseClick(GuiScreen parent);

    public static class InteractiveCopy extends InteractiveState {

        public static final GuiColorStatic DEFAULT_COLOR = new GuiColorStatic(0x98FB98);
        public static final GuiColorStatic OVER_COLOR = new GuiColorStatic(0xff55ff);
        public static final float TRANSITION_TIME = 150.0F;
        public static final int DISPLAY_COPIED_TIME = 1000;

        protected final GuiColorTransition color;

        protected final boolean disabled;
        protected final String toCopy;

        protected long lastMouseTransitionUpdate;
        protected float mouseTransition = 0.0F;
        protected boolean mouseOver = false;

        protected long lastCopy = 0;

        public InteractiveCopy(String toCopy, boolean disabled) {
            this(toCopy, disabled, DEFAULT_COLOR, OVER_COLOR);
        }

        protected InteractiveCopy(String toCopy, boolean disabled, IGuiColor defaultColor, IGuiColor overColor) {
            super();
            this.disabled = disabled;
            this.toCopy = toCopy;

            this.lastMouseTransitionUpdate = System.currentTimeMillis();

            this.color = new GuiColorTransition(overColor, defaultColor);
            this.color.setupBlending(true, 1.0F).setBlendDriver(new ValueFuncIO<>(() -> mouseTransition));
        }

        @Override
        public void handleMouseOver(boolean mouseOver) {
            if (disabled) return;

            this.mouseOver = mouseOver;

            long mills = System.currentTimeMillis();
            if (mouseOver && mouseTransition < 1.0F) {
                mouseTransition += (mills - lastMouseTransitionUpdate) / TRANSITION_TIME;
                mouseTransition = Math.min(mouseTransition, 1.0F);
            } else if (mouseTransition > 0.0F) {
                mouseTransition -= (mills - lastMouseTransitionUpdate) / TRANSITION_TIME;
                mouseTransition = Math.max(mouseTransition, 0.0F);
            }
            lastMouseTransitionUpdate = mills;
        }

        @Override
        public void handleMouseClick(GuiScreen parent) {
            if (disabled) return;

            GuiScreen.setClipboardString(toCopy);
            lastCopy = System.currentTimeMillis();
        }

        @Override
        public String format() {
            return TextFormatting.ITALIC.toString();
        }

        @Override
        public GuiColorTransition color() {
            return color;
        }

        @Override
        public List<String> tooltip(boolean mouseOver) {
            if (disabled) return null;

            if (System.currentTimeMillis() - lastCopy < DISPLAY_COPIED_TIME)
                return ImmutableList.of(LabsTranslate.translate("nomilabs.gui.bqu.custom.copied.tooltip"));

            if (mouseOver)
                return ImmutableList.of(LabsTranslate.translate("nomilabs.gui.bqu.custom.copy.tooltip"));

            return null;
        }
    }

    public static class InteractiveLink extends InteractiveCopy {

        public static final GuiColorStatic DEFAULT_COLOR = new GuiColorStatic(0x87ceeb);

        public InteractiveLink(String toCopy, boolean disabled) {
            super(toCopy, disabled, DEFAULT_COLOR, OVER_COLOR);
        }

        @Override
        public String format() {
            return TextFormatting.UNDERLINE.toString();
        }

        @Override
        public void handleMouseClick(GuiScreen parent) {
            if (disabled) return;

            Minecraft.getMinecraft().displayGuiScreen(new GuiCustomConfirmOpenLink(parent, toCopy, null));
        }

        @Override
        public List<String> tooltip(boolean mouseOver) {
            if (disabled) return null;

            if (mouseOver)
                return ImmutableList.of(LabsTranslate.translate("nomilabs.gui.bqu.custom.link.tooltip"));

            return null;
        }
    }
}
