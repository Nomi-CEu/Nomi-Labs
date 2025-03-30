package com.nomiceu.nomilabs.util;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Util class to compare ItemStacks based on Item and Meta.
 */
@SuppressWarnings("unused")
public class ItemMeta {

    private final Item item;
    private final int meta;

    public ItemMeta(ItemStack stack) {
        item = stack.getItem();
        meta = stack.getMetadata();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemMeta itemMeta)) return false;
        return item.equals(itemMeta.item) && meta == itemMeta.meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, meta);
    }

    public boolean compareWith(ItemStack other) {
        return item == other.getItem() && meta == other.getMetadata();
    }

    /**
     * Compares ItemStacks based on Item and Meta.
     */
    public static boolean compare(ItemStack a, ItemStack b) {
        return a.getItem().equals(b.getItem()) && a.getMetadata() == b.getMetadata();
    }

    public ItemStack toStack() {
        return toStack(1);
    }

    public ItemStack toStack(int amount) {
        return new ItemStack(item, amount, meta);
    }

    public Item getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }
}
