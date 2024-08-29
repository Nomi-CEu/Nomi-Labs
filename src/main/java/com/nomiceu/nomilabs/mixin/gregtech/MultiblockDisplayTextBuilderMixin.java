package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;

/**
 * Takes into account amperages when showing the Max EU/t of a Multiblock.
 */
@Mixin(value = MultiblockDisplayText.Builder.class, remap = false)
public class MultiblockDisplayTextBuilderMixin {

    @Inject(method = "addEnergyUsageLine",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(JJ)J"),
            require = 1,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void accountAmperages(IEnergyContainer energyContainer,
                                  CallbackInfoReturnable<MultiblockDisplayText.Builder> cir,
                                  @Local LocalLongRef maxVoltage) {
        maxVoltage.set(Math.max(energyContainer.getInputVoltage() * energyContainer.getInputAmperage(),
                energyContainer.getOutputVoltage() * energyContainer.getOutputAmperage()));
    }
}
