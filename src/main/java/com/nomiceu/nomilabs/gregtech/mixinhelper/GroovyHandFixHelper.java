package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;

public class GroovyHandFixHelper {

    public static String getRlPrefix(Material material) {
        return material.getModid().equals(GTValues.MODID) ? "" : material.getModid() + ":";
    }
}
