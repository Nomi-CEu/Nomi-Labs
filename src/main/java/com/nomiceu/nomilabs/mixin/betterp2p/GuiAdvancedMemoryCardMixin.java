package com.nomiceu.nomilabs.mixin.betterp2p;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;
import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.integration.betterp2p.*;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.GuiScale;
import com.projecturanus.betterp2p.client.gui.widget.WidgetButton;
import com.projecturanus.betterp2p.client.gui.widget.WidgetScrollBar;
import com.projecturanus.betterp2p.client.gui.widget.WidgetTypeSelector;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;
import com.projecturanus.betterp2p.network.packet.S2COpenGui;

import kotlin.Pair;

/**
 * Allows accessing needed functions and fields, initializes playerPos field in InfoList, fills up
 * LabsClientCache, cancels existing checks for invalid setups (moved to InfoList, right before sorting),
 * calls proper resetting of scrollbar in custom places, allows for custom filtering hover text,
 * and allow using arrow keys to scroll.
 */
@Mixin(value = GuiAdvancedMemoryCard.class, remap = false)
public abstract class GuiAdvancedMemoryCardMixin extends GuiScreen implements AccessibleGuiAdvancedMemoryCard {

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

    @Shadow
    @Final
    private WidgetScrollBar scrollBar;

    @Shadow
    private GuiScale scale;

    @Shadow
    @Final
    private WidgetButton refreshButton;

    @Shadow
    private int guiLeft;

    @Shadow
    private int guiTop;

    @Shadow
    protected abstract void refreshOverlay();

    @Unique
    private SortModeWidgetButton labs$sortModeButton;

    @Unique
    private SortDirectionWidgetButton labs$sortDirectionButton;

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

    @Override
    @Unique
    public void labs$changeSort(boolean forwards) {
        labs$getAccessibleInfo().labs$changeSortMode(forwards);
        infos.resort();
        infos.refilter();
        refreshOverlay();
    }

    @Override
    @Unique
    public SortModes labs$getSortMode() {
        return labs$getAccessibleInfo().labs$getSortMode();
    }

    @Override
    @Unique
    public void labs$swapSortReversed() {
        labs$getAccessibleInfo().labs$setSortReversed(!labs$getSortReversed());
        infos.resort();
        infos.refilter();
        refreshOverlay();
    }

    @Override
    @Unique
    public boolean labs$getSortReversed() {
        return labs$getAccessibleInfo().labs$getSortReversed();
    }

