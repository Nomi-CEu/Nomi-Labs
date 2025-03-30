package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.ArrayList;
import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.betterp2p.LabsFilters;
import com.projecturanus.betterp2p.client.gui.InfoFilter;

import kotlin.sequences.Sequence;
import kotlin.text.MatchResult;

@Mixin(value = InfoFilter.class, remap = false)
public class InfoFilterMixin {

    @Inject(method = "updateFilter",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            require = 1)
    private void handleLabsFilter(String query, CallbackInfo ci, @Local Sequence<MatchResult> tokens) {
        Iterator<MatchResult> iter = tokens.iterator();

        while (iter.hasNext()) {
            String token = iter.next().getValue();

            // It would be nice if we could remove these values
            // So they don't have to be regex matched
            // But sequence is read only
            if (token.contains("@")) {
                if (LabsFilters.DISTANCE_LESS.getPattern().matches(token)) {
                    MatchResult result = LabsFilters.DISTANCE_LESS.getPattern().find(token, 0);
                    labs$getThis().getActiveFilters().computeIfAbsent(LabsFilters.DISTANCE_LESS,
                            (k) -> new ArrayList<>());

                    labs$getThis().getActiveFilters().get(LabsFilters.DISTANCE_LESS)
                            .add(result.getGroupValues().get(1));
                } else if (LabsFilters.DISTANCE_MORE.getPattern().matches(token)) {
                    MatchResult result = LabsFilters.DISTANCE_MORE.getPattern().find(token, 0);
                    labs$getThis().getActiveFilters().computeIfAbsent(LabsFilters.DISTANCE_MORE,
                            (k) -> new ArrayList<>());

                    labs$getThis().getActiveFilters().get(LabsFilters.DISTANCE_MORE)
                            .add(result.getGroupValues().get(1));
                }
            }
        }
    }

    @Unique
    private InfoFilter labs$getThis() {
        return (InfoFilter) (Object) this;
    }
}
