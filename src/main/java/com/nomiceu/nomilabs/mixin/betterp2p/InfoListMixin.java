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
import com.projecturanus.betterp2p.client.gui.InfoFilter;
import com.projecturanus.betterp2p.client.gui.InfoList;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.client.gui.widget.WidgetScrollBar;
import com.projecturanus.betterp2p.network.data.P2PLocation;

/**
 * Handles updating infos' distance to player, and allows for custom sorting.
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
    @Override
    public void labs$setPlayerPos(Vec3d pos) {
        labs$playerPos = pos;
        calcDistFor(masterMap.values());
    }

    @Inject(method = "rebuild", at = @At("HEAD"))
    private void calcDistanceInRebuild(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                       CallbackInfo ci) {
        calcDistFor(updateList);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void calcDistanceInUpdate(Collection<InfoWrapper> updateList, WidgetScrollBar scrollbar, int numEntries,
                                      CallbackInfo ci) {
        calcDistFor(updateList);
    }

    @Unique
    private void calcDistFor(Iterable<InfoWrapper> infos) {
        for (InfoWrapper info : infos) {
            ((AccessibleInfoWrapper) (Object) info).labs$calculateDistance(labs$playerPos);
        }
    }

    @Inject(method = "resort", at = @At("HEAD"), cancellable = true)
    private void customSortLogic(CallbackInfo ci) {
        labs$getThis().getSorted().sort(SortModes.DEFAULT.getComp(getSelectedInfo()));
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

        filter.updateFilter(toSearch.toLowerCase());
        labs$getThis().setFiltered(labs$getThis().getSorted().stream()
                .filter(info -> {
                    if (getSelectedInfo() != null && info.getLoc().equals(getSelectedInfo().getLoc())) return true;

                    for (var entry : filter.getActiveFilters().entrySet()) {
                        if (!entry.getKey().getFilter().invoke(info, entry.getValue())) return false;
                    }
                    return true;
                }).sorted(SortModes.DEFAULT.getComp(getSelectedInfo()))
                .collect(Collectors.toList()));
    }

    @Unique
    private InfoList labs$getThis() {
        return (InfoList) (Object) this;
    }
}
