package com.nomiceu.nomilabs.tooltip;

import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class LabsTooltipHelper {
    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static class Tooltip {
        protected static final String defaultFormat = TextFormatting.WHITE.toString();

        protected final String key;
        protected final Object[] params;
        protected String format;
        protected String topFormat;
        protected boolean noTop;

        protected Tooltip(String key, Object[] params) {
            this.key = key;
            this.params = params;
            this.noTop = false;
        }

        /**
         * Makes a tooltip with the given translation key, with the specified params to input in {@link I18n#format(String, Object...)} with the default formatting (white).
         */
        public static Tooltip of(String key, Object... params) {
            return new Tooltip(key, params);
        }

        /**
         * Specifies a format to use. Also sets the TOP Formatting if it is not already set. If this is not specified, defaults to {@link LabsTooltipHelper.Tooltip#defaultFormat}.
         */
        public Tooltip withFormat(TextFormatting format) {
            this.format = format.toString();
            if (topFormat == null) topFormat = format.toString();
            return this;
        }

        /**
         * Specifies a format to use. Also sets the TOP Formatting if it is not already set. If this is not specified, defaults to {@link LabsTooltipHelper.Tooltip#defaultFormat}.
         */
        public Tooltip withFormat(String format) {
            this.format = format;
            if (topFormat == null) topFormat = format;
            return this;
        }

        /**
         * Specifies a TOP format to use. If this is not specified, defaults to specified formatting.
         */
        public Tooltip withTopFormat(TextFormatting topFormat) {
            this.topFormat = topFormat.toString();
            return this;
        }

        /**
         * Specifies a TOP format to use. If this is not specified, defaults to specified formatting.
         */
        public Tooltip withTopFormat(String topFormat) {
            this.topFormat = topFormat;
            return this;
        }

        /**
         * Makes this not have a TOP message.
         */
        public Tooltip withoutTOP() {
            noTop = true;
            return this;
        }

        /**
         * Gets the Formatted String, for use in Tooltips.
         */
        @SideOnly(Side.CLIENT)
        public String getFormattedString() {
            return (format == null ? defaultFormat : format) + I18n.format(key, params) + TextFormatting.RESET;
        }

        /**
         * Gets the Formatted String, for use in TOP. Returns null if this message should not be displayed on TOP.
         */
        @SideOnly(Side.CLIENT)
        @Nullable
        public String getTOPFormattedString() {
            if (noTop) return null;
            return (topFormat == null ? defaultFormat : topFormat) + IProbeInfo.STARTLOC + I18n.format(key, params) + IProbeInfo.ENDLOC + TextFormatting.RESET;
        }
    }
}
