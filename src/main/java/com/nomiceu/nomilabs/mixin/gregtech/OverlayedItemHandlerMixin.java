package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import gregtech.api.util.OverlayedItemHandler;

/**
 * Ensures items inputted into new slots take into account the stack's max stack size.
 */
@Mixin(value = OverlayedItemHandler.class, remap = false)
public class OverlayedItemHandlerMixin {

    @Inject(method = "insertStackedItemStack",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/api/util/OverlayedItemHandler$OverlayedItemHandlerSlot;setItemStack(Lnet/minecraft/item/ItemStack;)V",
                     ordinal = 0),
            require = 1,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void changeCanInsert(ItemStack stack, int amountToInsert, CallbackInfoReturnable<Integer> cir,
                                 @Local(index = 7) LocalIntRef insertedAmount) {
        insertedAmount.set(Math.min(insertedAmount.get(), stack.getMaxStackSize()));
    }
}
