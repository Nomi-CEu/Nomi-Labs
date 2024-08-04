package com.nomiceu.nomilabs.mixin.enderio;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.config.LabsConfig;

import crazypants.enderio.base.xp.XpUtil;

/**
 * Allows EIO Machines to use Linear XP Scaling.
 */
@Mixin(value = XpUtil.class, remap = false)
public class XPUtilMixin {

    @Inject(method = "calculateXPfromLevel", at = @At("HEAD"), cancellable = true)
    private static void calculateXpFromLevelLinear(int level, CallbackInfoReturnable<Long> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && level >= 0)
            cir.setReturnValue(((long) LabsConfig.advanced.aaEioLinearXp * level));
    }

    @Inject(method = "getLevelFromExp", at = @At("HEAD"), cancellable = true)
    private static void getLevelFromXpLinear(long exp, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && exp >= 0)
            cir.setReturnValue((int) (exp / LabsConfig.advanced.aaEioLinearXp));
    }

    @Inject(method = "getXpBarCapacity", at = @At("HEAD"), cancellable = true)
    private static void getLinearXPAmount(int level, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && level >= 0)
            cir.setReturnValue(LabsConfig.advanced.aaEioLinearXp);
    }
}
