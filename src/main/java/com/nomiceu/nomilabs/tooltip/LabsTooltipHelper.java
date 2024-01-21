package com.nomiceu.nomilabs.tooltip;

import mcjty.theoneprobe.api.IProbeInfo;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class LabsTooltipHelper {
    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static String getTOPFormat(String str) {
        return IProbeInfo.STARTLOC + str + IProbeInfo.ENDLOC;
    }
}
