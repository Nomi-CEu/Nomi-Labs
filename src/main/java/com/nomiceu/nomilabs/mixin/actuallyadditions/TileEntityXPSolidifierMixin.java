package com.nomiceu.nomilabs.mixin.actuallyadditions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.config.LabsConfig;

import de.ellpeck.actuallyadditions.mod.tile.TileEntityXPSolidifier;

@Mixin(value = TileEntityXPSolidifier.class, remap = false)
public abstract class TileEntityXPSolidifierMixin {

    @Inject(method = "getExperienceForLevel", at = @At("HEAD"), cancellable = true)
    private static void getLinearXpAmountLevel(int level, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && level >= 0)
            cir.setReturnValue(LabsConfig.advanced.aaEioLinearXp * level);
    }

    @Inject(method = "getLevelForExperience", at = @At("HEAD"), cancellable = true)
    private static void getLinearXPAmountXp(int experience, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && experience >= 0)
            cir.setReturnValue(experience / LabsConfig.advanced.aaEioLinearXp);
    }

    @Inject(method = "getXpBarCapacity", at = @At("HEAD"), cancellable = true)
    private static void getLinearXPAmount(int level, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.aaEioLinearXp != 0 && level >= 0)
            cir.setReturnValue(LabsConfig.advanced.aaEioLinearXp);
    }
}
