package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGuiAdvancedMemoryCard;
import com.nomiceu.nomilabs.integration.betterp2p.ExtendedITypeReceiver;
import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;
import com.nomiceu.nomilabs.network.LabsP2PChangeTypeMessage;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.P2PEntryConstants;
import com.projecturanus.betterp2p.client.gui.widget.WidgetP2PDevice;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

import kotlin.jvm.functions.Function0;

/**
 * Changes Background colors, handles button visibilities for new modes.
 */
@Mixin(value = WidgetP2PDevice.class, remap = false)
public abstract class WidgetP2PDeviceMixin implements ExtendedITypeReceiver {

    @Shadow
    protected abstract InfoWrapper getSelectedInfo();

    @Shadow
    @Final
    private GuiAdvancedMemoryCard gui;

    @Shadow
    @Final
    private Function0<InfoWrapper> infoSupplier;

    @Shadow
    private int y;
    @Shadow
    private int x;
    @Unique
    private static final int LABS_INPUT_COLOR = 0x7f6d9cf8;

    @Unique
    private static final int LABS_OUTPUT_COLOR = 0x7fecb36c;

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V",
                     remap = true))
    private void drawNewBackgrounds(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        var info = infoSupplier.invoke();
        boolean hasSelected = getSelectedInfo() != null;

        if (hasSelected && getSelectedInfo().getLoc() == info.getLoc()) {
            GuiScreen.drawRect(x, y, x + P2PEntryConstants.WIDTH, y + P2PEntryConstants.HEIGHT,
                    P2PEntryConstants.SELECTED_COLOR);
        } else if (info.getError()) {
            // P2P output without an input, or unbound
            GuiScreen.drawRect(x, y, x + P2PEntryConstants.WIDTH, y + P2PEntryConstants.HEIGHT,
                    P2PEntryConstants.ERROR_COLOR);
        } else if (hasSelected && getSelectedInfo().getFrequency() == info.getFrequency() && info.getFrequency() != 0) {
            // Show input/output
            GuiScreen.drawRect(x, y, x + P2PEntryConstants.WIDTH, y + P2PEntryConstants.HEIGHT,
                    info.getOutput() ? LABS_OUTPUT_COLOR : LABS_INPUT_COLOR);
        }
    }

    @Redirect(method = "render",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawRect(IIIII)V", remap = true))
    private void cancelOldBackgroundDraws(int left, int top, int right, int bottom, int color) {}

    @Inject(method = "updateButtonVisibility", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void handleAddInputVisibility(CallbackInfo ci, @Local(ordinal = 0) InfoWrapper info,
                                          @Local(ordinal = 0) BetterMemoryCardModes mode) {
        if (info == null || getSelectedInfo() == null) return;
        if (mode == LabsBetterMemoryCardModes.ADD_AS_INPUT || mode == LabsBetterMemoryCardModes.ADD_AS_OUTPUT) {
            // Bind Button should be visible if the device is not the selected,
            // and is bound to different frequency than selected
            info.getBindButton().enabled = info.getLoc() != getSelectedInfo().getLoc() &&
                    info.getFrequency() != (short) 0 &&
                    info.getFrequency() != getSelectedInfo().getFrequency() &&
                    info.getType() == getSelectedInfo().getType();
            info.getUnbindButton().enabled = false;
        }
    }

    @Override
    public void acceptIsInput(boolean isInput) {
        var info = infoSupplier.invoke();
        if (info != null) {
            LabsNetworkHandler.NETWORK_HANDLER.sendToServer(new LabsP2PChangeTypeMessage(info.getLoc(), isInput));
        }
        ((AccessibleGuiAdvancedMemoryCard) (Object) gui).labs$closeTypeSelector();
    }
}
