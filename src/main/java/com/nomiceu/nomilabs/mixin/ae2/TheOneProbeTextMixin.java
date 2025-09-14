package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.util.LabsTranslate;

import appeng.integration.modules.theoneprobe.TheOneProbeText;

/**
 * Part of the AE2 TOP Display on Dedicated Servers fix.
 * Applies a 'default fix', for use cases without complex behaviours (e.g. string format)
 */
@Mixin(value = TheOneProbeText.class, remap = false)
public abstract class TheOneProbeTextMixin {

    @Shadow
    public abstract String getUnlocalized();

    @Inject(method = "getLocal", at = @At("HEAD"), cancellable = true)
    private void delegateLocalization(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(LabsTranslate.topTranslate(getUnlocalized()));
    }
}
