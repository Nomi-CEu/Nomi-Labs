package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.integration.jei.mixinhelper.AccessibleListMultiMap;

import mezz.jei.collect.ListMultiMap;
import mezz.jei.collect.MultiMap;

/**
 * Allows directly replacing the list in a map. Used for recipe catalyst override.
 */
@Mixin(value = ListMultiMap.class, remap = false)
public class ListMultiMapMixin<K, V> extends MultiMap<K, V, List<V>> implements AccessibleListMultiMap<K, V> {

    /**
     * Mandatory Ignored Constructor
     */
    private ListMultiMapMixin(Supplier<List<V>> collectionSupplier) {
        super(collectionSupplier);
    }

    @Unique
    @Override
    public void labs$replaceKey(K key, List<V> value) {
        map.put(key, value);
    }
}
