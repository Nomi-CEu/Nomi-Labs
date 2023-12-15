package com.nomiceu.nomilabs.gregtech.multiblock.registry;

import com.nomiceu.nomilabs.gregtech.multiblock.MetaTileEntityCreativeTankProvider;
import com.nomiceu.nomilabs.gregtech.multiblock.MetaTileEntityGreenhouse;
import com.nomiceu.nomilabs.gregtech.multiblock.MetaTileEntityMicroverse;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.common.metatileentities.MetaTileEntities;

import java.util.List;

public class LabsMultiblocks {
    public static MetaTileEntityGreenhouse GREENHOUSE;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse1 MICROVERSE_1;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse2 MICROVERSE_2;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse3 MICROVERSE_3;

    public static MetaTileEntityCreativeTankProvider CREATIVE_TANK_PROVIDER;

    public static void initOld() {
        MICROVERSE_1 = MetaTileEntities.registerMetaTileEntity(32000, new MetaTileEntityMicroverse.MetaTileEntityMicroverse1(LabsNames.makeLabsName("microverse_projector_basic")));
        MICROVERSE_2 = MetaTileEntities.registerMetaTileEntity(32001, new MetaTileEntityMicroverse.MetaTileEntityMicroverse2(LabsNames.makeLabsName("microverse_projector_advanced")));
        MICROVERSE_3 = MetaTileEntities.registerMetaTileEntity(32002, new MetaTileEntityMicroverse.MetaTileEntityMicroverse3(LabsNames.makeLabsName("microverse_projector_advanced_ii")));
        CREATIVE_TANK_PROVIDER = MetaTileEntities.registerMetaTileEntity(32003, new MetaTileEntityCreativeTankProvider(LabsNames.makeLabsName("creative_tank_provider")));

    }
    public static void initNew() {
        GREENHOUSE = MetaTileEntities.registerMetaTileEntity(32050, new MetaTileEntityGreenhouse(LabsNames.makeLabsName("greenhouse"))); // Get a set id later, use 32050 for now
    }
}
