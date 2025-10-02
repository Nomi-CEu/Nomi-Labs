package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.fluids.client.gui.GuiFluidTerminal;

/**
 * Allows Auto-Focusing Fluid Terminal. Allows Repeat Keyboard Events.
 */
@Mixin(value = GuiFluidTerminal.class, remap = false)
public abstract class GuiFluidTerminalMixin extends AEBaseGui {

    @Shadow
    private MEGuiTextField searchField;

    /**
     * Default Ignored Constructor
     */
    private GuiFluidTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        searchField.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusFluid);
        Keyboard.enableRepeatEvents(true);
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"), remap = true)
    private void disableRepeat(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(false);
    }
}
