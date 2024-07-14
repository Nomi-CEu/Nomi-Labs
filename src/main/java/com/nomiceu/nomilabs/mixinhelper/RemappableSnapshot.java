package com.nomiceu.nomilabs.mixinhelper;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

public interface RemappableSnapshot {

    void addRemapped(int id, ResourceLocation key);

    void addAllRemapped(Map<Integer, ResourceLocation> map);

    void loadToRegistry(RemappableForgeRegistry reg);
}
