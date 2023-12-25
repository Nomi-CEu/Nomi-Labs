package com.nomiceu.nomilabs.gregtech.multiblock.registry;

import com.nomiceu.nomilabs.gregtech.multiblock.*;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.common.metatileentities.MetaTileEntities;

/**
 * Multiblocks all start at 31000, as colliding metas with old multiblocks usually causes problems.
 */
@SuppressWarnings("unused")
public class LabsMultiblocks {
    public static MetaTileEntityGreenhouse GREENHOUSE;
    public static MetaTileEntityMicroverseProjector.Microverse1 MICROVERSE_1;
    public static MetaTileEntityMicroverseProjector.Microverse2 MICROVERSE_2;
    public static MetaTileEntityMicroverseProjector.Microverse3 MICROVERSE_3;

    public static MetaTileEntityCreativeTankProvider CREATIVE_TANK_PROVIDER;

    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_1;
    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_2;

    public static MetaTileEntityActualizationChamber ACTUALIZATION_CHAMBER;
    public static MetaTileEntityUniversalCrystalizer UNIVERSAL_CRYSTALIZER;
    public static MetaTileEntityDMESimChamber DME_SIM_CHAMBER;

    public static void initOld() {
        MICROVERSE_1 = MetaTileEntities.registerMetaTileEntity(32100, new MetaTileEntityMicroverseProjector.Microverse1(LabsNames.makeLabsName("microverse_projector_1")));
        MICROVERSE_2 = MetaTileEntities.registerMetaTileEntity(32101, new MetaTileEntityMicroverseProjector.Microverse2(LabsNames.makeLabsName("microverse_projector_2")));
        MICROVERSE_3 = MetaTileEntities.registerMetaTileEntity(32102, new MetaTileEntityMicroverseProjector.Microverse3(LabsNames.makeLabsName("microverse_projector_3")));

        CREATIVE_TANK_PROVIDER = MetaTileEntities.registerMetaTileEntity(32103, new MetaTileEntityCreativeTankProvider(LabsNames.makeLabsName("creative_tank_provider")));

        NAQ_REACTOR_1 = MetaTileEntities.registerMetaTileEntity(32104, new MetaTileEntityNaquadahReactor.NaquadahReactor1(LabsNames.makeLabsName("naquadah_reactor_1")));
        NAQ_REACTOR_2 = MetaTileEntities.registerMetaTileEntity(32105, new MetaTileEntityNaquadahReactor.NaquadahReactor2(LabsNames.makeLabsName("naquadah_reactor_2")));

        ACTUALIZATION_CHAMBER = MetaTileEntities.registerMetaTileEntity(32106, new MetaTileEntityActualizationChamber(LabsNames.makeLabsName("actualization_chamber")));

        UNIVERSAL_CRYSTALIZER = MetaTileEntities.registerMetaTileEntity(32107, new MetaTileEntityUniversalCrystalizer(LabsNames.makeLabsName("universal_crystallizer")));

        DME_SIM_CHAMBER = MetaTileEntities.registerMetaTileEntity(32108, new MetaTileEntityDMESimChamber(LabsNames.makeLabsName("dme_sim_chamber")));
    }
    public static void initNew() {
        GREENHOUSE = MetaTileEntities.registerMetaTileEntity(32109, new MetaTileEntityGreenhouse(LabsNames.makeLabsName("greenhouse")));
    }
}
