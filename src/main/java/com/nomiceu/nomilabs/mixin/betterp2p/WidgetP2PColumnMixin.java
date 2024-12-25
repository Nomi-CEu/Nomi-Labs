package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;
import com.nomiceu.nomilabs.network.LabsP2PAddAsInputMessage;
import com.nomiceu.nomilabs.network.LabsP2PAddAsOutputMessage;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.IGuiTextField;
import com.projecturanus.betterp2p.client.gui.widget.WidgetP2PColumn;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

import kotlin.jvm.functions.Function0;

/**
 * Trims text before renaming, handles add as input/output, properly refreshes renames in gui.
 */
@Mixin(value = WidgetP2PColumn.class, remap = false)
public class WidgetP2PColumnMixin {

    @Shadow
    @Final
    private InfoList infos;

    @Shadow
    @Final
    private Function0<BetterMemoryCardModes> mode;

    @Shadow
    @Final
    private IGuiTextField renameBar;

    @Inject(method = "finishRename", at = @At("HEAD"))
    private void trimText(CallbackInfo ci) {
        renameBar.setText(renameBar.getText().trim());
    }

    @Inject(method = "finishRename",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper;sendToServer(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;)V",
                     shift = At.Shift.AFTER),
            require = 1)
    private void refreshRenameInGui(CallbackInfo ci) {
        renameBar.info.setName(renameBar.getText());
    }

    @Inject(method = "onBindButtonClicked", at = @At("HEAD"), cancellable = true)
    private void handleAddAs(InfoWrapper info, CallbackInfo ci) {
        if (infos.getSelectedInfo() == null) return;

        var currentMode = mode.invoke();
        if (!LabsBetterMemoryCardModes.LABS_ADDED_MODES.containsKey(currentMode)) return;

        if (currentMode == LabsBetterMemoryCardModes.ADD_AS_INPUT)
            LabsNetworkHandler.NETWORK_HANDLER
                    .sendToServer(new LabsP2PAddAsInputMessage(infos.getSelectedInfo().getLoc(), info.getFrequency()));
        else if (currentMode == LabsBetterMemoryCardModes.ADD_AS_OUTPUT)
            LabsNetworkHandler.NETWORK_HANDLER
                    .sendToServer(new LabsP2PAddAsOutputMessage(infos.getSelectedInfo().getLoc(), info.getFrequency()));
        ci.cancel();
    }
}
