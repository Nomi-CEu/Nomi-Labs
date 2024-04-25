package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.effortlessbuilding.ImprovedInventoryHelper;

import nl.requios.effortlessbuilding.buildmodifier.UndoRedo;

/**
 * Fixes a Transmutation Bug where Effortless would ignore Meta during Undo/Redo.
 */
@Mixin(value = UndoRedo.class, remap = false)
public class UndoRedoMixin {

    // Just Replace Whole Function, as it is short, and drops check uses wrong function
    @Inject(method = "findItemStackInInventory", at = @At("HEAD"), cancellable = true)
    private static void findProperItemStackInInventory(EntityPlayer player, IBlockState blockState,
                                                       CallbackInfoReturnable<ItemStack> cir) {
        if (blockState == null) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        var result = ImprovedInventoryHelper.findItemStackInInventory(player, blockState);
        if (!result.isEmpty()) {
            cir.setReturnValue(result);
            return;
        }

        /* Drops Check */
        Item itemDropped = blockState.getBlock().getItemDropped(blockState, player.world.rand, 10);
        int metaDropped = blockState.getBlock().damageDropped(blockState);
        if (itemDropped instanceof ItemBlock) {
            result = ImprovedInventoryHelper.findItemStackInInventory(player,
                    new ItemStack(itemDropped, 1, metaDropped));
        }
        // Will be empty if Previous found None
        cir.setReturnValue(result);
    }
}
