package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetTypeSelector;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

/**
 * Allows accessing needed functions and fields.
 */
@Mixin(value = GuiAdvancedMemoryCard.class, remap = false)
public abstract class GuiAdvancedMemoryCardMixin implements AccessibleGuiAdvancedMemoryCard {

    @Shadow
    private BetterMemoryCardModes mode;

    @Shadow
    protected abstract void syncMemoryInfo();

    @Shadow
    @Final
    private WidgetTypeSelector typeSelector;

    @Override
    @Unique
    public BetterMemoryCardModes labs$getMode() {
        return mode;
    }

    @Override
    @Unique
    public void labs$setMode(BetterMemoryCardModes mode) {
        this.mode = mode;
    }

    @Override
    @Unique
    public void labs$syncMemoryInfo() {
        syncMemoryInfo();
    }

    @Override
    @Unique
    public void labs$closeTypeSelector() {
        typeSelector.setVisible(false);
    }
}
