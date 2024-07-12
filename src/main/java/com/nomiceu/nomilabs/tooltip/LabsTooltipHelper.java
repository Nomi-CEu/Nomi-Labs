package com.nomiceu.nomilabs.tooltip;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mcjty.theoneprobe.api.IProbeInfo;

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsTooltipHelper {

    private static final Map<ItemMeta, List<LabsTranslate.Translatable>> TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ItemMeta, List<String>> CACHED_TOOLTIPS = new Object2ObjectOpenHashMap<>();

    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static String getTOPFormat(String str) {
        return IProbeInfo.STARTLOC + str + IProbeInfo.ENDLOC;
    }

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to add a tooltip in Labs, add a function to {@link TooltipAdder}.
     */
    public static void addTooltip(ItemMeta itemMeta, List<LabsTranslate.Translatable> tr) {
        if (TOOLTIPS.containsKey(itemMeta)) TOOLTIPS.get(itemMeta).addAll(tr);
        TOOLTIPS.put(itemMeta, tr);
    }

    public static void clearAll() {
        TOOLTIPS.clear();
        CACHED_TOOLTIPS.clear();
    }

    public static void onLanguageChange() {
        CACHED_TOOLTIPS.clear();
    }

    @Nullable
    public static List<String> getTranslatableFromStack(ItemStack stack) {
        if (stack.isEmpty()) return null;
        ItemMeta itemMeta = new ItemMeta(stack);
        if (!TOOLTIPS.containsKey(itemMeta)) return null;

        if (CACHED_TOOLTIPS.containsKey(itemMeta)) return CACHED_TOOLTIPS.get(itemMeta);

        CACHED_TOOLTIPS.put(itemMeta,
                TOOLTIPS.get(itemMeta).stream().map(LabsTranslate.Translatable::translate)
                        .collect(Collectors.toList()));
        return CACHED_TOOLTIPS.get(itemMeta);
    }
}
