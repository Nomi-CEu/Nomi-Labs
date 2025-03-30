package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.List;

import net.minecraftforge.common.util.EnumHelper;

import com.projecturanus.betterp2p.client.gui.Filter;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;

import kotlin.jvm.functions.Function2;
import kotlin.text.Regex;

public class LabsFilters {

    public static final Filter DISTANCE_LESS = addFilter("DISTANCE_LESS",
            new Regex("\\A@distless=((\\d+(\\.\\d*)?)|(\\.\\d+))m?\\z"), (info, args) -> {
                double distance = Double.MAX_VALUE;

                for (String arg : args) {
                    distance = Math.min(Double.parseDouble(arg), distance);
                }

                return ((AccessibleInfoWrapper) (Object) info).labs$getDistance() <= distance;
            });

    public static final Filter DISTANCE_MORE = addFilter("DISTANCE_MORE",
            new Regex("\\A@distmore=((\\d+(\\.\\d*)?)|(\\.\\d+))m?\\z"), (info, args) -> {
                double distance = 0;

                for (String arg : args) {
                    distance = Math.max(Double.parseDouble(arg), distance);
                }

                return ((AccessibleInfoWrapper) (Object) info).labs$getDistance() >= distance;
            });

    /**
     * Essentially, this loads the class, allowing the above values to be added.
     * <p>
     * If for some reason, the values are needed before this, they will still be loaded, and calling init will have no
     * affect on that.
     */
    public static void postInit() {}

    private static Filter addFilter(String name, Regex regex, Function2<InfoWrapper, List<String>, Boolean> filter) {
        return EnumHelper.addEnum(Filter.class, name,
                new Class<?>[] { Regex.class, Function2.class },
                regex, filter);
    }
}
