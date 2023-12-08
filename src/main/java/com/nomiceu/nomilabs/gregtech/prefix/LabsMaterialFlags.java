package com.nomiceu.nomilabs.gregtech.prefix;

import gregtech.api.unification.material.info.MaterialFlag;
import gregtech.api.unification.material.properties.PropertyKey;

public class LabsMaterialFlags {
    public static MaterialFlag GENERATE_PERFECT_GEM;

    public static void init() {
        GENERATE_PERFECT_GEM = new MaterialFlag.Builder("generate_perfect_gem")
                .requireProps(PropertyKey.GEM).build();
    }
}
