package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiFluidInterfaceConfigurationTerminal;
import appeng.client.gui.widgets.MEGuiTextField;

/**
 * Allows Auto-Focusing of the Fluid Interface Configuration Terminal. Allows Not Saving Searches.
 */
@Mixin(value = GuiFluidInterfaceConfigurationTerminal.class, remap = false)
public abstract class GuiFluidInterfaceConfigurationTerminalMixin extends AEBaseGui {

    @Shadow
    private MEGuiTextField searchFieldInputs;

    /**
     * Default Ignored Constructor
     */
    public GuiFluidInterfaceConfigurationTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        searchFieldInputs.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusConfigFluidInterface);
        if (!LabsConfig.modIntegration.ae2TerminalOptions.saveConfigInterfaceSearch)
            searchFieldInputs.setText("");
    }
}
