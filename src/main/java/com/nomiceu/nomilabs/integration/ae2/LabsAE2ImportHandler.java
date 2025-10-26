package com.nomiceu.nomilabs.integration.ae2;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.nomiceu.nomilabs.config.LabsConfig;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;

public class LabsAE2ImportHandler {

    public static int ASSEMBLY_LINE_RESEARCH_SLOT = 16;
    private static Map<String, Set<Integer>> PRIORITY_ITEMS;

    public static void init() {
        PRIORITY_ITEMS = new Object2ObjectOpenHashMap<>();
        for (String prio : LabsConfig.modIntegration.ae2TerminalOptions.ae2PrioritizedItems) {
            String[] split = prio.split("@");
            if (split.length != 2) {
                throw new IllegalArgumentException(
                        "Illegal config for ae2PrioritizedItems; must be two values separated by @!");
            }

            int meta = Integer.parseInt(split[1]);
            PRIORITY_ITEMS.computeIfAbsent(split[0], _k -> new ObjectArraySet<>()).add(meta);
        }
    }

    public static boolean isItemStackPrioritized(ItemStack stack) {
        ResourceLocation rl = stack.getItem().getRegistryName();
        if (rl == null || !PRIORITY_ITEMS.containsKey(rl.toString())) return false;

        return PRIORITY_ITEMS.get(rl.toString()).contains(stack.getMetadata());
    }
}
