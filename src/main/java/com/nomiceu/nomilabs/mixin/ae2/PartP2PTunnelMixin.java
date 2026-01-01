package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.util.LabsSide;

import appeng.api.implementations.items.IMemoryCard;
import appeng.parts.p2p.PartP2PTunnel;

/**
 * Allows disabling p2p tunnel in world type change.
 */
@Mixin(value = PartP2PTunnel.class, remap = false)
public class PartP2PTunnelMixin {

    @WrapMethod(method = "onPartActivate")
    private boolean disableP2PINWorldChange(EntityPlayer player, EnumHand hand, Vec3d pos,
                                            Operation<Boolean> original) {
        if (!LabsConfig.modIntegration.disableP2PInWorldChange)
            return original.call(player, hand, pos);

        // AE2 default behaviours
        if (LabsSide.isClient()) return true;

        if (hand == EnumHand.OFF_HAND) return false;

        ItemStack checkItem;

        ItemStack mainHandItem = player.getHeldItem(hand);
        if (mainHandItem.isEmpty())
            checkItem = player.getHeldItem(EnumHand.OFF_HAND);
        else
            checkItem = mainHandItem;

        // Only do normal handling if we are handling a memory card
        if (!checkItem.isEmpty() && checkItem.getItem() instanceof IMemoryCard) {
            return original.call(player, hand, pos);
        }

        return false;
    }
}
