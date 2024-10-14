package com.nomiceu.nomilabs.tooltip;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsTooltipHelper {

    private static final Set<ItemMeta> CLEARED = new ObjectOpenHashSet<>();

    private static final Map<ItemMeta, List<LabsTranslate.Translatable>> TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ItemMeta, List<String>> CACHED_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<Pair<ItemMeta, ItemMeta>, String> CACHED_NBT_WARNINGS = new Object2ObjectOpenHashMap<>();

    public static String DRAWER_UPDGRADE = LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades");

    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to add a tooltip in Labs, add a function to {@link TooltipAdder}.
     */
    public static void clearTooltip(ItemMeta itemMeta) {
        CLEARED.add(itemMeta);
    }

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to add a tooltip in Labs, add a function to {@link TooltipAdder}.
     */
    public static void addTooltip(ItemMeta itemMeta, List<LabsTranslate.Translatable> tr) {
        if (TOOLTIPS.containsKey(itemMeta)) TOOLTIPS.get(itemMeta).addAll(tr);
        else TOOLTIPS.put(itemMeta, tr);
    }

    public static void clearAll() {
        CLEARED.clear();
        TOOLTIPS.clear();
        CACHED_TOOLTIPS.clear();
        CACHED_NBT_WARNINGS.clear();
    }

    public static void onLanguageChange() {
        CACHED_TOOLTIPS.clear();
        CACHED_NBT_WARNINGS.clear();
        DRAWER_UPDGRADE = LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades");
    }

    public static boolean shouldClear(ItemStack stack) {
        return CLEARED.contains(new ItemMeta(stack));
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

    public static String getTranslatedNBTClearer(ItemMeta outputItemMeta,
                                                 ItemMeta inputItemMeta,
                                                 LabsTranslate.Translatable tr) {
        if (CACHED_NBT_WARNINGS.containsKey(Pair.of(outputItemMeta, inputItemMeta)))
            return CACHED_NBT_WARNINGS.get(Pair.of(outputItemMeta, inputItemMeta));

        var translated = tr.translate();

        CACHED_NBT_WARNINGS.put(Pair.of(outputItemMeta, inputItemMeta), translated);
        return translated;
    }
}
