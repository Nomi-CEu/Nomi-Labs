package com.nomiceu.nomilabs.gregtech.multiblock.registry;

import com.blakebr0.extendedcrafting.block.BlockStorage;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.multiblock.*;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import io.sommers.packmode.PMConfig;

public class LabsMultiblocks {
    public static MetaTileEntityGreenhouse GREENHOUSE;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse1 MICROVERSE_1;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse2 MICROVERSE_2;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse3 MICROVERSE_3;

    public static MetaTileEntityCreativeTankProvider CREATIVE_TANK_PROVIDER;

    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_1;
    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_2;

    public static MetaTileEntityActualizationChamber ACTUALIZATION_CHAMBER;
    public static MetaTileEntityUniversalCrystalizer UNIVERSAL_CRYSTALIZER;
    public static MetaTileEntityDMESimChamber DME_SIM_CHAMBER;

    public static void initOld() {
        MICROVERSE_1 = MetaTileEntities.registerMetaTileEntity(32000, new MetaTileEntityMicroverse.MetaTileEntityMicroverse1(LabsNames.makeLabsName("microverse_projector_1")));
        MICROVERSE_2 = MetaTileEntities.registerMetaTileEntity(32001, new MetaTileEntityMicroverse.MetaTileEntityMicroverse2(LabsNames.makeLabsName("microverse_projector_2")));
        MICROVERSE_3 = MetaTileEntities.registerMetaTileEntity(32002, new MetaTileEntityMicroverse.MetaTileEntityMicroverse3(LabsNames.makeLabsName("microverse_projector_3")));
        CREATIVE_TANK_PROVIDER = MetaTileEntities.registerMetaTileEntity(32003, new MetaTileEntityCreativeTankProvider(LabsNames.makeLabsName("creative_tank_provider")));

        NAQ_REACTOR_1 = MetaTileEntities.registerMetaTileEntity(32004, new MetaTileEntityNaquadahReactor(LabsNames.makeLabsName("naquadah_reactor_1"), 1, GTValues.ZPM, 3,
                MetaBlocks.COMPRESSED.get(Materials.Duranium).getBlock(Materials.Duranium), ModBlocks.blockTrimmed.getStateFromMeta(BlockTrimmed.Type.ULTIMATE_TRIMMED.getMetadata())));

        Material material;
        if (PMConfig.getPackMode().equals(LabsValues.NORMAL_MODE))
            material = Materials.RutheniumTriniumAmericiumNeutronate;
        else
            material = LabsMaterials.Taranium;

        NAQ_REACTOR_2 = MetaTileEntities.registerMetaTileEntity(32005, new MetaTileEntityNaquadahReactor(LabsNames.makeLabsName("naquadah_reactor_2"), 2, GTValues.UV, 3,
                MetaBlocks.COMPRESSED.get(material).getBlock(material), ModBlocks.blockStorage.getStateFromMeta(BlockStorage.Type.ULTIMATE.getMetadata())));

        ACTUALIZATION_CHAMBER = MetaTileEntities.registerMetaTileEntity(32006, new MetaTileEntityActualizationChamber(LabsNames.makeLabsName("actualization_chamber")));

        UNIVERSAL_CRYSTALIZER = MetaTileEntities.registerMetaTileEntity(32007, new MetaTileEntityUniversalCrystalizer(LabsNames.makeLabsName("universal_crystallizer")));

        DME_SIM_CHAMBER = MetaTileEntities.registerMetaTileEntity(32008, new MetaTileEntityDMESimChamber(LabsNames.makeLabsName("dme_sim_chamber")));
    }
    public static void initNew() {
        GREENHOUSE = MetaTileEntities.registerMetaTileEntity(32009, new MetaTileEntityGreenhouse(LabsNames.makeLabsName("greenhouse")));
    }
}
