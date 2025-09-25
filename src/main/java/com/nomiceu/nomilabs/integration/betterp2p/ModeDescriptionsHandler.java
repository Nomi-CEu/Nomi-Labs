package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nomiceu.nomilabs.mixin.betterp2p.GuiAdvancedMemoryCardKtAccessor;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;
import com.projecturanus.betterp2p.item.BetterMemoryCardModesKt;

public class ModeDescriptionsHandler {

    public static final Map<BetterMemoryCardModes, List<String>> labsModeDescriptions;

    public static void refreshDescriptions() {
        labsModeDescriptions.clear();
        for (var mode : BetterMemoryCardModes.values()) {
            if (mode == BetterMemoryCardModes.UNBIND || LabsBetterMemoryCardModes.LABS_ADDED_MODES.containsKey(mode))
                labsModeDescriptions.put(mode,
                        GuiAdvancedMemoryCardKtAccessor.fmtTooltips(mode.getUnlocalizedName(),
                                mode.getUnlocalizedDesc(),
                                BetterMemoryCardModesKt.MAX_TOOLTIP_LENGTH));
        }

        // Add for Input/Output (Custom)
        labsModeDescriptions.put(BetterMemoryCardModes.INPUT,
                GuiAdvancedMemoryCardKtAccessor.fmtTooltips(BetterMemoryCardModes.INPUT.getUnlocalizedName(),
                        new String[] { "nomilabs.gui.advanced_memory_card.mode.input.desc.1",
                                "nomilabs.gui.advanced_memory_card.mode.input.desc.2",
                                "nomilabs.gui.advanced_memory_card.mode.input.desc.3" },
                        BetterMemoryCardModesKt.MAX_TOOLTIP_LENGTH));

        labsModeDescriptions.put(BetterMemoryCardModes.OUTPUT,
                GuiAdvancedMemoryCardKtAccessor.fmtTooltips(BetterMemoryCardModes.OUTPUT.getUnlocalizedName(),
                        new String[] { "nomilabs.gui.advanced_memory_card.mode.output.desc.1",
                                "nomilabs.gui.advanced_memory_card.mode.output.desc.2",
                                "nomilabs.gui.advanced_memory_card.mode.output.desc.3" },
                        BetterMemoryCardModesKt.MAX_TOOLTIP_LENGTH));
    }

    static {
        labsModeDescriptions = new HashMap<>();
        refreshDescriptions();
    }
}
