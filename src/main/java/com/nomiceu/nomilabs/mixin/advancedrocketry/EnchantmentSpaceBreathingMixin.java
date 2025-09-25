package com.nomiceu.nomilabs.mixin.advancedrocketry;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.api.items.armor.IArmorItem;
import zmaster587.advancedRocketry.enchant.EnchantmentSpaceBreathing;

/**
 * Fixes the airtight seal enchantment not being applicable to GT Armors.
 */
@Mixin(value = EnchantmentSpaceBreathing.class, remap = false)
public class EnchantmentSpaceBreathingMixin {

    @WrapMethod(method = "canApply", remap = true)
    private boolean checkGTArmor(ItemStack stack, Operation<Boolean> original) {
        return !stack.isEmpty() && (original.call(stack) || stack.getItem() instanceof IArmorItem);
    }
}
