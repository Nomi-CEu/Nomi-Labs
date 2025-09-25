package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.effortlessbuilding.GenericReachUpgrade;

import nl.requios.effortlessbuilding.item.ItemReachUpgrade1;

/**
 * Adds Proper Information and Improves Clarity of Messages of Reach Upgrade 1.
 */
@Mixin(value = ItemReachUpgrade1.class, remap = false)
public class ItemReachUpgrade1Mixin {

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true, remap = true)
    private void onNewItemRightClick(World world, EntityPlayer player, EnumHand hand,
                                     CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        cir.setReturnValue(GenericReachUpgrade.onItemRightClick(world, player, hand, 1));
    }

    @SideOnly(Side.CLIENT)
    @Inject(method = "addInformation", at = @At("HEAD"), cancellable = true, remap = true)
    private void addNewInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag,
                                   CallbackInfo ci) {
        GenericReachUpgrade.addInformation(tooltip, 1);
        ci.cancel();
    }
}
