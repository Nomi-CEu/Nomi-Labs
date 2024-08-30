package com.nomiceu.nomilabs.mixin.vanilla;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.text2speech.Narrator;
import com.mojang.text2speech.NarratorDummy;
import com.nomiceu.nomilabs.config.LabsConfig;

@SuppressWarnings("PublicStaticMixinMember")
@Mixin(value = Narrator.class, remap = false)
public interface NarratorMixin {

    @Inject(method = "getNarrator", at = @At("HEAD"), cancellable = true, remap = false)
    static void getDummyNarrator(CallbackInfoReturnable<Narrator> cir) {
        if (LabsConfig.advanced.disableNarrator) cir.setReturnValue(new NarratorDummy());
    }
}
