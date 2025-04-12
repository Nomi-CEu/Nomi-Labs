package com.nomiceu.nomilabs.integration.betterquesting;

import static com.nomiceu.nomilabs.integration.betterquesting.PanelDescription.fontRenderer;
import static com.nomiceu.nomilabs.integration.betterquesting.RenderDescriptionPart.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.gui.FontRenderer;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
public abstract class DescriptionPart {

    public static final Pattern SPECIAL_MATCH = Pattern
            .compile("<\\{(link|copy)}>(.+?)(?:<\\|>(.+?))?(?:<\\|>(disabled))?<\\{\\1}>");

    public abstract DescriptionPart allAfter(int startIndex);

    public abstract RenderDescriptionPart toRender(int x, int y);

    public abstract RenderDescriptionPart toRender(int x, int y, int end);

    public abstract String toRenderText();

    public static boolean textContainsCustom(String text) {
        return SPECIAL_MATCH.matcher(text).find();
    }

    public static List<DescriptionPart> splitString(String text) {
        List<DescriptionPart> parts = new ObjectArrayList<>();
        Matcher matcher = SPECIAL_MATCH.matcher(text);

        int lastHandled = 0;
        String currFormat = "";

        while (matcher.find()) {
            String normalText = currFormat + text.substring(lastHandled, matcher.start());
            parts.add(new DescriptionText(normalText));

            // The Pattern matches only link or copy, so we can directly assume the other is true if one is not.
            parts.add(new DescriptionInteractive(matcher.group(1), matcher.group(2), matcher.group(3),
                    matcher.group(4) != null));

            // Save New Values
            lastHandled = matcher.end();
            currFormat = FontRenderer.getFormatFromString(normalText);
        }

        String remainingText = text.substring(lastHandled);
        if (!remainingText.isEmpty())
            parts.add(new DescriptionText(currFormat + remainingText));

        return ImmutableList.copyOf(parts);
    }

    public static class DescriptionText extends DescriptionPart {

        protected final String text;

        public DescriptionText(String text) {
            this.text = text;
        }

        @Override
        public DescriptionPart allAfter(int startIndex) {
            String sub = text.substring(startIndex);
            sub = FontRenderer.getFormatFromString(text.substring(0, startIndex)) + sub;
            return new DescriptionText(sub);
        }

        @Override
        public RenderDescriptionPart toRender(int x, int y) {
            return toRender(x, y, text.length());
        }

        @Override
        public RenderDescriptionPart toRender(int x, int y, int end) {
            String sub = StringUtils.stripEnd(text.substring(0, end), "\n\r");
            return new RenderDescriptionText(sub, fontRenderer().getStringWidth(sub), x, y);
        }

        @Override
        public String toRenderText() {
            return text;
        }
    }

    public static class DescriptionInteractive extends DescriptionPart {

        protected final String type;
        protected final String display;
        protected final String content;
        protected final InteractiveState state;

        public DescriptionInteractive(String type, String content, String display, boolean disabled) {
            this.type = type;
            this.content = content;

            this.state = CustomType.valueOf(type.toUpperCase()).toState.toState(content, disabled);

            if (display == null)
                this.display = state.format() + content;
            else
                this.display = state.format() + display;
        }

        private DescriptionInteractive(String type, String content, String display, InteractiveState state) {
            this.type = type;
            this.content = content;
            this.display = state.format() + display;
            this.state = state;
        }

        public InteractiveState getState() {
            return state;
        }

        @Override
        public DescriptionPart allAfter(int startIndex) {
            return new DescriptionInteractive(type, content, display.substring(startIndex), state);
        }

        @Override
        public RenderDescriptionPart toRender(int x, int y) {
            return toRender(x, y, display.length());
        }

        @Override
        public RenderDescriptionPart toRender(int x, int y, int end) {
            String sub = StringUtils.stripEnd(display.substring(0, end), "\n\r");
            return new RenderDescriptionInteractive(sub, fontRenderer().getStringWidth(sub), x, y, state);
        }

        @Override
        public String toRenderText() {
            return display;
        }
    }

    public enum CustomType {

        LINK(InteractiveState.InteractiveLink::new),
        COPY(InteractiveState.InteractiveCopy::new);

        public final TypeToState toState;

        CustomType(TypeToState toState) {
            this.toState = toState;
        }
    }

    @FunctionalInterface
    public interface TypeToState {

        InteractiveState toState(String toCopy, boolean disabled);
    }
}
