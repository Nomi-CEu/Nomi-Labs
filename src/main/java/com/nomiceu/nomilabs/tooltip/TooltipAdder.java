package com.nomiceu.nomilabs.tooltip;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.enderio.core.client.handlers.SpecialTooltipHandler;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import crazypants.enderio.base.capacitor.CapacitorKey;

@SideOnly(Side.CLIENT)
public class TooltipAdder {

    public static void addTooltipNormal(List<String> tooltip, ItemStack stack) {
        if (LabsTooltipHelper.shouldClear(stack))
            tooltip.clear();

        var groovyTooltips = LabsTooltipHelper.getTranslatableFromStack(stack);
        if (groovyTooltips != null)
            tooltip.addAll(groovyTooltips);

        // Add Information of EIO Capacitors' Levels
        if (Loader.isModLoaded(LabsValues.ENDER_IO_MODID) && LabsConfig.modIntegration.enableEnderIOIntegration)
            addTooltipEIO(tooltip, stack);
    }

    @Optional.Method(modid = LabsValues.ENDER_IO_MODID)
    private static void addTooltipEIO(List<String> tooltip, ItemStack stack) {
        if (stack.hasCapability(CapabilityCapacitorData.getCapNN(), null)) {
            if (!SpecialTooltipHandler.showAdvancedTooltips()) return;

            var cap = Objects.requireNonNull(stack.getCapability(CapabilityCapacitorData.getCapNN(), null)); // Null
                                                                                                             // shouldn't
                                                                                                             // happen,
                                                                                                             // as
                                                                                                             // hasCapability
                                                                                                             // returned
                                                                                                             // true
            var level = cap.getUnscaledValue(CapacitorKey.NO_POWER);

            var formatter = new DecimalFormat("0.##"); // Format Levels to two decimal places (or less, this also
                                                       // removes trailing zeros)
            formatter.setRoundingMode(RoundingMode.HALF_UP); // Rounds up if in the middle (.5), else rounds to nearest

            // No clue what to use as the capacitor key, using NO_POWER, common declarations, in EnderIO and Nomi-Labs,
            // don't use that parameter anyway
            tooltip.add(translate("tooltip.nomilabs.capacitors.level", formatter.format(level)));
        }
    }
}
