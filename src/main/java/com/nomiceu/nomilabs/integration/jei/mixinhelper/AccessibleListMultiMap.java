package com.nomiceu.nomilabs.integration.jei.mixinhelper;

import java.util.List;

public interface AccessibleListMultiMap<K, V> {

    void labs$replaceKey(K key, List<V> value);
}
