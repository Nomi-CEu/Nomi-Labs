package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.Comparator;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.projecturanus.betterp2p.client.gui.InfoWrapper;

public enum SortModes {

    DEFAULT(SortModes::compDefault);

    private final Function<InfoWrapper, Comparator<InfoWrapper>> compFromSelected;

    /**
     * Create a Sort Mode.
     * 
     * @param compFromSelected Function that takes the selected p2p and returns a comparator.
     *                         You do not need to handle the case where either one is the selected p2p.
     *                         Note that item smaller = in front!
     *                         Selected p2p may be null!
     */
    SortModes(Function<InfoWrapper, Comparator<InfoWrapper>> compFromSelected) {
        this.compFromSelected = compFromSelected;
    }

    public Comparator<InfoWrapper> getComp(InfoWrapper selected) {
        var applyComp = compFromSelected.apply(selected);
        return (a, b) -> {
            if (selected != null) {
                // Selected first
                if (a.getLoc().equals(selected.getLoc())) return -1;
                if (b.getLoc().equals(selected.getLoc())) return 1;
            }

            return applyComp.compare(a, b);
        };
    }

    /* Sorters */
    private static Comparator<InfoWrapper> compDefault(InfoWrapper selected) {
        return (a, b) -> {
            if (selected != null) {
                // Same freq. as selected, priority over rest
                // Checking for unbound is not needed, just have all unbound at front if selected is unbound
                if (a.getFrequency() == selected.getFrequency()) {
                    if (b.getFrequency() != selected.getFrequency()) return -1;
                    return compareTypeThenDist(a, b);
                }

                if (b.getFrequency() == selected.getFrequency()) return 1;
            }

            if (a.getFrequency() != b.getFrequency()) {
                // Unbound first
                if (a.getFrequency() == 0) return -1;
                if (b.getFrequency() == 0) return 1;

                // Errors next, also sorted by frequency (display)
                if (a.getError()) {
                    if (b.getError()) {
                        return StringUtils.compare(a.getFreqDisplay(), b.getFreqDisplay());
                    }
                    return -1;
                }
                if (b.getError()) return 1;

                // Else, sort by frequency (display)
                return StringUtils.compare(a.getFreqDisplay(), b.getFreqDisplay());
            }

            return compareTypeThenDist(a, b);
        };
    }

    private static int compareTypeThenDist(InfoWrapper a, InfoWrapper b) {
        if (a.getOutput() != b.getOutput()) return a.getOutput() ? 1 : -1; // Inputs First

        return getDistance(a) > getDistance(b) ? 1 : -1; // Furthest Last
    }

    private static double getDistance(InfoWrapper info) {
        return ((AccessibleInfoWrapper) (Object) (info)).labs$getDistance();
    }
}
