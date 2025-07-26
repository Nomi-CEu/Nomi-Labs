package com.nomiceu.nomilabs.mixin.ae2fc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glodblock.github.client.GuiFluidPatternTerminal;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumableButton;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeButtonDisplay;

/**
 * Fixes location of non-consume toggle button.
 */
@Mixin(value = GuiFluidPatternTerminal.class, remap = false)
public class GuiFluidPatternTerminalMixin {

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void relocateInclNonConsumeButton(CallbackInfo ci) {
        InclNonConsumableButton button = ((InclNonConsumeButtonDisplay) this).labs$inclNonConsumeButton();
        button.setAe2Fc();
        button.y += 10;
    }
}
