package com.nomiceu.nomilabs.integration.draconicevolution;

import net.minecraft.util.math.BlockPos;

import com.brandon3055.brandonscore.lib.Vec3I;

public interface ImprovedTileEnergyCore {

    /**
     * @return True if the core has an active builder, false otherwise.
     */
    boolean hasActiveBuilder();

    /**
     * @return True if the core has an active destructor, false otherwise.
     */
    boolean hasActiveDestructor();

    void setExpectedBlockString(String string);

    void setExpectedBlockPos(BlockPos pos);

    String getExpectedBlockString();

    Vec3I getExpectedBlockPos();
}
