package com.nomiceu.nomilabs.mixin.thermalfoundation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.config.LabsConfig;

import cofh.thermalfoundation.item.tome.ItemTomeExperience;

/**
 * Applies Linear XP Scaling to Thermal Tomes.
 */
@Mixin(value = ItemTomeExperience.class, remap = false)
public class ItemTomeExperienceMixin {

    @Inject(method = "getTotalExpForLevel", at = @At("HEAD"), cancellable = true)
    private static void getLinearXp(int level, CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.otherModsLinearXp != 0 && level >= 0)
            cir.setReturnValue(LabsConfig.advanced.otherModsLinearXp * level);
    }
}
