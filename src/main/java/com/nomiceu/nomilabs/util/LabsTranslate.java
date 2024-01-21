package com.nomiceu.nomilabs.util;

import gregtech.client.utils.TooltipHelper;
import net.minecraft.util.text.TextFormatting;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@SuppressWarnings("unused")
public class LabsTranslate {
    public static String translate(String key, Object... params) {
        return translateWithBackup(key, key, params);
    }

    public static String translateWithBackup(String key, String backup, Object... params) {
        if (LabsSide.isDedicatedServer()) return backup; // I18n is not available on Dedicated Servers
        // Try Translating Indirectly
        try {
            return (String) Class.forName("net.minecraft.client.resources.I18n")
                    .getMethod("format", String.class, Object[].class)
                    .invoke(null, key, params);
        } catch (ClassNotFoundException | NoSuchMethodException
                 | InvocationTargetException | IllegalAccessException | ClassCastException e) {
            return backup;
        }
    }

    // Only GT Format Code version is available.
    // If need Text Formatting format, place it in lang.

    public static String translateFormat(String key, TooltipHelper.GTFormatCode format, Object... params) {
        return format(translate(key, params), format);
    }

    public static String translateWithBackupFormat(String key, String backup, TooltipHelper.GTFormatCode format, Object... params) {
        return format(translateWithBackup(key, backup, params), format);
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
