package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import com.nomiceu.nomilabs.integration.effortlessbuilding.GenericReachUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import nl.requios.effortlessbuilding.buildmodifier.ModifierSettingsManager;
import nl.requios.effortlessbuilding.helper.ReachHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Changes the actual max axis distance and max blocks.
 */
@Mixin(value = ReachHelper.class, remap = false)
public class ReachHelperMixin {
    @Inject(method = "getMaxBlocksPerAxis", at = @At("HEAD"), cancellable = true)
    private static void getNewMaxBlocksPerAxis(EntityPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (player.isCreative()) {
            cir.setReturnValue(GenericReachUpgrade.REACH_MAP.get(GenericReachUpgrade.CREATIVE_LEVEL).axis);
            return;
        }
        int level = ModifierSettingsManager.getModifierSettings(player).getReachUpgrade();
        cir.setReturnValue(GenericReachUpgrade.REACH_MAP.get(level).axis);
    }

    @Inject(method = "getMaxBlocksPlacedAtOnce", at = @At("HEAD"), cancellable = true)
    private static void getNewMaxBlocksPlacedAtOnce(EntityPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (player.isCreative()) {
            cir.setReturnValue(GenericReachUpgrade.REACH_MAP.get(GenericReachUpgrade.CREATIVE_LEVEL).maxBlocks);
            return;
        }
        int level = ModifierSettingsManager.getModifierSettings(player).getReachUpgrade();
        cir.setReturnValue(GenericReachUpgrade.REACH_MAP.get(level).maxBlocks);
    }
}
