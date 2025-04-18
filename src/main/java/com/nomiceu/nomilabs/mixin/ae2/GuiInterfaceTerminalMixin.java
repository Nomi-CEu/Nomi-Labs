package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiInterfaceTerminal;
import appeng.client.gui.widgets.MEGuiTooltipTextField;

/**
 * Focuses the Interface Terminal on entry, so that keybinds (like Bogosort Cfg Keybind) doesn't register. Allows repeat
 * keyboard events. Fixes PMT Buttons.
 */
@Mixin(value = GuiInterfaceTerminal.class, remap = false)
public abstract class GuiInterfaceTerminalMixin extends AEBaseGui {

    @Shadow
    @Final
    private MEGuiTooltipTextField searchFieldNames;

    /**
     * Default Ignored Constructor
     */
    public GuiInterfaceTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(true);
        searchFieldNames.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusInterface);
    }

    @Unique
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
