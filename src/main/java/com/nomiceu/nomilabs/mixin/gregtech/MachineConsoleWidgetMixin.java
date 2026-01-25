package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.gui.widgets.WidgetGroup;
import gregtech.api.terminal.gui.widgets.RectButtonWidget;
import gregtech.api.util.Position;
import gregtech.common.terminal.app.console.MachineConsoleWidget;

/**
 * Fixes the rendering of toggle auto-output buttons in the GT Console App.
 * Also, fixes allow input by output not having correct initial state.
 */
@Mixin(value = MachineConsoleWidget.class, remap = false)
public class MachineConsoleWidgetMixin extends WidgetGroup {

    @Unique
    private static final String LABS$OUTPUT_HOVER = "terminal.console.auto_output";

    /**
     * Stop loading of initial state.
     * Instead allow it to be computed on changes detection, meaning that the value (if in a true state) is correctly
     * synced between client and server.
     */
    @Redirect(method = "initWidgets",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/terminal/gui/widgets/RectButtonWidget;setInitValue(Z)Lgregtech/api/terminal/gui/widgets/RectButtonWidget;"))
    private RectButtonWidget cancelLoadingInitialState(RectButtonWidget instance, boolean isPressed) {
        return instance;
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void fixAutoOutputWidgets(CallbackInfo ci) {
        // Re-add item/fluid output after, to prevent CME
        int itemIdx = -1;
        int fluidIdx = -1;

        for (int i = 0; i < widgets.size(); i++) {
            if (!(widgets.get(i) instanceof CircleButtonWidgetAccessor circle))
                continue;

            // Does its hover text match item/fluid output buttons?
            String[] hoverText = circle.labs$getHoverText();
            if (hoverText == null || !(hoverText.length == 1 && LABS$OUTPUT_HOVER.equals(hoverText[0])))
                continue;

            // Check if its an item
            if (((TextureArea) circle.labs$getIcon()).imageLocation == GuiTextures.BUTTON_ITEM_OUTPUT.imageLocation) {
                itemIdx = i;
            } else {
                // It must be a fluid
                fluidIdx = i;
            }
        }

        // Perform replacements
        labs$replaceOutputWidget(itemIdx, "gregtech.gui.item_auto_output.tooltip", GuiTextures.BUTTON_ITEM_OUTPUT);
        labs$replaceOutputWidget(fluidIdx, "gregtech.gui.fluid_auto_output.tooltip", GuiTextures.BUTTON_FLUID_OUTPUT);
    }

    @Unique
    private void labs$replaceOutputWidget(int idx, String tooltip, TextureArea texture) {
        if (idx == -1) return;

        Widget widget = widgets.get(idx);
        widgets.remove(idx);

        var rect = (RectButtonWidgetAccessor) widget;

        // Click data is unused by the original widget handling; simply put in null
        addWidget(idx,
                new ToggleButtonWidget(widget.getSelfPosition().x, widget.getSelfPosition().y, widget.getSize().width,
                        widget.getSize().height,
                        texture, () -> rect.labs$getSupplier().get(),
                        (pressed) -> rect.labs$getOnPressed().accept(null, pressed))
                                .setTooltipText(tooltip)
                                .shouldUseBaseBackground());

        // Perform discard widget actions
        widget.setUiAccess(null);
        widget.setGui(null);
        widget.setSizes(null);
        widget.setParentPosition(Position.ORIGIN);
    }
}
