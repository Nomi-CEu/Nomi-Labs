package com.nomiceu.nomilabs.mixinhelper;

import java.util.Map;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

public interface RemappableForgeRegistry {

    void addRemapped(int id, ResourceLocation key);

    Set<Integer> getBlocked();

    Map<Integer, ResourceLocation> getRemapped();
}
