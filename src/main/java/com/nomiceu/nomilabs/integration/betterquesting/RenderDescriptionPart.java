package com.nomiceu.nomilabs.integration.betterquesting;

import static com.nomiceu.nomilabs.integration.betterquesting.PanelDescription.fontRenderer;

import betterquesting.api2.client.gui.resources.colors.GuiColorStatic;

/**
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
public abstract class RenderDescriptionPart {

    protected final int width;
    protected final int x;
    protected final int y;

    protected RenderDescriptionPart(int width, int x, int y) {
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public int height() {
        return fontRenderer().FONT_HEIGHT;
    }

    public int width() {
        return width;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public abstract void render();

    public interface InteractivableRender {

        boolean isMouseOver(int mx, int my);
    }

    public static class RenderDescriptionText extends RenderDescriptionPart {

        public static final GuiColorStatic DEFAULT_COLOR = new GuiColorStatic(0xFFFFFF);

        protected final String text;

        public RenderDescriptionText(String text, int width, int x, int y) {
            super(width, x, y);
            this.text = text;
        }

        @Override
        public void render() {
            fontRenderer().drawString(text, x, y, DEFAULT_COLOR.getRGB());
        }
    }

    public static class RenderDescriptionInteractive extends RenderDescriptionPart implements InteractivableRender {

        protected final String text;
        protected final InteractiveState interactive;

        public RenderDescriptionInteractive(String text, int width, int x, int y, InteractiveState interactive) {
            super(width, x, y);

            this.text = text;
            this.interactive = interactive;

            this.interactive.addPart(this);
        }

        @Override
        public int height() {
            return super.height() + 1;
        }

        @Override
        public void render() {
            fontRenderer().drawStringWithShadow(text, x, y, interactive.color().getRGB());
        }

        @Override
        public boolean isMouseOver(int mx, int my) {
            return mx >= x && mx <= x + width && my >= y && my <= y + fontRenderer().FONT_HEIGHT;
        }
    }
}
