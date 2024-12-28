package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.Comparator;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;

public enum SortModes {

    DEFAULT("nomilabs.gui.advanced_memory_card.sort.default", SortModes::compDefault),
    DISTANCE("nomilabs.gui.advanced_memory_card.sort.distance", wrapComp(SortModes::compareDistThenName)),
    NAME("nomilabs.gui.advanced_memory_card.sort.name", wrapComp((a, b) -> {
        if (a.getName().equals(b.getName())) return compareTypeThenDist(a, b, false);
        return StringUtils.compare(a.getName(), b.getName());
    }));

    private final String translationKey;
    private final Function<InfoWrapper, Comparator<InfoWrapper>> compFromSelected;

    /**
     * Create a Sort Mode.
     *
     * @param key              Translation Key
     * @param compFromSelected Function that takes the selected p2p and returns a comparator.
     *                         You do not need to handle the case where either one is the selected p2p.
     *                         Note that item smaller = in front!
     *                         Selected p2p may be null!
     */
    SortModes(String key, Function<InfoWrapper, Comparator<InfoWrapper>> compFromSelected) {
        this.translationKey = key;
        this.compFromSelected = compFromSelected;
    }

    public String getName() {
        return LabsTranslate.translate(translationKey);
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

    /* Util */
    private static Function<InfoWrapper, Comparator<InfoWrapper>> wrapComp(Comparator<InfoWrapper> comp) {
        return (a) -> comp;
    }

    /* Sorters */
    private static Comparator<InfoWrapper> compDefault(InfoWrapper selected) {
        return (a, b) -> {
            if (selected != null) {
                // Same freq. as selected, priority over rest
                // Checking for unbound is not needed, just have all unbound at front if selected is unbound
                if (a.getFrequency() == selected.getFrequency()) {
                    if (b.getFrequency() != selected.getFrequency()) return -1;
                    return compareTypeThenDist(a, b, true);
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

            return compareTypeThenDist(a, b, true);
        };
    }

    private static int compareTypeThenDist(InfoWrapper a, InfoWrapper b, boolean compareNameBackup) {
        if (a.getOutput() != b.getOutput()) return a.getOutput() ? 1 : -1; // Inputs First

        if (!compareNameBackup)
            return Double.compare(getDistance(a), getDistance(b)); // Furthest Last
        return compareDistThenName(a, b);
    }

    private static int compareDistThenName(InfoWrapper a, InfoWrapper b) {
        double distA = getDistance(a);
        double distB = getDistance(b);

        if (distA == distB) return StringUtils.compare(a.getName(), b.getName());
        return Double.compare(distA, distB);
    }

    private static double getDistance(InfoWrapper info) {
        return ((AccessibleInfoWrapper) (Object) (info)).labs$getDistance();
    }
}
