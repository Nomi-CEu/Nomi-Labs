package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumChest;

import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;

/**
 * Part of the implementation to allow quantum chests to be lockable.
 * <p>
 * Checks insert item operations, updates locked stack when item inserted.
 */
@Mixin(targets = "gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest$QuantumChestItemHandler",
       remap = false)
public class MetaTileEntityQuantumChestItemHandlerMixin {

    @Shadow
    @Final
    MetaTileEntityQuantumChest this$0;

    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void checkLocked(int slot, ItemStack insertedStack, boolean simulate,
                             CallbackInfoReturnable<ItemStack> cir) {
        if (((AccessibleQuantumChest) this$0).labs$lockedBlocksStack(insertedStack))
            cir.setReturnValue(insertedStack);
    }

    @Inject(method = "insertItem", at = @At("RETURN"))
    private void updateLocked(int slot, ItemStack insertedStack, boolean simulate,
                              CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result == null) return; // This shouldn't happen, but check just in case

        // Insert Item Successful
        if (result.getCount() < insertedStack.getCount() && !simulate) {
            ((AccessibleQuantumChest) this$0).labs$stackInserted(insertedStack);
        }
    }
}
