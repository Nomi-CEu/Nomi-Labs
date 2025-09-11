package com.nomiceu.nomilabs.mixinhelper;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

public interface RemappableSnapshot {

    void labs$addRemapped(int id, ResourceLocation key);

    void labs$addAllRemapped(Map<Integer, ResourceLocation> map);

    void labs$loadToRegistry(RemappableForgeRegistry reg);
}
