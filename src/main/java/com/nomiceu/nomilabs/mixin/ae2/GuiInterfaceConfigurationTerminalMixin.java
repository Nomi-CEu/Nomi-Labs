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
import appeng.client.gui.implementations.GuiInterfaceConfigurationTerminal;
import appeng.client.gui.widgets.MEGuiTextField;

/**
 * Allows Auto-Focusing of the Interface Configuration Terminal. Allows Not Saving Searches. Allows Repeat Keyboard
 * Events.
 */
@Mixin(value = GuiInterfaceConfigurationTerminal.class, remap = false)
public abstract class GuiInterfaceConfigurationTerminalMixin extends AEBaseGui {

    @Shadow
    private MEGuiTextField searchFieldInputs;

    /**
     * Default Ignored Constructor
     */
    private GuiInterfaceConfigurationTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        searchFieldInputs.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusConfigInterface);
        if (!LabsConfig.modIntegration.ae2TerminalOptions.saveConfigInterfaceSearch)
            searchFieldInputs.setText("");
        Keyboard.enableRepeatEvents(true);
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"), remap = true)
    private void disableRepeat(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(false);
    }
}
