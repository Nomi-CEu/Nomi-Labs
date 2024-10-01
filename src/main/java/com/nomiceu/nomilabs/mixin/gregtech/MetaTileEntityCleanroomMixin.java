package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.api.capability.IEnergyContainer;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityCleanroom;

@Mixin(value = MetaTileEntityCleanroom.class, remap = false)
public class MetaTileEntityCleanroomMixin {

    @Shadow
    private IEnergyContainer energyContainer;

    @Inject(method = "drainEnergy", at = @At("HEAD"), cancellable = true)
    private void checkNull(boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        if (energyContainer == null) cir.setReturnValue(false);
    }
}
