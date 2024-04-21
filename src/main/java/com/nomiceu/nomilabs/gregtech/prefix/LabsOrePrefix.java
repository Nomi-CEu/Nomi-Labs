package com.nomiceu.nomilabs.gregtech.prefix;

import static gregtech.api.GTValues.M;
import static gregtech.api.unification.ore.OrePrefix.Flags.ENABLE_UNIFICATION;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;

@SuppressWarnings("unused")
public class LabsOrePrefix extends OrePrefix {

    public static OrePrefix GEM_PERFECT;

    public LabsOrePrefix(String name, long materialAmount, @Nullable Material material,
                         @Nullable MaterialIconType materialIconType, long flags,
                         @Nullable Predicate<Material> condition) {
        super(name, materialAmount, material, materialIconType, flags, condition);
        MetaItems.addOrePrefix(this);
    }

    public static void init() {
        GEM_PERFECT = new LabsOrePrefix("gemPerfect", M * 8, null, new MaterialIconType("gemPerfect"),
                ENABLE_UNIFICATION, mat -> mat.hasFlag(LabsMaterialFlags.GENERATE_PERFECT_GEM));
    }
}
