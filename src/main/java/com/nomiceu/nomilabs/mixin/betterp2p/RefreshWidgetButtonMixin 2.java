package com.nomiceu.nomilabs.mixin.betterp2p;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetButton;

/**
 * Adds a hover tooltip.
 */
@Mixin(targets = "com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard$4", remap = false)
public abstract class RefreshWidgetButtonMixin extends WidgetButton {

    /**
     * Mandatory Ignored Constructor
     */
    private RefreshWidgetButtonMixin(@NotNull GuiAdvancedMemoryCard gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initHoverText(CallbackInfo ci) {
        setHoverText(ImmutableList.of(LabsTranslate.translate("nomilabs.gui.advanced_memory_card.button.refresh")));
    }
}
