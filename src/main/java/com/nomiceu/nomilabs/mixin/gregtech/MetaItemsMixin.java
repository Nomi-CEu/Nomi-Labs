package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;

/**
 * Allows getting the Ore Prefixes used in Generating Meta Items for Materials.
 */
@Mixin(value = MetaItems.class, remap = false)
public interface MetaItemsMixin {

    @Accessor(value = "orePrefixes")
    static List<OrePrefix> getOrePrefixes() {
        throw new NotImplementedException("MetaItemsMixin failed to apply!");
    }
}
