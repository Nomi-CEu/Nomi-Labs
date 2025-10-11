package com.nomiceu.nomilabs.gregtech.mixinhelper;

import net.minecraft.item.ItemStack;

public interface AccessibleQuantumChest extends AccessibleQuantumStorage {

    boolean labs$lockedBlocksStack(ItemStack stack);

    void labs$stackInserted(ItemStack stack);

    ItemStack labs$getLockedStack();

    long labs$getMaxStored();
}
