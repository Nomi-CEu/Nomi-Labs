package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.common.ToolEventHandlers;

/**
 * Fix chainsaw breaking replacing currently held item with power unit, regardless of whether it is the chainsaw.
 */
@Mixin(value = ToolEventHandlers.class, remap = false)
public class ToolEventHandlersMixin {

    @Redirect(method = "onPlayerDestroyItem",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraftforge/event/entity/player/PlayerDestroyItemEvent;getHand()Lnet/minecraft/util/EnumHand;",
                       ordinal = 0),
              require = 1)
    private static EnumHand checkForSameStack(PlayerDestroyItemEvent instance) {
        var hand = instance.getHand();
        if (hand == null) return null;

        // For the tree felling routine, the player may no longer be holding the chainsaw
        // Check for exact instance equality, and if not, return null
        // Returning null leads to the item being added to inventory instead of replacing item in hand
        if (instance.getEntityPlayer().getHeldItem(hand) != instance.getOriginal())
            return null;

        return hand;
    }
}
