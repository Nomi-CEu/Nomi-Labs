package com.nomiceu.nomilabs.gregtech.prefix;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import static gregtech.api.GTValues.M;
import static gregtech.api.unification.ore.OrePrefix.Flags.ENABLE_UNIFICATION;

public class LabsOrePrefix extends OrePrefix {

    public static final OrePrefix gemPerfect = new LabsOrePrefix("gemPerfect", M * 8, null, new MaterialIconType("gemPerfect"),
            ENABLE_UNIFICATION, mat -> mat.hasFlag(LabsMaterialFlags.GENERATE_PERFECT_GEM));

    public LabsOrePrefix(String name, long materialAmount, @Nullable Material material, @Nullable MaterialIconType materialIconType, long flags, @Nullable Predicate<Material> condition) {
        super(name, materialAmount, material, materialIconType, flags, condition);
        MetaItems.addOrePrefix(this);
    }

    public static void init() {
        // Add any modifications here. This must NOT be removed if empty,
        // as it is also used to ensure static initialization of prefixes
        // at the correct time in loading.
    }
}
