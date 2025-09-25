package com.nomiceu.nomilabs.tooltip;

import java.util.List;

import org.lwjgl.input.Keyboard;

public class LabsTooltipHelper {

    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    /**
     * Tries to remove a range of lines from the tooltip.
     * 
     * @param tooltip Tooltip to modify.
     * @param from    Index to begin, inclusive. If it is negative, nothing happens. If the tooltip size is less than
     *                this, nothing happens.
     * @param to      Index to end, exclusive. If the tooltip size is less than this, this is set to the tooltip size.
     */
    public static void tryRemove(List<String> tooltip, int from, int to) {
        if (from < 0) return;

        if (tooltip.size() <= from) return;
        if (tooltip.size() < to) to = tooltip.size();

        tooltip.subList(from, to).clear();
    }
}
