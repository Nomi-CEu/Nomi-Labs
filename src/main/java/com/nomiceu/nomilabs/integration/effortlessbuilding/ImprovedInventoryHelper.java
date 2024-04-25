package com.nomiceu.nomilabs.integration.effortlessbuilding;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ImprovedInventoryHelper {

    public static ItemStack findItemStackInInventory(EntityPlayer player, IBlockState state) {
        var block = state.getBlock();
        var meta = block.getMetaFromState(state);
        for (ItemStack invStack : player.inventory.mainInventory) {
            if (!invStack.isEmpty() && invStack.getItem() instanceof ItemBlock itemBlock &&
                    itemBlock.getBlock().equals(block) && invStack.getMetadata() == meta) {
                return invStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack findItemStackInInventory(EntityPlayer player, ItemStack stack) {
        var item = stack.getItem();
        var meta = stack.getMetadata();
        for (ItemStack invStack : player.inventory.mainInventory) {
            if (!invStack.isEmpty() && invStack.getItem() instanceof ItemBlock itemBlock && itemBlock.equals(item) &&
                    invStack.getMetadata() == meta) {
                return invStack;
            }
        }
        return ItemStack.EMPTY;
    }
}
