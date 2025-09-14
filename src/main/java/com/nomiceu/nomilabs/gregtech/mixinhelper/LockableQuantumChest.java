package com.nomiceu.nomilabs.gregtech.mixinhelper;

import net.minecraft.item.ItemStack;

public interface LockableQuantumChest extends LockableQuantumStorage<ItemStack> {

    boolean labs$lockedBlocksStack(ItemStack stack);

    void labs$stackInserted(ItemStack stack);
}
