package com.nomiceu.nomilabs.mixinhelper;

import java.util.Map;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

public interface RemappableForgeRegistry {

    void labs$addRemapped(int id, ResourceLocation key);

    Set<Integer> labs$getBlocked();

    Map<Integer, ResourceLocation> labs$getRemapped();
}
