package com.nomiceu.nomilabs.mixin.ae2;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.ae2.LabsSpecialSlotIngredient;

import appeng.api.storage.data.IAEFluidStack;
import appeng.client.me.SlotFluidME;

/**
 * Applies <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/451">AE2 #451</a> for v0.56.5.
 */
@Mixin(value = SlotFluidME.class, remap = false)
public abstract class SlotFluidMEMixin implements LabsSpecialSlotIngredient {

    @Shadow
    public abstract IAEFluidStack getAEFluidStack();

    @Nullable
    @Override
    public Object labs$getIngredient() {
        return getAEFluidStack() == null ? null : getAEFluidStack().getFluidStack();
    }
}
