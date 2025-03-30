package com.nomiceu.nomilabs.tooltip;

import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;

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

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsTooltipHelper {

    private static final Map<ItemMeta, List<Translatable>> TOOLTIPS = new Object2ObjectOpenHashMap<>();

    public static Translatable DRAWER_UPDGRADE = LabsTranslate.translatable("tooltip.nomilabs.drawers.upgrades");

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
    public static void addTooltip(ItemMeta itemMeta, List<LabsTranslate.Translatable> tr) {
        if (TOOLTIPS.containsKey(itemMeta)) TOOLTIPS.get(itemMeta).addAll(tr);
        else TOOLTIPS.put(itemMeta, tr);
    }

    public static void clearAll() {
        TOOLTIPS.clear();
    }

    @Nullable
    public static List<String> getTranslatableFromStack(ItemStack stack) {
        if (stack.isEmpty()) return null;
        ItemMeta itemMeta = new ItemMeta(stack);
        if (!TOOLTIPS.containsKey(itemMeta)) return null;

        return TOOLTIPS.get(itemMeta).stream().map(LabsTranslate.Translatable::translate)
                .collect(Collectors.toList());
    }
}
