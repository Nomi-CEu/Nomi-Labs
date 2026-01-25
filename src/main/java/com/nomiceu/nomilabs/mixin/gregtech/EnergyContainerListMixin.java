package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;

/**
 * Distributes energy insert/extract more evenly across energy containers.
 */
@Mixin(value = EnergyContainerList.class, remap = false)
public final class EnergyContainerListMixin {

    @Shadow
    @Final
    private List<IEnergyContainer> energyContainerList;

    @WrapMethod(method = "changeEnergy")
    private long distributeChangeEnergy(long energyToAdd, Operation<Long> original) {
        long energyAdded = 0L;

        // Amount of energy to change for each container, assuming perfect distribution
        // Strip decimals to prevent over insertion/extraction
        long energyPerContainer = energyToAdd / energyContainerList.size();

        // Too little energy to distribute, use original logic
        if (energyPerContainer == 0)
            return original.call(energyToAdd);

        // Change energy assuming perfect distribution
        for (IEnergyContainer iEnergyContainer : energyContainerList) {
            energyAdded += iEnergyContainer.changeEnergy(energyPerContainer);
            if (energyAdded == energyToAdd) {
                return energyAdded;
            }
        }

        // Energy remaining: delegate back to original logic
        return original.call(energyToAdd - energyAdded);
    }
}
