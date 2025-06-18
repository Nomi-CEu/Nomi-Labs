package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.nomiceu.nomilabs.util.ItemTagMeta;

import appeng.util.item.AEItemStack;

/**
 * Ignores Forge Caps in comparing ItemStacks.
 */
@Mixin(value = AEItemStack.class, remap = false)
public class AEItemStackMixin {

    @Redirect(method = "isSameType(Lnet/minecraft/item/ItemStack;)Z",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/item/ItemStack;areItemStacksEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z",
                       remap = true),
              require = 1)
    private boolean ignoreForgeCaps(ItemStack stackA, ItemStack stackB) {
        return ItemTagMeta.compare(stackA, stackB);
    }
}
