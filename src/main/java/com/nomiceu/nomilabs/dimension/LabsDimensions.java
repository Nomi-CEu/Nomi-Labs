package com.nomiceu.nomilabs.dimension;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class LabsDimensions {

    public static final int VOID_ID = 119;

    public static DimensionType VOID;

    public static void register() {
        VOID = DimensionType.register("void_world", "_void", VOID_ID, LabsVoidWorldProvider.class, false);
        DimensionManager.registerDimension(VOID_ID, VOID);
    }
}
