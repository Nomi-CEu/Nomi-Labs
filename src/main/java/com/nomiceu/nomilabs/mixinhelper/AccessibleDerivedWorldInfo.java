package com.nomiceu.nomilabs.mixinhelper;

import net.minecraft.world.storage.WorldInfo;

public interface AccessibleDerivedWorldInfo {

    WorldInfo getDelegate();
}
