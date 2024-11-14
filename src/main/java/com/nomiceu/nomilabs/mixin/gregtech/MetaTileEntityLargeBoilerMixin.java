package com.nomiceu.nomilabs.mixin.gregtech;

import gregtech.common.metatileentities.multi.BoilerType;
import gregtech.common.metatileentities.multi.MetaTileEntityLargeBoiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Updates Tooltips for the 10x Fuel Efficiency Increase in {@link BoilerRecipeLogicMixin}.
 */
@Mixin(value = MetaTileEntityLargeBoiler.class, remap = false)
public class MetaTileEntityLargeBoilerMixin {
    @Redirect(method = "addInformation", at = @At(value = "INVOKE", target = "Lgregtech/common/metatileentities/multi/BoilerType;runtimeBoost(I)I", remap = false), remap = true)
    private int getNewRuntimeBoost(BoilerType instance, int ticks) {
        return instance.runtimeBoost(ticks * 10);
    }
}
