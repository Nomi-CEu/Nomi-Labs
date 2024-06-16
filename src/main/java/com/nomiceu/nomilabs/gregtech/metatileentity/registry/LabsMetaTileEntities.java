package com.nomiceu.nomilabs.gregtech.metatileentity.registry;

import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.metatileentity.part.MetaTileEntityGrowthBase;
import gregtech.client.renderer.texture.Textures;
import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.*;
import com.nomiceu.nomilabs.gregtech.metatileentity.part.MetaTileEntityGrowthModifier;
import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.GTValues;
import gregtech.common.metatileentities.MetaTileEntities;

/**
 * Meta Tile Entities all start at 31000, as colliding metas with old Meta Tile Entities usually causes problems.
 */
@SuppressWarnings("unused")
public class LabsMetaTileEntities {

    public static MetaTileEntityGrowthModifier GROWTH_LAMP_1;
    public static MetaTileEntityGrowthModifier GROWTH_VENT_1;
    public static MetaTileEntityGrowthBase GROWTH_BASE_1;
    public static MetaTileEntityGrowthChamber GROWTH_CHAMBER;
    public static MetaTileEntityEcosystemEmulator ECOSYSTEM_EMULATOR;

    public static MetaTileEntityMicroverseProjector.Microverse1 MICROVERSE_1;
    public static MetaTileEntityMicroverseProjector.Microverse2 MICROVERSE_2;
    public static MetaTileEntityMicroverseProjector.Microverse3 MICROVERSE_3;

    public static MetaTileEntityCreativeTankProvider CREATIVE_TANK_PROVIDER;

    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_1;
    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_2;

    public static MetaTileEntityActualizationChamber ACTUALIZATION_CHAMBER;
    public static MetaTileEntityUniversalCrystalizer UNIVERSAL_CRYSTALIZER;
    public static MetaTileEntityDMESimChamber DME_SIM_CHAMBER;

    public static void preInit() {
        if (LabsConfig.content.gtCustomContent.enableOldMultiblocks)
            initOld();
        if (LabsConfig.content.gtCustomContent.enableNewMultiblocks)
            initNew();
    }

    private static void initOld() {
        MICROVERSE_1 = MetaTileEntities.registerMetaTileEntity(32100,
                new MetaTileEntityMicroverseProjector.Microverse1(LabsNames.makeLabsName("microverse_projector_1")));
        MICROVERSE_2 = MetaTileEntities.registerMetaTileEntity(32101,
                new MetaTileEntityMicroverseProjector.Microverse2(LabsNames.makeLabsName("microverse_projector_2")));
        MICROVERSE_3 = MetaTileEntities.registerMetaTileEntity(32102,
                new MetaTileEntityMicroverseProjector.Microverse3(LabsNames.makeLabsName("microverse_projector_3")));

        CREATIVE_TANK_PROVIDER = MetaTileEntities.registerMetaTileEntity(32103,
                new MetaTileEntityCreativeTankProvider(LabsNames.makeLabsName("creative_tank_provider")));

        NAQ_REACTOR_1 = MetaTileEntities.registerMetaTileEntity(32104,
                new MetaTileEntityNaquadahReactor.NaquadahReactor1(LabsNames.makeLabsName("naquadah_reactor_1")));
        NAQ_REACTOR_2 = MetaTileEntities.registerMetaTileEntity(32105,
                new MetaTileEntityNaquadahReactor.NaquadahReactor2(LabsNames.makeLabsName("naquadah_reactor_2")));

        ACTUALIZATION_CHAMBER = MetaTileEntities.registerMetaTileEntity(32106,
                new MetaTileEntityActualizationChamber(LabsNames.makeLabsName("actualization_chamber")));

        UNIVERSAL_CRYSTALIZER = MetaTileEntities.registerMetaTileEntity(32107,
                new MetaTileEntityUniversalCrystalizer(LabsNames.makeLabsName("universal_crystallizer")));

        if (Loader.isModLoaded(LabsValues.DME_MODID))
            DME_SIM_CHAMBER = MetaTileEntities.registerMetaTileEntity(32108,
                    new MetaTileEntityDMESimChamber(LabsNames.makeLabsName("dme_sim_chamber")));
    }

    private static void initNew() {
        GROWTH_CHAMBER = MetaTileEntities.registerMetaTileEntity(32109,
                new MetaTileEntityGrowthChamber(LabsNames.makeLabsName("growth_chamber")));

        ECOSYSTEM_EMULATOR = MetaTileEntities.registerMetaTileEntity(32110,
                new MetaTileEntityEcosystemEmulator(LabsNames.makeLabsName("ecosystem_emulator")));

        GROWTH_LAMP_1 = MetaTileEntities.registerMetaTileEntity(32111,
                new MetaTileEntityGrowthModifier(LabsNames.makeLabsName("growth_lamp_1"), GTValues.MV, LabsTextures.OVERLAY_LAMP_1));

        GROWTH_VENT_1 = MetaTileEntities.registerMetaTileEntity(32112,
                new MetaTileEntityGrowthModifier(LabsNames.makeLabsName("growth_vent_1"), GTValues.MV, LabsTextures.OVERLAY_VENT_1));

        GROWTH_BASE_1 = MetaTileEntities.registerMetaTileEntity(32113, new MetaTileEntityGrowthBase(LabsNames.makeLabsName("growth_base_1"), GTValues.MV, Textures.STEAM_CASING_BRONZE));
    }
}
