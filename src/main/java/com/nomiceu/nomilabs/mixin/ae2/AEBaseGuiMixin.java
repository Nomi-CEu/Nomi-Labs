package com.nomiceu.nomilabs.mixin.ae2;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.ITooltip;
import appeng.container.AEBaseContainer;
import appeng.container.slot.AppEngSlot;
import appeng.items.misc.ItemEncodedPattern;

/**
 * Fixes Mouse Tweaks Interactions in Terminals, for AE2 v0.56.5.
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/507">AE2 #507</a> for v0.56.5.
 * Fixes issues with using hotbar keys on disabled slots.
 * Fixes Blank Encoded Patterns rendering as nothing.
 * Disables custom jei ghost item handling.
 */
@Mixin(value = AEBaseGui.class, remap = false)
public abstract class AEBaseGuiMixin extends GuiContainer {

    @Shadow
    protected abstract void drawTooltip(ITooltip tooltip, int mouseX, int mouseY);

    @Shadow
    protected abstract List<Slot> getInventorySlots();

    /**
     * Mandatory Ignored Constructor
     */
    private AEBaseGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "MT_isMouseTweaksDisabled", at = @At("HEAD"), cancellable = true)
    private void setMouseTweaksEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "MT_isIgnored", at = @At("HEAD"), cancellable = true)
    private void setMouseTweaksNotIgnored(Slot slot, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "MT_disableRMBDraggingFunctionality", at = @At("HEAD"), cancellable = true)
    private void newRMBDraggingFunctionality(CallbackInfoReturnable<Boolean> cir) {
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

    @Inject(method = "drawScreen",
            at = @At(value = "INVOKE",
                     target = "Lappeng/client/gui/AEBaseGui;renderHoveredToolTip(II)V"),
            require = 1,
            remap = true)
    private void drawExtraLabels(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        for (Object o : labelList) {
            if (o instanceof ITooltip tooltip) {
                drawTooltip(tooltip, mouseX, mouseY);
            }
        }
    }

    @WrapOperation(method = "drawScreen",
                   at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseGui;bookmarkedJEIghostItem(II)V"))
    private void cancelCustomJEIHandling(AEBaseGui instance, int i, int mouseX, Operation<Void> original) {}

    @Inject(method = "checkHotbarKeys",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Slot;getSlotStackLimit()I"),
            require = 1,
            cancellable = true,
            remap = true,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void checkSlotsWithIndirectPlayerInv(int keyCode, CallbackInfoReturnable<Boolean> cir,
                                                 @Local(ordinal = 1) int j) {
        for (Slot s : getInventorySlots()) {
            if (s.getSlotIndex() == j && (s instanceof AppEngSlot app) &&
                    (app.getItemHandler() instanceof PlayerInvWrapper) &&
                    !s.canTakeStack(((AEBaseContainer) this.inventorySlots).getPlayerInv().player)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "drawSlot",
            at = @At(value = "INVOKE",
                     target = "Lappeng/items/misc/ItemEncodedPattern;getOutput(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;",
                     remap = false,
                     shift = At.Shift.AFTER),
            remap = true,
            require = 1)
    private void drawBlankEncodedPattern(Slot s, CallbackInfo ci, @Local ItemEncodedPattern pattern) {
        if (pattern.getOutput(s.getStack()).isEmpty())
            super.drawSlot(s);
    }
}
