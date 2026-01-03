package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;

import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectLinkedOpenHashMap;

public enum SortModes {

    DEFAULT("nomilabs.gui.advanced_memory_card.sort.default", SortModes::compDefault),
    DISTANCE("nomilabs.gui.advanced_memory_card.sort.distance", wrapComp(SortModes::compareDistThenName)),
    NAME("nomilabs.gui.advanced_memory_card.sort.name", wrapComp((a, b) -> {
        if (a.getName().equals(b.getName())) return compareTypeThenDist(a, b);
        return StringUtils.compareIgnoreCase(a.getName(), b.getName());
    }));

    private final String translationKey;
    private final BiConsumer<InfoWrapper, List<InfoWrapper>> sortFunc;

    /**
     * Create a Sort Mode.
     *
     * @param key      Translation Key
     * @param sortFunc Function that takes the selected p2p, and p2p list, and sorts the list.
     *                 You do not need to handle the case where either one is the selected p2p.
     *                 Sort the p2p list IN PLACE.
     *                 Selected p2p may be null!
     */
    SortModes(String key, BiConsumer<InfoWrapper, List<InfoWrapper>> sortFunc) {
        this.translationKey = key;
        this.sortFunc = sortFunc;
    }

    public String getName() {
        return LabsTranslate.translate(translationKey);
    }

    public void applySort(InfoWrapper selected, List<InfoWrapper> list, boolean reversed) {
        sortFunc.accept(selected, list);
        if (reversed) Collections.reverse(list);

        placeSelectedFirst(selected, list);
    }

    /* Util */
    private static void placeSelectedFirst(InfoWrapper selected, List<InfoWrapper> list) {
        if (selected == null) return;

        var iter = list.iterator();
        while (iter.hasNext()) {
            if (iter.next().getLoc() == selected.getLoc()) {
                iter.remove();
                break;
            }
        }

        list.add(0, selected);
    }

    private static BiConsumer<InfoWrapper, List<InfoWrapper>> wrapComp(Comparator<InfoWrapper> comp) {
        return (a, list) -> list.sort(comp);
    }

    /* Sorters */
    private static void compDefault(InfoWrapper selected, List<InfoWrapper> list) {
        // First, sort list by type & dist
        list.sort(SortModes::compareTypeThenDist);

        // Group by freq
        // Use a linked hash map so the entries are in order;
        // e.g. frequencies sorted by closest input,
        // linked to all p2ps of that freq sorted by type & dist
        Map<Short, List<InfoWrapper>> grouped = new Short2ObjectLinkedOpenHashMap<>();
        for (var info : list) {
            grouped.computeIfAbsent(info.getFrequency(), (_key) -> new ObjectArrayList<>()).add(info);
        }

        list.clear();

        // Put selected frequency first
        if (selected != null) {
            List<InfoWrapper> selectedFrequencyP2ps = grouped.get(selected.getFrequency());

            if (selectedFrequencyP2ps != null) {
                list.addAll(selectedFrequencyP2ps);
                grouped.remove(selected.getFrequency());
            }
        }

        // Add all other p2ps
        for (var groupedList : grouped.values()) {
            list.addAll(groupedList);
        }
    }

    private static int compareTypeThenDist(InfoWrapper a, InfoWrapper b) {
        if (a.getOutput() != b.getOutput()) return a.getOutput() ? 1 : -1; // Inputs First

        return compareDistThenName(a, b);
    }

    private static int compareDistThenName(InfoWrapper a, InfoWrapper b) {
        int compDist = compareDistance(a, b);

        if (compDist == 0) return StringUtils.compareIgnoreCase(a.getName(), b.getName());
        return compDist;
    }

    /**
     * Compares distance, but checks for different dimensions first.
     */
    private static int compareDistance(InfoWrapper a, InfoWrapper b) {
        if (getAccess(a).labs$isDifferentDim()) {
            if (getAccess(b).labs$isDifferentDim())
                return Integer.compare(a.getLoc().getDim(), b.getLoc().getDim());
            return 1; // Different Dimensions Last
        }
        if (getAccess(b).labs$isDifferentDim())
            return -1; // Different Dimensions Last

        return Double.compare(getAccess(a).labs$getDistance(), getAccess(b).labs$getDistance());
    }

    private static AccessibleInfoWrapper getAccess(InfoWrapper info) {
        return ((AccessibleInfoWrapper) (Object) (info));
    }
}
