package com.nomiceu.nomilabs.groovy;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.groovy.mixinhelper.AccessibleKeyBinding;
import com.nomiceu.nomilabs.mixin.KeyBindingAccessor;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;
import com.nomiceu.nomilabs.util.LabsTranslate;

@GroovyBlacklist
public class KeyBindingHelper {

    public static void drawKeybindingTooltip(int mouseX, int mouseY, FontRenderer fontRenderer, KeyBinding keybinding) {
        fontRenderer.drawString(LabsTranslate.translate(keybinding.getKeyCategory()), mouseX + 10, mouseY, 0xFFFFFF);
        var spacing = fontRenderer.FONT_HEIGHT;
        var currentSpacing = spacing;
        if (LabsTooltipHelper.isShiftDown() && LabsConfig.advanced.controlMenuTooltipSettings.showID) {
            fontRenderer.drawString(keybinding.getKeyDescription(), mouseX + 10, mouseY + spacing, 0x808080);
            currentSpacing += spacing;
        }
        if (LabsTooltipHelper.isCtrlDown() && LabsConfig.advanced.controlMenuTooltipSettings.showClass)
            fontRenderer.drawString(keybinding.getClass().getName(), mouseX + 10, mouseY + currentSpacing, 0x505050);
    }

    public static void addKeybindOverride(String id, KeyModifier modifier, int keyCode) {
        if (!KeyBindingAccessor.getKeybindRegistry().containsKey(id)) {
            LabsGroovyHelper
                    .throwOrGroovyLog(new IllegalArgumentException("Keybind with ID " + id + " was not found!"));
            NomiLabs.LOGGER.info("Available Keybind IDs: {}", KeyBindingAccessor.getKeybindRegistry().keySet());
            GroovyLog.get().info("Available Keybind IDs: " + KeyBindingAccessor.getKeybindRegistry().keySet());
            return;
        }
        ((AccessibleKeyBinding) KeyBindingAccessor.getKeybindRegistry().get(id))
                .setDefaultKeyModifierAndCode(modifier, keyCode);
    }
}
