package com.nomiceu.nomilabs.gregtech.multiblock.registry;

import com.nomiceu.nomilabs.gregtech.multiblock.MetaTileEntityGreenhouse;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.common.metatileentities.MetaTileEntities;

public class LabsMultiblocks {
    public static MetaTileEntityGreenhouse GREENHOUSE;
    public static void initOld() {

    }
    public static void initNew() {
        GREENHOUSE = MetaTileEntities.registerMetaTileEntity(32000, new MetaTileEntityGreenhouse(LabsNames.makeLabsName("greenhouse"))); // Get a set id later, use 32000 for now
    }
}
