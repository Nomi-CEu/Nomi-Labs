package com.nomiceu.nomilabs.mixin.nuclearcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.groovy.NCActiveCoolerHelper;
import com.nomiceu.nomilabs.integration.nuclearcraft.AccessibleCoolerType;

import nc.enumm.MetaEnums;

/**
 * Allows for proper checking of custom Active Cooler Fluids.
 */
@Mixin(value = MetaEnums.CoolerType.class, remap = false)
public class CoolerTypeMixin implements AccessibleCoolerType {

    @Shadow
    private int id;

    @Shadow
    private String fluidName;

    @Inject(method = "getFluidName", at = @At("HEAD"), cancellable = true)
    private void getProperFluidName(CallbackInfoReturnable<String> cir) {
        if (id == 0)
            cir.setReturnValue(fluidName);
        else
            cir.setReturnValue(NCActiveCoolerHelper.fluidNamesFromIDs.get(id - 1));
    }

    @Override
    @Unique
    public String getOriginalFluidName() {
        return fluidName;
    }
}
