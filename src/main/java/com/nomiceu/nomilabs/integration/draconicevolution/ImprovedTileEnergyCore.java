package com.nomiceu.nomilabs.integration.draconicevolution;

import net.minecraft.util.math.BlockPos;

import com.brandon3055.brandonscore.lib.Vec3I;

public interface ImprovedTileEnergyCore {

    /**
     * @return True if the core has an active builder, false otherwise.
     */
    boolean labs$hasActiveBuilder();

    /**
     * @return True if the core has an active destructor, false otherwise.
     */
    boolean labs$hasActiveDestructor();

    void labs$setExpectedBlockString(String string);

    void labs$setExpectedBlockPos(BlockPos pos);

    String labs$getExpectedBlockString();

    Vec3I labs$getExpectedBlockPos();
}
