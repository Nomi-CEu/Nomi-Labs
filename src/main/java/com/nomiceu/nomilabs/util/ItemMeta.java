package com.nomiceu.nomilabs.util;

import net.minecraft.item.ItemStack;

public class ItemMeta {
    /**
     * Returns true if itemstacks are equal. Compares item & meta.
     */
    public static boolean compare(ItemStack a, ItemStack b) {
        return a.getItem().equals(b.getItem()) && a.getMetadata() == b.getMetadata();
    }
}
