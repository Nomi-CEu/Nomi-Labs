package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.ae2.LabsSpecialSlotIngredient;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.AEGuiHandler;
import appeng.client.gui.implementations.GuiCraftAmount;
import appeng.client.gui.widgets.GuiCustomSlot;

/**
 * Applies <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/451">AE2 #451</a> for v0.56.5.
 */
@Mixin(value = AEGuiHandler.class, remap = false)
public class AEGuiHandlerMixin {

    @Inject(method = "getIngredientUnderMouse(Lappeng/client/gui/AEBaseGui;II)Ljava/lang/Object;",
            at = @At("TAIL"),
            cancellable = true)
    private void checkFluidSlots(AEBaseGui guiContainer, int mouseX, int mouseY, CallbackInfoReturnable<Object> cir) {
        if (cir.getReturnValue() != null || guiContainer instanceof GuiCraftAmount) return;

        Slot slot = guiContainer.getSlotUnderMouse();
        if (slot instanceof LabsSpecialSlotIngredient special) {
            cir.setReturnValue(special.labs$getIngredient());
        }

        for (GuiCustomSlot customSlot : guiContainer.getGuiSlots()) {
            if (labs$checkSlotArea(guiContainer, customSlot, mouseX, mouseY) &&
                    customSlot instanceof LabsSpecialSlotIngredient special) {
                cir.setReturnValue(special.labs$getIngredient());
            }
        }
    }

    @Unique
    private boolean labs$checkSlotArea(GuiContainer gui, GuiCustomSlot slot, int mouseX, int mouseY) {
        int i = gui.guiLeft;
        int j = gui.guiTop;
        mouseX = mouseX - i;
        mouseY = mouseY - j;
        return mouseX >= slot.xPos() - 1 &&
                mouseX < slot.xPos() + slot.getWidth() + 1 &&
                mouseY >= slot.yPos() - 1 &&
                mouseY < slot.yPos() + slot.getHeight() + 1;
    }
}
