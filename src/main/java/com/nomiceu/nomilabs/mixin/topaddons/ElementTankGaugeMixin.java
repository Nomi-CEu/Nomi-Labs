package com.nomiceu.nomilabs.mixin.topaddons;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;

import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.netty.buffer.ByteBuf;

/**
 * Fixes Localization of Fluid Names. (Client Only)
 */
@Mixin(value = ElementTankGauge.class, remap = false)
public class ElementTankGaugeMixin {

    @Shadow
    @Final
    @Mutable
    private String fluidName;

    @Shadow
    @Final
    private int amount;

    @Inject(method = "<init>(Lio/netty/buffer/ByteBuf;)V", at = @At("RETURN"))
    private void translateFluidName(ByteBuf buf, CallbackInfo ci) {
        fluidName = LabsFluidNameElement.translateFluid(fluidName, amount, "ElementTankGauge");
    }
}
