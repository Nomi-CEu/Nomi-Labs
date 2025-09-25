package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.LockableQuantumChest;

import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;

/**
 * Part of the implementation to allow quantum chests to be lockable.
 * <p>
 * Checks locked in insert slot.
 */
@Mixin(targets = "gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest$1", remap = false)
public class MetaTileEntityQuantumChestGTItemHandlerMixin extends GTItemStackHandler {

    @Shadow
    @Final
    MetaTileEntityQuantumChest this$0;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumChestGTItemHandlerMixin(MetaTileEntity metaTileEntity) {
        super(metaTileEntity);
    }

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void checkedLocked(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (((LockableQuantumChest) this$0).labs$lockedBlocksStack(stack))
            cir.setReturnValue(false);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ((LockableQuantumChest) this$0).labs$stackInserted(getStackInSlot(0));
    }
}
