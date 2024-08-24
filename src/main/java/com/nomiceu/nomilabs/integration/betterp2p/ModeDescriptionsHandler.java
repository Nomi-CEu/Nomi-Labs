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
            labsModeDescriptions.put(mode,
                    GuiAdvancedMemoryCardKtAccessor.fmtTooltips(mode.getUnlocalizedName(), mode.getUnlocalizedDesc(),
                            BetterMemoryCardModesKt.MAX_TOOLTIP_LENGTH));
        }
    }

    static {
        labsModeDescriptions = new HashMap<>();
        refreshDescriptions();
    }
}
