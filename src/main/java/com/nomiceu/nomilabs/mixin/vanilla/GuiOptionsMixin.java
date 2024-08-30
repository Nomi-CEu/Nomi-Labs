package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.network.LabsDifficultyChangeMessage;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;

/**
 * Fixes Difficulty Button not setting difficulty for all worlds and saving to server.properties.
 */
@Mixin(GuiOptions.class)
public abstract class GuiOptionsMixin extends GuiScreen {

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void handleProperDifficultyChange(GuiButton button, CallbackInfo ci) {
        if (!button.enabled || button.id != 108) return;

        // Set difficulty properly, but do not cancel
        // Let operation set difficulty of world as well, to provide instantaneous change
        LabsNetworkHandler.NETWORK_HANDLER
                .sendToServer(new LabsDifficultyChangeMessage(mc.world.getDifficulty().getId() + 1));
    }
}
