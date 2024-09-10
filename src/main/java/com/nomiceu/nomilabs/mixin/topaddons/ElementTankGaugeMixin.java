package com.nomiceu.nomilabs.mixin.topaddons;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;

import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.network.NetworkTools;

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

    @Shadow
    @Final
    private int capacity;

    @Shadow
    @Final
    private String tankName;

    @Shadow
    @Final
    private String suffix;

    @Shadow
    @Final
    private int color1;

    @Shadow
    @Final
    private boolean sneaking;

    @Unique
    private String labs$originalName;

    @Inject(method = "<init>(Lio/netty/buffer/ByteBuf;)V", at = @At("RETURN"))
    private void translateFluidName(ByteBuf buf, CallbackInfo ci) {
        labs$originalName = fluidName;
        fluidName = LabsFluidNameElement.translateFluid(fluidName, amount, "ElementTankGauge");
    }

    /**
     * Replace the Whole Method, safer for such a critical method
     */
    @Inject(method = "toBytes", at = @At("HEAD"), cancellable = true)
    private void writeOriginalName(ByteBuf buf, CallbackInfo ci) {
        NetworkTools.writeString(buf, tankName);
        NetworkTools.writeString(buf, labs$originalName);
        buf.writeInt(amount);
        buf.writeInt(capacity);
        NetworkTools.writeString(buf, suffix);
        buf.writeInt(color1);
        buf.writeBoolean(sneaking);
        ci.cancel();
    }
}
