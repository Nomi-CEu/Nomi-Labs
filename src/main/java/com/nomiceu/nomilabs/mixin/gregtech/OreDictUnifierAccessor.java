package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.stack.ItemAndMetadata;
import gregtech.api.unification.stack.ItemMaterialInfo;

@Mixin(value = OreDictUnifier.class, remap = false)
public interface OreDictUnifierAccessor {

    @Accessor("materialUnificationInfo")
    static Map<ItemAndMetadata, ItemMaterialInfo> getMaterialUnificationInfo() {
        throw new NotImplementedException("OreDictUnifierAccessor failed to apply!");
    }
}
