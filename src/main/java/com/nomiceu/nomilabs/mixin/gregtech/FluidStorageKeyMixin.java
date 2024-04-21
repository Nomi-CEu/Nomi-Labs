package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.api.fluids.store.FluidStorageKey;

/**
 * Allows getting all possible FluidStorageKey options.
 */
@Mixin(value = FluidStorageKey.class, remap = false)
public interface FluidStorageKeyMixin {

    @Accessor(value = "keys")
    static Map<ResourceLocation, FluidStorageKey> getKeys() {
        throw new NotImplementedException("FluidStorageKeyMixin failed to apply!");
    }
}
