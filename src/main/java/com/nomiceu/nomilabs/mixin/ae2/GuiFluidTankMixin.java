package com.nomiceu.nomilabs.mixin.ae2;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.ae2.LabsSpecialSlotIngredient;

import appeng.api.storage.data.IAEFluidStack;
import appeng.fluids.client.gui.widgets.GuiFluidTank;

/**
 * Applies <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/451">AE2 #451</a> for v0.56.5.
 */
@Mixin(value = GuiFluidTank.class, remap = false)
public abstract class GuiFluidTankMixin implements LabsSpecialSlotIngredient {

    @Shadow
    public abstract IAEFluidStack getFluidStack();

    @Override
    public @Nullable Object labs$getIngredient() {
        return getFluidStack() == null ? null : getFluidStack().getFluidStack();
    }
}
