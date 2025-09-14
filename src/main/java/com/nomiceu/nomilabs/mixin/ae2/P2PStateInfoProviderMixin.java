package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsLangKeyWithArgsElement;

import appeng.integration.modules.theoneprobe.TheOneProbeText;
import appeng.integration.modules.theoneprobe.part.P2PStateInfoProvider;
import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Properly localizes a TOP string.
 */
@Mixin(value = P2PStateInfoProvider.class, remap = false)
public class P2PStateInfoProviderMixin {

    @WrapOperation(method = "addProbeInfo",
                   at = @At(value = "INVOKE",
                            target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;",
                            ordinal = 1))
    private IProbeInfo properlyAddTextP2PIsOutput(IProbeInfo instance, String s, Operation<IProbeInfo> original,
                                                  @Local(ordinal = 2) int inputCount) {
        if (inputCount <= 1) {
            return original.call(instance, s);
        }

        return instance.element(new LabsLangKeyWithArgsElement(TheOneProbeText.P2P_OUTPUT_MANY_INPUTS.getUnlocalized(),
                Integer.toString(inputCount)));
    }

    @WrapOperation(method = "addProbeInfo",
                   at = @At(value = "INVOKE",
                            target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;",
                            ordinal = 2))
    private IProbeInfo properlyAddTextP2PIsInput(IProbeInfo instance, String s, Operation<IProbeInfo> original,
                                                 @Local(ordinal = 1) int outputCount) {
        if (outputCount <= 1) {
            return original.call(instance, s);
        }

        return instance.element(new LabsLangKeyWithArgsElement(TheOneProbeText.P2P_INPUT_MANY_OUTPUTS.getUnlocalized(),
                Integer.toString(outputCount)));
    }
}
