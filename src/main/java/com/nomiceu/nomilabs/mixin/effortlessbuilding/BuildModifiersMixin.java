package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.effortlessbuilding.ImprovedInventoryHelper;

import nl.requios.effortlessbuilding.buildmodifier.BuildModifiers;

/**
 * Fixes Transmutation when Building, as Effortless isn't checking Meta.
 */
@Mixin(value = BuildModifiers.class, remap = false)
public class BuildModifiersMixin {

    @Redirect(method = "onBlockPlaced",
              at = @At(value = "INVOKE",
                       target = "Lnl/requios/effortlessbuilding/helper/InventoryHelper;findItemStackInInventory(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/block/Block;)Lnet/minecraft/item/ItemStack;"),
              require = 1)
    private static ItemStack getProperStacksInInv(EntityPlayer player, Block block, @Local IBlockState blockState) {
        return ImprovedInventoryHelper.findItemStackInInventory(player, blockState);
    }
}
