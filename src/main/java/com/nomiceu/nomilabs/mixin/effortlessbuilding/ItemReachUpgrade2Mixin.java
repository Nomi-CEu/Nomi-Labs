package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import com.nomiceu.nomilabs.integration.effortlessbuilding.GenericReachUpgrade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.requios.effortlessbuilding.item.ItemReachUpgrade2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Adds Proper Information and Improves Clarity of Messages of Reach Upgrade 2.
 */
@Mixin(value = ItemReachUpgrade2.class, remap = false)
public class ItemReachUpgrade2Mixin {
    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true, remap = true)
    public void onNewItemRightClick(World world, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        cir.setReturnValue(GenericReachUpgrade.onItemRightClick(world, player, hand, 2));
    }

    @Inject(method = "addInformation", at = @At("HEAD"), cancellable = true, remap = true)
    @SideOnly(Side.CLIENT)
    public void addNewInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, CallbackInfo ci) {
        GenericReachUpgrade.addInformation(tooltip, 2);
        ci.cancel();
    }
}
