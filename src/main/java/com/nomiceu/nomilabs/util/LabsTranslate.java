package com.nomiceu.nomilabs.util;

import gregtech.client.utils.TooltipHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.Arrays;
import java.util.IllegalFormatException;

@SuppressWarnings("unused")
public class LabsTranslate {
    public static String translate(String key, Object... params) {
        if (LabsSide.isDedicatedServer()) return translateServerSide(key, params); // I18n is not available on Dedicated Servers
        try {
            return net.minecraft.client.resources.I18n.format(key, params);
        } catch (Exception e) {
            return translateServerSide(key, params);
        }
    }

    @SuppressWarnings("deprecation")
    private static String translateServerSide(String key, Object... params) {
        try {
            var localTranslated = I18n.translateToLocalFormatted(key, params);
            if (!localTranslated.equals(key)) return localTranslated;

            // Try fallback
            var fallbackTranslated = I18n.translateToFallback(key);
            if (!fallbackTranslated.equals(key) && params.length != 0) {
                try {
                     fallbackTranslated = String.format(fallbackTranslated, params);
                }
                catch (IllegalFormatException var5) {
                    fallbackTranslated = "Format error: " + fallbackTranslated;
                }
            }
            return fallbackTranslated;
        } catch (Exception e) {
            return key;
        }
    }

    // Only GT Format Code version is available.
    // If need Text Formatting format, place it in lang.

    public static String translateFormat(String key, TooltipHelper.GTFormatCode format, Object... params) {
        return format(translate(key, params), format);
    }

    public static String format(String str, TextFormatting... formats) {
        return String.join("", Arrays.stream(formats).map(TextFormatting::toString).toArray(String[]::new))
                + str + TextFormatting.RESET;
    }

    public static String format(String str, TooltipHelper.GTFormatCode... formats) {
        return String.join("", Arrays.stream(formats).map(TooltipHelper.GTFormatCode::toString).toArray(String[]::new))
                + str + TextFormatting.RESET;
    }

    public static String format(String str, Format... formats) {
        return String.join("", Arrays.stream(formats).map((format) -> format.format).toArray(String[]::new))
                + str + TextFormatting.RESET;
    }

    public static class Format {
        public final String format;
        private Format(String format) {
            this.format = format;
        }
        public Format of(TextFormatting format) {
            return new Format(format.toString());
        }
        public Format of(TooltipHelper.GTFormatCode format) {
            return new Format(format.toString());
        }
    }
}
