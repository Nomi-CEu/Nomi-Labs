package com.nomiceu.nomilabs.mixin.ae2;

import java.io.IOException;

import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiRenamer;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;

/**
 * Saves renames on exit gui through escape, and allows repeat keyboard events.
 */
@Mixin(value = GuiRenamer.class, remap = false)
public abstract class GuiRenamerMixin extends AEBaseGui {

    @Shadow
    private MEGuiTextField textField;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiRenamerMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("HEAD"), remap = true)
    private void enableKeyboard(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(true);
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true, remap = true)
    private void handleEsc(char character, int key, CallbackInfo ci) {
        if (key != Keyboard.KEY_ESCAPE) return;

        try {
            NetworkHandler.instance().sendToServer(new PacketValueConfig("QuartzKnife.ReName", textField.getText()));
        } catch (IOException e) {
            NomiLabs.LOGGER.fatal("[GuiRenamerMixin] Failed to save Ore Regex on exit!");
        }

        mc.player.closeScreen();
        ci.cancel();
    }

    @Unique
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
