package com.nomiceu.nomilabs.util;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Util class to compare ItemStacks based on Item, Tag and Meta.
 */
@SuppressWarnings("unused")
public class ItemTagMeta extends ItemMeta {

    private final NBTTagCompound tag;

    public ItemTagMeta(ItemStack stack) {
        super(stack);
        tag = stack.getTagCompound();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof ItemTagMeta itemTagMeta)) return false;
        return Objects.equals(tag, itemTagMeta.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tag);
    }

    /**
     * Compares ItemStacks based on Item, Tag and Meta.
     */
    public static boolean compare(ItemStack a, ItemStack b) {
        return ItemMeta.compare(a, b) && Objects.equals(a.getTagCompound(), b.getTagCompound());
    }

    public ItemStack toStack() {
        return toStack(1);
    }

    public ItemStack toStack(int amt) {
        var stack = super.toStack(amt);
        if (tag != null) stack.setTagCompound(tag);
        return stack;
    }

    public NBTTagCompound getTag() {
        return tag;
    }
}
