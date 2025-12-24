package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.common.covers.CoverEnderFluidLink;

/**
 * Fixes leading zeros being stripped.
 */
@Mixin(value = CoverEnderFluidLink.class, remap = false)
public class CoverEnderFluidLinkMixin {

    @Shadow
    private boolean isColorTemp;

    @Shadow
    private int color;

    @Inject(method = "getColorStr", at = @At("HEAD"), cancellable = true)
    private void getProperColorStr(CallbackInfoReturnable<String> cir) {
        if (isColorTemp) return; // Default impl

        cir.setReturnValue(String.format("%08X", color));
    }
}
