package com.nomiceu.nomilabs.integration.betterp2p;

import net.minecraft.util.math.Vec3d;

public interface AccessibleInfoWrapper {

    void labs$calculateDistance(Vec3d playerPos, int dim);

    double labs$getDistance();

    boolean labs$isDifferentDim();

    String labs$getDimensionName();

    void labs$setConnectionAmt(int amt);
}
