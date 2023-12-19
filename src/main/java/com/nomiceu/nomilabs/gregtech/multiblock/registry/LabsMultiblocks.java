package com.nomiceu.nomilabs.gregtech.multiblock.registry;

import com.blakebr0.extendedcrafting.block.BlockStorage;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.multiblock.*;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockCompressed;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import io.sommers.packmode.PMConfig;
import io.sommers.packmode.PackMode;
import io.sommers.packmode.compat.crafttweaker.PackModeInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class LabsMultiblocks {
    public static MetaTileEntityGreenhouse GREENHOUSE;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse1 MICROVERSE_1;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse2 MICROVERSE_2;
    public static MetaTileEntityMicroverse.MetaTileEntityMicroverse3 MICROVERSE_3;

    public static MetaTileEntityCreativeTankProvider CREATIVE_TANK_PROVIDER;

    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_1;
    public static MetaTileEntityNaquadahReactor NAQ_REACTOR_2;

    public static MetaTileEntityActualizationChamber ACTUALIZATION_CHAMBER;

    public static void initOld() {
        MICROVERSE_1 = MetaTileEntities.registerMetaTileEntity(32000, new MetaTileEntityMicroverse.MetaTileEntityMicroverse1(LabsNames.makeLabsName("microverse_projector_basic")));
        MICROVERSE_2 = MetaTileEntities.registerMetaTileEntity(32001, new MetaTileEntityMicroverse.MetaTileEntityMicroverse2(LabsNames.makeLabsName("microverse_projector_advanced")));
        MICROVERSE_3 = MetaTileEntities.registerMetaTileEntity(32002, new MetaTileEntityMicroverse.MetaTileEntityMicroverse3(LabsNames.makeLabsName("microverse_projector_advanced_ii")));
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
    }
    public static void initNew() {
        GREENHOUSE = MetaTileEntities.registerMetaTileEntity(32050, new MetaTileEntityGreenhouse(LabsNames.makeLabsName("greenhouse"))); // Get a set id later, use 32050 for now
    }
}
