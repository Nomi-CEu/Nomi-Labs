package com.nomiceu.nomilabs.mixin.gregtech;

import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = MetaItems.class, remap = false)
public interface MetaItemsMixin {
    @Accessor(value = "orePrefixes")
    static List<OrePrefix> getOrePrefixes() {
        throw new NotImplementedException("MetaItemsMixin failed to apply!");
    }
}
