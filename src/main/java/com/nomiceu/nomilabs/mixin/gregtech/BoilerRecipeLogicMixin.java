package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.capability.impl.BoilerRecipeLogic;

/**
 * Makes fuels last 10x as long in large boilers, to fix fuel efficiency disparity between it and small boilers.
 */
@Mixin(value = BoilerRecipeLogic.class, remap = false)
public class BoilerRecipeLogicMixin {

    @Redirect(method = "trySearchNewRecipe",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/tileentity/TileEntityFurnace;getItemBurnTime(Lnet/minecraft/item/ItemStack;)I",
                       remap = true))
    private int increaseFuelBurnTime(ItemStack itemStack) {
        return TileEntityFurnace.getItemBurnTime(itemStack) * 10;
    }
}
