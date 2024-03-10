package com.nomiceu.nomilabs.gregtech.mixinhelper;

public interface AccessibleEnergyContainerList {
    /**
     * The total amperage of all the containers with the highest input voltage.
     */
    long getTotalHighestInputAmperage();
}
