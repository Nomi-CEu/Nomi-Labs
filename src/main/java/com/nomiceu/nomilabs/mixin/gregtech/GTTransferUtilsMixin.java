package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.util.GTTransferUtils;

/**
 * Fixes issues with transporting items with a stack size of 1, into containers with 1 slot
 * (e.g. Super Chests, Storage Drawers, etc.)
 */
@Mixin(value = GTTransferUtils.class, remap = false)
public class GTTransferUtilsMixin {

    @Redirect(method = "insertItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isStackable()Z", remap = true),
              remap = false,
              require = 1)
    private static boolean ignoreStackableCheck(ItemStack instance) {
        return true;
    }
}
