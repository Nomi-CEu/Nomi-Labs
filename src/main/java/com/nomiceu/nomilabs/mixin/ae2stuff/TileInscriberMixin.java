package com.nomiceu.nomilabs.mixin.ae2stuff;

import net.bdew.ae2stuff.machines.inscriber.TileInscriber;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

/**
 * Fixes a duplication glitch with name presses.
 */
@Mixin(value = TileInscriber.class, remap = false)
public class TileInscriberMixin {

    @WrapOperation(method = "net$bdew$ae2stuff$machines$inscriber$TileInscriber$$makeNamePressRecipe",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;",
                            remap = true),
                   remap = false,
                   require = 2)
    private ItemStack copyAndNormalizeStackSize(ItemStack instance, Operation<ItemStack> original) {
        ItemStack copied = original.call(instance);
        copied.setCount(1);
        return copied;
    }
}
