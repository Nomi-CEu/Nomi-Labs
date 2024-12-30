package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;
import com.nomiceu.nomilabs.network.LabsP2PAddAsInputMessage;
import com.nomiceu.nomilabs.network.LabsP2PAddAsOutputMessage;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.*;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

import kotlin.jvm.functions.Function0;

/**
 * Trims text before renaming, handles add as input/output, properly refreshes renames in gui,
 * better handling of renaming and mouse clicking.
 */
@Mixin(value = WidgetP2PColumn.class, remap = false)
public abstract class WidgetP2PColumnMixin {

    @Shadow
    @Final
    private InfoList infos;

    @Shadow
    @Final
    private Function0<BetterMemoryCardModes> mode;

    @Shadow
    @Final
    private IGuiTextField renameBar;

    @Shadow
    @Final
    private GuiAdvancedMemoryCard gui;

    @Shadow
    protected abstract void onBindButtonClicked(InfoWrapper info);

    @Shadow
    protected abstract void onUnbindButtonClicked(InfoWrapper info);

    @Shadow
    protected abstract void onRenameButtonClicked(InfoWrapper info, int index);

    @Shadow
    public abstract void finishRename();

    @Shadow
    protected abstract void onSelectButtonClicked(WidgetP2PDevice widget, InfoWrapper info, int mouseButton);

    @Unique
    private static final int labs$renameBarX = 50;

    @Unique
    private static final int labs$renameBarY = 1;

    @Inject(method = "onRenameButtonClicked", at = @At("TAIL"))
    private void setSelectionAndPos(InfoWrapper info, int index, CallbackInfo ci) {
        // Reposition rename bar. taking into account internal padding
        renameBar.x += 1;
        renameBar.y += 1;

        // Select all text
        renameBar.setCursorPositionEnd();
        renameBar.setSelectionPos(0);
    }

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

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void newMouseClickedLogic(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        ci.cancel();

        // Changes from original:
        // Fix rename bar 'click area' being 1 pixel too tall
        // Only perform 'onRenameButtonClicked' if the rename bar is in a new location or just became visible
        // Only 'click' rename bar if it didn't change location or visibility
        // Only 'select' widget if rename bar wasn't unselected
        // Faster logic

        boolean renameClicked = false;
        boolean renameChanged = false;
        WidgetP2PDevice toSelect = null;

        for (int i = 0; i < labs$getThis().getEntries().size(); i++) {
            WidgetP2PDevice widget = labs$getThis().getEntries().get(i);
            InfoWrapper info = widget.getInfoSupplier().invoke();

            if (info == null) continue;

            var wX = (double) widget.getX();
            var wY = (double) widget.getY();

            // Outside Widget Area
            if (!(mouseX > wX && mouseX < wX + P2PEntryConstants.WIDTH && mouseY > wY &&
                    mouseY < wY + P2PEntryConstants.HEIGHT))
                continue;

            // Anything past here, we can break
            // Mouse will only affect this widget

            // Case 1: Bind Button
            if (info.getBindButton().mousePressed(gui.mc, mouseX, mouseY)) {
                onBindButtonClicked(info);
                break;
            }

            // Case 2: Unbind Button
            if (info.getUnbindButton().mousePressed(gui.mc, mouseX, mouseY)) {
                onUnbindButtonClicked(info);
                break;
            }

            // Case 3: Renaming
            if (mouseX > wX + labs$renameBarX && mouseX < wX + labs$renameBarX + renameBar.width &&
                    mouseY > wY + labs$renameBarY && mouseY < wY + labs$renameBarY + renameBar.height) {
                renameClicked = true;

                // Same rename bar as before
                // Ignore
                if (renameBar.info == info) break;

                finishRename();
                widget.setRenderNameTextfield(false);
                onRenameButtonClicked(info, i);
                renameChanged = true;
                break;
            }

            // Case 4: Selection
            toSelect = widget;
            break;
        }

        if (renameClicked) {
            if (!renameChanged)
                renameBar.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (renameBar.getVisible())
            finishRename();
        else if (toSelect != null)
            onSelectButtonClicked(toSelect, toSelect.getInfoSupplier().invoke(), mouseButton);
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

    @Unique
    private WidgetP2PColumn labs$getThis() {
        return (WidgetP2PColumn) (Object) this;
    }
}
