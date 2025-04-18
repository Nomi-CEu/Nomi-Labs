package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.container.AEBaseContainer;
import appeng.container.slot.SlotDisabled;

/**
 * Applies
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/f1e5a33a6e0f391c3083eb0ef163a3adaea7577f">AE2
 * f1e5a33</a> and its later fix commit, as well as
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/7baf538a7684cadffc012c2dc580ee52c84d552a">AE2
 * 7baf538</a>, for v0.56.5.
 */
@Mixin(value = AEBaseContainer.class, remap = false)
public abstract class AEBaseContainerMixin extends Container {

    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true, remap = true)
    private void checkDisabled(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player,
                               CallbackInfoReturnable<ItemStack> cir) {
        if (slotId >= 0) {
            var slot = getSlot(slotId);
            if (slot instanceof SlotDisabled) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
