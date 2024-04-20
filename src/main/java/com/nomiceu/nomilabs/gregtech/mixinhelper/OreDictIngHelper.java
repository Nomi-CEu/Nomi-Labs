package com.nomiceu.nomilabs.gregtech.mixinhelper;

public class OreDictIngHelper {
    // No way GS Reload or other moments where JEI reloads happens > 9 Quintillion Times... right?
    // Overkill just in case.
    private static long STANDARD = 0;

    public static void incStandard() {
        STANDARD++;
    }

    public static long getStandard() {
        return STANDARD;
    }
}
