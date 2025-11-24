package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;

import gregtech.api.capability.IEnergyContainer;

/**
 * Reduces lag due to pairing up GTCE P2Ps.
 */
@Mixin(targets = "appeng.parts.p2p.PartP2PGTCEPower$InputEnergyStorage", remap = false)
public class PartP2PGTCEPowerInputEnergyMixin {

    @WrapOperation(method = "getEnergyCanBeInserted",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/api/capability/IEnergyContainer;getEnergyCanBeInserted()J",
                            ordinal = 0))
    private long saveResultOfCall1(IEnergyContainer instance, Operation<Long> original,
                                   @Share("labs$energyCalculated") LocalLongRef labs$energyCalculated) {
        labs$energyCalculated.set(original.call(instance));
        return labs$energyCalculated.get();
    }

    @WrapOperation(method = "getEnergyCanBeInserted",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/api/capability/IEnergyContainer;getEnergyCanBeInserted()J",
                            ordinal = 1))
    private long useSavedInCall2(IEnergyContainer instance, Operation<Long> original,
                                 @Share("labs$energyCalculated") LocalLongRef labs$energyCalculated) {
        return labs$energyCalculated.get();
    }
}
