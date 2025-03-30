package com.nomiceu.nomilabs.mixin.betterp2p;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGuiAdvancedMemoryCard;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleInfoWrapper;
import com.nomiceu.nomilabs.integration.betterp2p.ExtendedITypeReceiver;
import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;
import com.nomiceu.nomilabs.network.LabsP2PChangeTypeMessage;
import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.P2PEntryConstants;
import com.projecturanus.betterp2p.client.gui.widget.WidgetP2PDevice;

/**
 * Changes Background colors, handles button visibilities for new modes, renders distance information.
 */
@Mixin(value = WidgetP2PDevice.class, remap = false)
public abstract class WidgetP2PDeviceMixin implements ExtendedITypeReceiver {

    @Shadow
    protected abstract InfoWrapper getSelectedInfo();

    @Shadow
    @Final
    private GuiAdvancedMemoryCard gui;

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
        var info = labs$getThis().getInfoSupplier().invoke();
        boolean hasSelected = getSelectedInfo() != null;

        if (hasSelected && getSelectedInfo().getLoc() == info.getLoc()) {
            GuiScreen.drawRect(x, y, x + P2PEntryConstants.WIDTH, y + P2PEntryConstants.HEIGHT,
                    P2PEntryConstants.SELECTED_COLOR);
        } else if (info.getError() || info.getFrequency() == 0) {
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

    @Redirect(method = "render",
              at = @At(value = "INVOKE",
                       target = "Lcom/projecturanus/betterp2p/client/gui/InfoWrapper;getChannels()Ljava/lang/String;"),
              require = 1)
    @Nullable
    private String cancelExistingChannelHandling(InfoWrapper instance) {
        return null;
    }

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/widget/WidgetP2PDevice;updateButtonVisibility()V"),
            require = 1)
    private void renderDistance(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        var info = labs$getThis().getInfoSupplier().invoke();
        var accessible = ((AccessibleInfoWrapper) (Object) info);
        String distString;

        if (accessible.labs$isDifferentDim())
            distString = LabsTranslate.translate("nomilabs.gui.advanced_memory_card.info.dim",
                    accessible.labs$getDimensionName(), info.getLoc().getDim());
        else
            distString = LabsTranslate.translate("nomilabs.gui.advanced_memory_card.info.dist",
                    accessible.labs$getDistance());

        gui.mc.fontRenderer.drawString(distString, x + P2PEntryConstants.LEFT_ALIGN, y + 33, 0);
    }

    @Inject(method = "updateButtonVisibility", at = @At("TAIL"))
    private void handleAddInputVisibility(CallbackInfo ci) {
        var info = labs$getThis().getInfoSupplier().invoke();
        var mode = labs$getThis().getModeSupplier().invoke();
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
        var info = labs$getThis().getInfoSupplier().invoke();
        if (info != null) {
            LabsNetworkHandler.NETWORK_HANDLER.sendToServer(new LabsP2PChangeTypeMessage(info.getLoc(), isInput));
        }
        ((AccessibleGuiAdvancedMemoryCard) (Object) gui).labs$closeTypeSelector();
    }

    @Unique
    private WidgetP2PDevice labs$getThis() {
        return (WidgetP2PDevice) (Object) this;
    }
}
