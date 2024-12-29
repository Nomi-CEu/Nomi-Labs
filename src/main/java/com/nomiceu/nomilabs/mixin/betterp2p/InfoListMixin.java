package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleInfoList;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleInfoWrapper;
import com.nomiceu.nomilabs.integration.betterp2p.SortModes;
import com.projecturanus.betterp2p.client.gui.Filter;
import com.projecturanus.betterp2p.client.gui.InfoFilter;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.WidgetScrollBar;
import com.projecturanus.betterp2p.network.data.P2PLocation;

/**
 * Handles updating infos' distance to player, and allows for custom sorting, including with error info.
 * Improves handling of scrollbar after refresh, reload, or refilter.
 */
@Mixin(value = InfoList.class, remap = false)
public abstract class InfoListMixin implements AccessibleInfoList {

    @Shadow
    @Final
    private HashMap<P2PLocation, InfoWrapper> masterMap;

    @Shadow
    @Final
    private InfoFilter filter;

    @Shadow
    @Nullable
    public abstract InfoWrapper getSelectedInfo();

    @Shadow
    protected abstract String getSearchStr();

    @Unique
    private Vec3d labs$playerPos;

    @Unique
    private SortModes labs$sortMode = SortModes.DEFAULT;

    @Unique
    private boolean labs$sortReversed = false;

    @Unique
    @Override
    public void labs$setSortMode(SortModes mode) {
        this.labs$sortMode = mode;
    }

    @Unique
    @Override
    public SortModes labs$getSortMode() {
        return labs$sortMode;
    }

    @Unique
    @Override
    public void labs$changeSortMode(boolean forwards) {
        int newModeOrdinal = labs$sortMode.ordinal() + (forwards ? 1 : -1);
        int maxSize = SortModes.values().length;

        if (newModeOrdinal < 0) newModeOrdinal = maxSize - 1;
        else newModeOrdinal = newModeOrdinal % maxSize;

        labs$sortMode = SortModes.values()[newModeOrdinal];
    }

    @Unique
    @Override
    public boolean labs$getSortReversed() {
        return labs$sortReversed;
    }

    @Unique
    @Override
    public void labs$setSortReversed(boolean reversed) {
        this.labs$sortReversed = reversed;
    }

    @Unique
    @Override
    public void labs$setPlayerPos(Vec3d pos) {
        labs$playerPos = pos;
        labs$calcDistFor(masterMap.values());
    }

    @Unique
    @Override
    public void labs$properlyResetScrollbar(WidgetScrollBar scrollBar, int numEntries) {
        // numEntries is the amount of entries visible
        // So we subtract from the total size (if 16 entries, but 5 are visible, we only need 11 scrolls)
        var size = Math.max(0, labs$getThis().getFiltered().size() - numEntries);
        // pageSize seems to be unused, or set to either 1 or 23
        scrollBar.setRange(0, size, 23);

        // Reset scroll position
        scrollBar.setCurrentScroll(0);
    }

    @Unique
    private void labs$calcDistFor(Iterable<InfoWrapper> infos) {
        for (InfoWrapper info : infos) {
            ((AccessibleInfoWrapper) (Object) info).labs$calculateDistance(labs$playerPos);
        }
    }

    @Unique
    private void labs$checkInfo() {
        // Do for all, as this is pre-filtering
        // We can use sorted list here, as values have been added, but not sorted yet
        for (InfoWrapper info : labs$getThis().getSorted()) {
            // Ignore unbound, classified as different error type
            if (info.getFrequency() == 0) {
                info.setError(false);
                continue;
            }

            // Search for same freq, and opposite output state (output search for input, etc.)
            int connections = labs$amount(info.getFrequency(), !info.getOutput());
            info.setError(connections == 0);
            ((AccessibleInfoWrapper) (Object) info).labs$setConnectionAmt(connections);
        }
    }

    @Unique
    private int labs$amount(int freq, boolean isOutput) {
        int amount = 0;
        for (InfoWrapper info : labs$getThis().getSorted()) {
            if (info.getFrequency() == freq && info.getOutput() == isOutput)
                amount++;
        }
        return amount;
    }

    @Inject(method = "refresh",
            at = @At(value = "INVOKE", target = "Lcom/projecturanus/betterp2p/client/gui/InfoList;resort()V"),
            require = 1)
    private void checkInfoBeforeSort1(CallbackInfo ci) {
        labs$checkInfo();
    }

    @Inject(method = "rebuild", at = @At("HEAD"))
    private void calcDistanceInRebuild(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                       CallbackInfo ci) {
        labs$calcDistFor(updateList);
    }

    @Inject(method = "rebuild",
            at = @At(value = "INVOKE", target = "Lcom/projecturanus/betterp2p/client/gui/InfoList;resort()V"),
            require = 1)
    private void checkInfoBeforeSort2(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                      CallbackInfo ci) {
        labs$checkInfo();
    }

    @Inject(method = "rebuild",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/widget/WidgetScrollBar;setRange(III)V"),
            require = 1,
            cancellable = true)
    private void replaceOldRebuildScrollbar(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar,
                                            int numEntries, CallbackInfo ci) {
        ci.cancel();
        labs$properlyResetScrollbar(scrollbar, numEntries);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void calcDistanceInUpdate(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                      CallbackInfo ci) {
        labs$calcDistFor(updateList);
    }

    @Inject(method = "update",
            at = @At(value = "INVOKE", target = "Lcom/projecturanus/betterp2p/client/gui/InfoList;resort()V"),
            require = 1)
    private void checkInfoBeforeSort3(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                      CallbackInfo ci) {
        labs$checkInfo();
    }

    @Inject(method = "update",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/widget/WidgetScrollBar;setRange(III)V"),
            require = 1,
            cancellable = true)
    private void replaceOldUpdateScrollbar(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar,
                                           int numEntries, CallbackInfo ci) {
        ci.cancel();
        labs$properlyResetScrollbar(scrollbar, numEntries);
    }

    @Inject(method = "resort", at = @At("HEAD"), cancellable = true)
    private void customSortLogic(CallbackInfo ci) {
        var sorter = labs$sortMode.getComp(getSelectedInfo());
        if (labs$sortReversed) sorter = sorter.reversed();

        labs$getThis().getSorted().sort(sorter);
        ci.cancel();
    }

    @Inject(method = "refilter",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void newFilterLogic(CallbackInfo ci) {
        ci.cancel();

        // Shortcut
        String toSearch = getSearchStr().trim();
        if (toSearch.isEmpty()) {
            labs$getThis().setFiltered(ImmutableList.copyOf(labs$getThis().getSorted()));
            return;
        }

        var sorter = labs$sortMode.getComp(getSelectedInfo());
        if (labs$sortReversed) sorter = sorter.reversed();

        filter.updateFilter(toSearch.toLowerCase());
        labs$getThis().setFiltered(labs$getThis().getSorted().stream()
                .filter(info -> {
                    if (getSelectedInfo() != null && info.getLoc().equals(getSelectedInfo().getLoc())) return true;

                    for (var entry : filter.getActiveFilters().entrySet()) {
                        // Special Case: Bound
                        // Check for Errors as well as Unbound
                        if (entry.getKey() == Filter.BOUND) {
                            return info.getFrequency() != 0 && !info.getError();
                        }

                        // Normal Filter
                        if (!entry.getKey().getFilter().invoke(info, entry.getValue())) return false;
                    }
                    return true;
                }).sorted(sorter)
                .collect(Collectors.toList()));
    }

    @Unique
    private InfoList labs$getThis() {
        return (InfoList) (Object) this;
    }
}
