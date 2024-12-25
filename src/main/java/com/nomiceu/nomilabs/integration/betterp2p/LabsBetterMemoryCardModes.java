package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.Map;

import net.minecraftforge.common.util.EnumHelper;

import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class LabsBetterMemoryCardModes {

    /**
     * Map of Mode to Labs ID.
     */
    public static final Map<BetterMemoryCardModes, Integer> LABS_ADDED_MODES = new Object2ObjectOpenHashMap<>();

    public static final BetterMemoryCardModes ADD_AS_INPUT = addMode(0, "ADD_AS_INPUT",
            "gui.advanced_memory_card.mode.add_as_input",
            "nomilabs.gui.advanced_memory_card.mode.add_input.desc.1",
            "nomilabs.gui.advanced_memory_card.mode.add_input.desc.2");

    public static final BetterMemoryCardModes ADD_AS_OUTPUT = addMode(1, "ADD_AS_OUTPUT",
            "gui.advanced_memory_card.mode.add_as_output",
            "nomilabs.gui.advanced_memory_card.mode.add_output.desc.1",
            "nomilabs.gui.advanced_memory_card.mode.add_output.desc.2");

    /**
     * Essentially, this loads the class, allowing the above values to be added.
     * <p>
     * If for some reason, the values are needed before this, they will still be loaded, and calling init will have no
     * affect on that.
     */
    public static void preInit() {}

    private static BetterMemoryCardModes addMode(int labsId, String name, String unlocalizedName,
                                                 String... unlocalizedDesc) {
        BetterMemoryCardModes result = EnumHelper.addEnum(BetterMemoryCardModes.class, name,
                new Class<?>[] { String.class, String[].class },
                unlocalizedName, unlocalizedDesc);
        LABS_ADDED_MODES.put(result, labsId);
        return result;
    }
}
