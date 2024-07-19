package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.client.gui.AEBaseGui;

/**
 * Fixes Mouse Tweaks Interactions in Terminals, for AE2 v0.56.5.
 */
@Mixin(value = AEBaseGui.class, remap = false)
public abstract class AEBaseGuiMixin extends GuiContainer {

    /**
     * Mandatory Ignored Constructor
     */
    public AEBaseGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "MT_isMouseTweaksDisabled", at = @At("HEAD"), cancellable = true)
    public void setMouseTweaksEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "MT_isIgnored", at = @At("HEAD"), cancellable = true)
    public void setMouseTweaksNotIgnored(Slot slot, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "MT_disableRMBDraggingFunctionality", at = @At("HEAD"), cancellable = true)
    public void newRMBDraggingFunctionality(CallbackInfoReturnable<Boolean> cir) {
        if (dragSplitting && dragSplittingButton == 1) {
            dragSplitting = false;
            if (getSlotUnderMouse() != null &&
                    getSlotUnderMouse().isItemValid(mc.player.inventory.getItemStack()))
                ignoreMouseUp = true;
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }
}
