package com.nomiceu.nomilabs.tooltip;

import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsTooltipHelper {

    private static final Map<ItemMeta, Integer> REMOVE = new Object2ObjectOpenHashMap<>();
    private static final Map<ItemMeta, List<Translatable>> TOOLTIPS = new Object2ObjectOpenHashMap<>();

    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to add a tooltip in Labs, add a function to {@link TooltipChanger}.
     */
    public static void addTooltip(ItemMeta itemMeta, List<LabsTranslate.Translatable> tr) {
        if (TOOLTIPS.containsKey(itemMeta)) TOOLTIPS.get(itemMeta).addAll(tr);
        else TOOLTIPS.put(itemMeta, tr);
    }

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to remove parts of tooltips in Labs, add a function to {@link TooltipChanger}.
     */
    public static void removeTooltip(ItemMeta itemMeta, int amt) {
        REMOVE.put(itemMeta, amt);
    }

    public static void clearAll() {
        TOOLTIPS.clear();
        REMOVE.clear();
    }

    public static void modifyTooltip(List<String> tooltip, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemMeta itemMeta = new ItemMeta(stack);

        if (REMOVE.containsKey(itemMeta)) {
            int amt = REMOVE.get(itemMeta);

            // Amt < 0 is our shorthand for remove all
            if (amt < 0 || amt > tooltip.size() - 1)
                amt = tooltip.size() - 1; // Don't remove the item name

            // If advanced tooltips are enabled, we need to ignore one, because of resource location printing
            if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
                tooltip.subList(tooltip.size() - amt - 1, tooltip.size() - 1).clear();
            else
                tooltip.subList(tooltip.size() - amt, tooltip.size()).clear();
        }

        if (!TOOLTIPS.containsKey(itemMeta)) return;

        tooltip.addAll(TOOLTIPS.get(itemMeta).stream().map(LabsTranslate.Translatable::translate)
                .collect(Collectors.toList()));
    }
}
