package com.nomiceu.nomilabs.mixin.thermalfoundation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cofh.api.tileentity.IUpgradeable;
import cofh.core.util.helpers.ChatHelper;
import cofh.thermalfoundation.item.ItemUpgrade;

/**
 * Makes Unapplyable Upgrades Display Fail Message Instead of Entering GUI.
 */
@Mixin(value = ItemUpgrade.class, remap = false)
public class ItemUpgradeMixin {

    @Inject(method = "onItemUseFirst", at = @At("HEAD"), cancellable = true)
    private void handleUnapplyableUpgrades(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
                                           float hitY, float hitZ, EnumHand hand,
                                           CallbackInfoReturnable<EnumActionResult> cir) {
        var te = world.getTileEntity(pos);
        if (!(te instanceof IUpgradeable upgradeable) || world.isRemote) return;

        if (!upgradeable.canUpgrade(player.getHeldItem(hand))) {
            ChatHelper.sendIndexedChatMessageToPlayer(player,
                    new TextComponentTranslation("chat.thermalfoundation.upgrade.install.failure"));
            cir.setReturnValue(EnumActionResult.SUCCESS); // So that it doesn't open GUI
        }
    }
}
