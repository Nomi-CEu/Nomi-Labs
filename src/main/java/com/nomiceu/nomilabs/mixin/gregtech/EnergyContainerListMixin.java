package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleEnergyContainerList;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EnergyContainerList.class, remap = false)
public class EnergyContainerListMixin implements AccessibleEnergyContainerList {
    @Shadow
    @Final
    private long highestInputVoltage;

    /**
     * The total amperage of all the containers with the highest input voltage.
     */
    @Unique
    private long totalHighestInputAmperage = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initTotalHighestInputAmp(List<IEnergyContainer> energyContainerList, CallbackInfo ci) {
        for (IEnergyContainer container : energyContainerList) {
            if (container.getInputVoltage() == highestInputVoltage) {
                totalHighestInputAmperage += container.getInputAmperage();
            }
        }
    }

    @Override
    public long getTotalHighestInputAmperage() {
        return totalHighestInputAmperage;
    }
}
