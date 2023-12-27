package com.nomiceu.nomilabs.tooltip;

import com.enderio.core.client.handlers.SpecialTooltipHandler;
import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import crazypants.enderio.base.capacitor.CapacitorKey;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class TooltipAdder {
    public static void addTooltipNormal(List<String> tooltip, ItemStack stack) {
        // Add Information of EIO Capacitors' Levels
        if (stack.hasCapability(CapabilityCapacitorData.getCapNN(), null)) {
            if (!SpecialTooltipHandler.showAdvancedTooltips()) return;

            var cap = Objects.requireNonNull(stack.getCapability(CapabilityCapacitorData.getCapNN(), null)); // Null shouldn't happen, as hasCapability returned true
            var level = cap.getUnscaledValue(CapacitorKey.NO_POWER);

            var formatter = new DecimalFormat("0.##"); // Format Levels to two decimal places (or less, this also removes trailing zeros)
            formatter.setRoundingMode(RoundingMode.HALF_UP); // Rounds up if in the middle (.5), else rounds to nearest

            // No clue what to use as the capacitor key, using NO_POWER, common declarations, in EnderIO and Nomi-Labs, don't use that parameter anyways
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("tooltip.nomilabs.capacitors.level", formatter.format(level)));
        }
    }
}
