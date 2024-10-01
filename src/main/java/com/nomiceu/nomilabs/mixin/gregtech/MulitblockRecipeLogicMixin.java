package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeLogic;

import gregtech.api.capability.impl.MultiblockRecipeLogic;

@Mixin(value = MultiblockRecipeLogic.class, remap = false)
public class MulitblockRecipeLogicMixin {

    @Inject(method = "invalidate", at = @At("RETURN"))
    private void invalidateLabs(CallbackInfo ci) {
        ((AccessibleRecipeLogic) this).labs$invalidate();
    }
}
