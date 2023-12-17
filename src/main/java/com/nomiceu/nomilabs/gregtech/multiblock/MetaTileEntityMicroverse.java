package com.nomiceu.nomilabs.gregtech.multiblock;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.multiblock.registry.LabsMultiblocks;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import team.chisel.Features;
import team.chisel.common.carving.Carving;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MetaTileEntityMicroverse extends RecipeMapMultiblockController {
    protected final int tier;

    public MetaTileEntityMicroverse(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, LabsRecipeMaps.MICROVERSE_RECIPES.get(tier - 1));
        this.tier = tier;
    }

    protected IBlockState getCasingStateMain() {
        return LabsBlocks.MICROVERSE_CASING.getDefaultState();
    }

    protected IBlockState getCasingStateGlass() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS);
    }

    protected IBlockState getCasingStateDiamond() {
        return Objects.requireNonNull(Carving.chisel.getGroup(Blocks.DIAMOND_BLOCK.getDefaultState())).getVariations()
                .get(4).getBlockState(); // This cursed line returns the Space Diamond Chisel Block
    }

    protected IBlockState getCasingStateGrate() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING);
    }

    protected IBlockState getCasingStateEngine() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ENGINE_INTAKE_CASING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return LabsTextures.MICROVERSE_CASING;
    }

    @Override
    public boolean hasMufflerMechanics() {
        return true;
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.COMPRESSOR_OVERLAY;
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    public static class MetaTileEntityMicroverse1 extends MetaTileEntityMicroverse {
        public MetaTileEntityMicroverse1(ResourceLocation metaTileEntityId) {
            super(metaTileEntityId,1);
        }

        @Override
        @NotNull
        protected BlockPattern createStructurePattern() {
            return FactoryBlockPattern.start()
                    .aisle("XXX", "XVX", "XXX")
                    .aisle("XXX", "GDG", "XXX")
                    .aisle("XSX", "XGX", "XXX")
                    .where('S', selfPredicate())
                    .where('G', states(getCasingStateGlass()))
                    .where('V', states(getCasingStateGrate()))
                    .where('D', states(getCasingStateDiamond()))
                    .where('X', states(getCasingStateMain()).setMinGlobalLimited(12).or(autoAbilities()))
                    .build();
        }

        // Overrides the Preview
        @Override
        public List<MultiblockShapeInfo> getMatchingShapes() {
            ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            shapeInfo.add(MultiblockShapeInfo.builder()
                    .aisle("EEM", "XVX", "XXX")
                    .aisle("FXX", "GDG", "XHX")
                    .aisle("ISO", "XGX", "XXX")
                    .where('X', getCasingStateMain())
                    .where('S', LabsMultiblocks.MICROVERSE_1, EnumFacing.SOUTH)
                    .where('G', getCasingStateGlass())
                    .where('V', getCasingStateGrate())
                    .where('D', getCasingStateDiamond())
                    .where('H', MetaTileEntities.MUFFLER_HATCH[GTValues.ULV], EnumFacing.UP) // ULV = LV, Muffler Hatches start at LV
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[GTValues.LV], EnumFacing.WEST)
                    .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.MV], EnumFacing.NORTH)
                    .where('M', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                            getCasingStateMain(), EnumFacing.NORTH)
                    .build());
            return shapeInfo;
        }

        @Override
        public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new MetaTileEntityMicroverse1(metaTileEntityId);
        }
    }

    public static class MetaTileEntityMicroverse2 extends MetaTileEntityMicroverse {
        public MetaTileEntityMicroverse2(ResourceLocation metaTileEntityId) {
            super(metaTileEntityId,2);
        }

        @Override
        @NotNull
        protected BlockPattern createStructurePattern() {
            return FactoryBlockPattern.start()
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                    .aisle("XVXVX", "GDDDG", "GDDDG", "GDDDG", "XVXVX")
                    .aisle("XXXXX", "GDDDG", "GD#DG", "GDDDG", "XXXXX")
                    .aisle("XVXVX", "GDDDG", "GDDDG", "GDDDG", "XVXVX")
                    .aisle("XXSXX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                    .where('S', selfPredicate())
                    .where('G', states(getCasingStateGlass()))
                    .where('V', states(getCasingStateGrate()))
                    .where('D', states(getCasingStateDiamond()))
                    .where('X', states(getCasingStateMain()).setMinGlobalLimited(45).or(autoAbilities()))
                    .where('#', any())
                    .build();
        }

        // Overrides the Preview
        @Override
        public List<MultiblockShapeInfo> getMatchingShapes() {
            ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            shapeInfo.add(MultiblockShapeInfo.builder()
                    .aisle("XEEMX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                    .aisle("XVXVX", "GDDDG", "GDDDG", "GDDDG", "XVXVX")
                    .aisle("XXXXX", "GDDDG", "GD#DG", "GDDDG", "XXHXX")
                    .aisle("XVXVX", "GDDDG", "GDDDG", "GDDDG", "XVXVX")
                    .aisle("XISOX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                    .where('X', getCasingStateMain())
                    .where('S', LabsMultiblocks.MICROVERSE_2, EnumFacing.SOUTH)
                    .where('G', getCasingStateGlass())
                    .where('V', getCasingStateGrate())
                    .where('D', getCasingStateDiamond())
                    .where('H', MetaTileEntities.MUFFLER_HATCH[GTValues.ULV], EnumFacing.UP) // ULV = LV, Muffler Hatches start at LV
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.EV], EnumFacing.NORTH)
                    .where('M', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                            getCasingStateMain(), EnumFacing.NORTH)
                    .where('#', Blocks.AIR.getDefaultState())
                    .build());
            return shapeInfo;
        }

        @Override
        public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new MetaTileEntityMicroverse2(metaTileEntityId);
        }
    }

    public static class MetaTileEntityMicroverse3 extends MetaTileEntityMicroverse {
        public MetaTileEntityMicroverse3(ResourceLocation metaTileEntityId) {
            super(metaTileEntityId,3);
        }

        @Override
        @NotNull
        protected BlockPattern createStructurePattern() {
            return FactoryBlockPattern.start()
                    .aisle("#########", "#########", "##XXXXX##", "##XVXVX##", "##XXXXX##", "##XVXVX##", "##XXXXX##", "#########", "#########")
                    .aisle("#########", "##XGGGX##", "#XDDDDDX#", "#GDDDDDG#", "#GDDDDDG#", "#GDDDDDG#", "#XDDDDDX#", "##XGGGX##", "#########")
                    .aisle("##XXXXX##", "#XDDDDDX#", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "#XDDDDDX#", "##XXXXX##")
                    .aisle("##XGGGX##", "#GDDDDDG#", "XDDDDDDDX", "GDD###DDG", "GDD###DDG", "GDD###DDG", "XDDDDDDDX", "#GDDDDDG#", "##XGGGX##").setRepeatable(3)
                    .aisle("##XXXXX##", "#XDDDDDX#", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "#XDDDDDX#", "##XXXXX##")
                    .aisle("#########", "##XGGGX##", "#XDDDDDX#", "#GDDDDDG#", "#GDDDDDG#", "#GDDDDDG#", "#XDDDDDX#", "##XGGGX##", "#########")
                    .aisle("#########", "#########", "##XXSXX##", "##XGGGX##", "##XGGGX##", "##XGGGX##", "##XXXXX##", "#########", "#########")
                    .where('S', selfPredicate())
                    .where('G', states(getCasingStateGlass()))
                    .where('V', states(getCasingStateEngine()))
                    .where('D', states(getCasingStateDiamond()))
                    .where('X', states(getCasingStateMain()).setMinGlobalLimited(115).or(autoAbilities()))
                    .where('#', any())
                    .build();
        }

        // Overrides the Preview
        @Override
        public List<MultiblockShapeInfo> getMatchingShapes() {
            ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            shapeInfo.add(MultiblockShapeInfo.builder()
                    .aisle("#########", "#########", "##XEEMX##", "##XVXVX##", "##XXHXX##", "##XVXVX##", "##XXXXX##", "#########", "#########")
                    .aisle("#########", "##XGGGX##", "#XDDDDDX#", "#GDDDDDG#", "#GDDDDDG#", "#GDDDDDG#", "#XDDDDDX#", "##XGGGX##", "#########")
                    .aisle("##XXXXX##", "#XDDDDDX#", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "#XDDDDDX#", "##XXXXX##")
                    .aisle("##XGGGX##", "#GDDDDDG#", "XDDDDDDDX", "GDD###DDG", "GDD###DDG", "GDD###DDG", "XDDDDDDDX", "#GDDDDDG#", "##XGGGX##")
                    .aisle("##XGGGX##", "#GDDDDDG#", "XDDDDDDDX", "GDD###DDG", "GDD###DDG", "GDD###DDG", "XDDDDDDDX", "#GDDDDDG#", "##XGGGX##")
                    .aisle("##XGGGX##", "#GDDDDDG#", "XDDDDDDDX", "GDD###DDG", "GDD###DDG", "GDD###DDG", "XDDDDDDDX", "#GDDDDDG#", "##XGGGX##")
                    .aisle("##XXXXX##", "#XDDDDDX#", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "XDDDDDDDX", "#XDDDDDX#", "##XXXXX##")
                    .aisle("#########", "##XGGGX##", "#XDDDDDX#", "#GDDDDDG#", "#GDDDDDG#", "#GDDDDDG#", "#XDDDDDX#", "##XGGGX##", "#########")
                    .aisle("#########", "#########", "##XISOX##", "##XGGGX##", "##XGGGX##", "##XGGGX##", "##XXXXX##", "#########", "#########")
                    .where('X', getCasingStateMain())
                    .where('S', LabsMultiblocks.MICROVERSE_3, EnumFacing.SOUTH)
                    .where('G', getCasingStateGlass())
                    .where('V', getCasingStateEngine())
                    .where('D', getCasingStateDiamond())
                    .where('H', MetaTileEntities.MUFFLER_HATCH[GTValues.ULV], EnumFacing.NORTH) // ULV = LV, Muffler Hatches start at LV
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                    .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.IV], EnumFacing.NORTH)
                    .where('M', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                            getCasingStateMain(), EnumFacing.NORTH)
                    .where('#', Blocks.AIR.getDefaultState())
                    .build());
            return shapeInfo;
        }

        @Override
        public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new MetaTileEntityMicroverse3(metaTileEntityId);
        }
    }
}
