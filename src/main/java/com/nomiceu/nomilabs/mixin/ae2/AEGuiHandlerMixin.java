package com.nomiceu.nomilabs.mixin.ae2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.integration.ae2.LabsSpecialSlotIngredient;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.AEGuiHandler;
import appeng.client.gui.implementations.GuiCraftAmount;
import appeng.client.gui.widgets.GuiCustomSlot;
import appeng.container.interfaces.IJEIGhostIngredients;
import mezz.jei.api.gui.IGhostIngredientHandler;
import mezz.jei.bookmarks.BookmarkItem;

/**
 * Applies <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/451">AE2 #451</a> for v0.56.5.
 * Allows ghost item handling of bookmarks.
 */
@Mixin(value = AEGuiHandler.class, remap = false)
public class AEGuiHandlerMixin {

    @SuppressWarnings("unchecked")
    @WrapMethod(method = "getTargets(Lappeng/client/gui/AEBaseGui;Ljava/lang/Object;Z)Ljava/util/List;")
    private <I> List<IGhostIngredientHandler.Target<I>> newGetTargetsLogic(AEBaseGui gui, I ingredient, boolean doStart,
                                                                           Operation<List<IGhostIngredientHandler.Target<I>>> original) {
        if (!(gui instanceof IJEIGhostIngredients g)) return Collections.emptyList();

        // Bookmarks
        if (ingredient instanceof BookmarkItem<?>book) {
            List<IGhostIngredientHandler.Target<Object>> phantomTargets = (List<IGhostIngredientHandler.Target<Object>>) (Object) g
                    .getPhantomTargets(book.ingredient);

            List<IGhostIngredientHandler.Target<I>> result = new ArrayList<>();
            for (IGhostIngredientHandler.Target<Object> target : phantomTargets) {
                result.add(new IGhostIngredientHandler.Target<>() {

                    @Override
                    public @NotNull Rectangle getArea() {
                        return target.getArea();
                    }

                    @Override
                    public void accept(@NotNull I ingredient) {
                        target.accept(book.ingredient);
                    }
                });
            }

            return result;
        }

        return (List<IGhostIngredientHandler.Target<I>>) (Object) g.getPhantomTargets(ingredient);
    }

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
