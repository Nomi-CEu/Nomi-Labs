package com.nomiceu.nomilabs.mixin.betterquesting;

import javax.swing.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;

import betterquesting.client.gui2.editors.TextEditorFrame;

/**
 * Fixes crashes on specific environments, on cleanroom loader.
 */
@Mixin(value = TextEditorFrame.class, remap = false)
public class TextEditorFrameMixin extends JFrame {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void changeUITheme(CallbackInfo ci) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            return;
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("Failed to set default look and feel, defaulting to cross platform... ", e);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("Failed to set cross platform look and feel!");
            throw new RuntimeException(e);
        }
    }
}
