package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityObjectHolder;

/**
 * Stops Items being allowed into the slots when a recipe is running, causing voiding issues.
 */
@Mixin(targets = "gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityObjectHolder$ObjectHolderHandler",
       remap = false)
public class ObjectHolderHandlerMixin {

    @Shadow(aliases = "this$0")
    @Final
    MetaTileEntityObjectHolder this$0;

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void checkBlocked(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (((MetaTileEntityObjectHolderAccessor) this$0).invokeIsSlotBlocked())
            cir.setReturnValue(false);
    }
}
