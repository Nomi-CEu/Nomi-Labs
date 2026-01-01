package com.nomiceu.nomilabs.mixin.advancedrocketry;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.api.items.armor.IArmorItem;
import zmaster587.advancedRocketry.api.IAtmosphere;
import zmaster587.advancedRocketry.util.ItemAirUtils;

/**
 * Fixes the airtight seal enchantment working with GT Armors.
 */
@Mixin(value = ItemAirUtils.ItemAirWrapper.class, remap = false)
public abstract class ItemAirWrapperMixin {

    @Shadow
    public abstract int decrementAir(ItemStack stack, int amt);

    @WrapMethod(method = "protectsFromSubstance")
    private boolean handleGTArmorItems(IAtmosphere atmosphere, ItemStack stack, boolean commitProtection,
                                       Operation<Boolean> original) {
        // Custom GT armor handling
        if (stack != null && stack.getItem() instanceof IArmorItem) {
            if (stack.getItem().getEquipmentSlot(stack) == EntityEquipmentSlot.CHEST) {
                return decrementAir(stack, 1) == 1;
            }

            return true;
        }

        // Normal handling
        return original.call(atmosphere, stack, commitProtection);
    }
}
