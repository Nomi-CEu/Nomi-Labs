package com.nomiceu.nomilabs.mixin.betterp2p;

import com.nomiceu.nomilabs.integration.betterp2p.LabsClientCache;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import kotlin.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetTypeSelector;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows accessing needed functions and fields. Also fills up LabsClientCache.
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

    @Shadow
    protected abstract InfoWrapper getSelectedInfo();

    @Shadow
    @Final
    private InfoList infos;

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

    @Inject(method = "refreshOverlay", at = @At("HEAD"))
    private void fillLabsCache(CallbackInfo ci) {
        LabsClientCache.inputLoc.clear();
        LabsClientCache.outputLoc.clear();

        var selected = getSelectedInfo();
        if (selected == null) return;

        infos.getSorted().stream()
                .filter(info -> info.getFrequency() == selected.getFrequency())
                .filter(info -> info.getType() == selected.getType())
                .filter(info -> info.getLoc().getDim() == selected.getLoc().getDim())
                .filter(info -> !info.equals(selected))
                .map(info -> new Pair<>(info.getOutput() ? LabsClientCache.outputLoc : LabsClientCache.inputLoc,
                        info.getLoc()))
                .forEach(pair -> pair.getFirst().add(new Pair<>(pair.getSecond().getPos(), pair.getSecond().getFacing())));
    }
}