    @Inject(method = "getSortRules", at = @At("HEAD"), cancellable = true)
    private void getCustomSortRules(CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(ImmutableList.of(
                format(translate("nomilabs.gui.advanced_memory_card.filter.title"), BOLD, UNDERLINE),
                "<name>§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.name"),
                "§9@in§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.input"),
                "§6@out§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.output"),
                "§a@b§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.bound"),
                "§c@u§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.unbound"),
                "§b@distless=<distance>§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.distless"),
                "§d@distmore=<distance>§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.distmore"),
                "§e@type=<type1>;<type2>;...§7 - " + translate("nomilabs.gui.advanced_memory_card.filter.type"),
                "",
                translateFormat("nomilabs.gui.advanced_memory_card.filter.end", GRAY)));
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setup(S2COpenGui msg, CallbackInfo ci) {
        // Cannot use gui.mc at this point
        var player = Minecraft.getMinecraft().player;
        labs$getAccessibleInfo().labs$setPlayerPos(player.getPositionVector(), player.dimension);
        labs$getAccessibleInfo().labs$setSortMode(LabsClientCache.sortMode);
        labs$getAccessibleInfo().labs$setSortReversed(LabsClientCache.sortReversed);

        labs$sortModeButton = new SortModeWidgetButton(labs$getThis(), 0, 0, 32, 32);
        labs$sortDirectionButton = new SortDirectionWidgetButton(labs$getThis(), 0, 0, 32, 32);
    }

    @Inject(method = "initGui", at = @At("HEAD"), remap = true)
    private void clearExistingButtons(CallbackInfo ci) {
        buttonList.clear();
    }

    @Inject(method = "initGui", at = @At("TAIL"), remap = true)
    private void handleEndInit(CallbackInfo ci) {
        labs$properlyResetScrollbar();

        // Refresh button position: below all
        refreshButton.setPosition(guiLeft - 32, guiTop + 162);

        // Add sort change button, above type button
        labs$sortModeButton.setPosition(guiLeft - 32, guiTop + 98);
        buttonList.add(labs$sortModeButton);

        // Add sort direction button, below type button
        labs$sortDirectionButton.setPosition(guiLeft - 32, guiTop + 130);
        buttonList.add(labs$sortDirectionButton);
    }

    @Redirect(method = "initGui",
              at = @At(value = "INVOKE",
                       target = "Lcom/projecturanus/betterp2p/client/gui/GuiAdvancedMemoryCard;checkInfo()V",
                       remap = false),
              require = 1,
              remap = true)
    private void cancelExistingChecksInit(GuiAdvancedMemoryCard instance) {}

    @Redirect(method = "refreshInfo",
              at = @At(value = "INVOKE",
                       target = "Lcom/projecturanus/betterp2p/client/gui/GuiAdvancedMemoryCard;checkInfo()V"),
              require = 1)
    private void cancelExistingChecksRefresh(GuiAdvancedMemoryCard instance) {}

    @Redirect(method = "updateInfo",
              at = @At(value = "INVOKE",
                       target = "Lcom/projecturanus/betterp2p/client/gui/GuiAdvancedMemoryCard;checkInfo()V"),
              require = 1)
    private void cancelExistingChecksUpdate(GuiAdvancedMemoryCard instance) {}

    @Inject(method = "mouseClicked",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/InfoList;refilter()V",
                     shift = At.Shift.AFTER,
                     remap = false),
            require = 1,
            remap = true)
    private void properlyResetScrollbarFilterClear(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        labs$properlyResetScrollbar();
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true, remap = true)
    private void allowArrowScroll(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == Keyboard.KEY_UP) {
            scrollBar.wheel(1);
            ci.cancel();
            return;
        }
        if (keyCode == Keyboard.KEY_DOWN) {
            scrollBar.wheel(-1);
            ci.cancel();
        }
    }

    @Inject(method = "keyTyped",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/InfoList;refilter()V",
                     shift = At.Shift.AFTER,
                     remap = false),
            require = 1,
            remap = true)
    private void properlyResetScrollbarFilterTyped(char typedChar, int keyCode, CallbackInfo ci) {
        labs$properlyResetScrollbar();
    }

    @Inject(method = "refreshOverlay", at = @At("HEAD"))
    private void fillLabsCache(CallbackInfo ci) {
        LabsClientCache.inputLoc.clear();
        LabsClientCache.outputLoc.clear();

        var selected = getSelectedInfo();
        if (selected == null) return;

        LabsClientCache.selectedIsOutput = getSelectedInfo().getOutput();

        // Reset time
        LabsClientCache.lastSelectedRenderChange = System.currentTimeMillis();
        LabsClientCache.renderingSelected = true;

        if (selected.getFrequency() == 0) return;

        infos.getSorted().stream()
                .filter(info -> info.getFrequency() == selected.getFrequency())
                .filter(info -> info.getType() == selected.getType())
                .filter(info -> info.getLoc().getDim() == selected.getLoc().getDim())
                .filter(info -> !info.equals(selected))
                .map(info -> new Pair<>(info.getOutput() ? LabsClientCache.outputLoc : LabsClientCache.inputLoc,
                        info.getLoc()))
                .forEach(pair -> pair.getFirst()
                        .add(new Pair<>(pair.getSecond().getPos(), pair.getSecond().getFacing())));
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"), remap = true)
    private void save(CallbackInfo ci) {
        LabsClientCache.sortMode = labs$getSortMode();
        LabsClientCache.sortReversed = labs$getSortReversed();
    }

    @Unique
    private void labs$properlyResetScrollbar() {
        labs$getAccessibleInfo().labs$properlyResetScrollbar(scrollBar,
                scale.getSize().invoke(height - 75));
    }

    @Unique
    private AccessibleInfoList labs$getAccessibleInfo() {
        return ((AccessibleInfoList) (Object) infos);
    }

    @Unique
    private GuiAdvancedMemoryCard labs$getThis() {
        return (GuiAdvancedMemoryCard) (Object) this;
    }
}
