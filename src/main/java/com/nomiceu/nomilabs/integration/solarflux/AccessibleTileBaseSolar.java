package com.nomiceu.nomilabs.integration.solarflux;

import net.minecraft.util.EnumFacing;

public interface AccessibleTileBaseSolar {

    void labs$addBlacklistSide(EnumFacing facing);

    long labs$rawEnergy();

    void labs$setEnergy(long amt);
}
