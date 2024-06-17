package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.gregtech.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.gregtech.metatileentity.registry.LabsMetaTileEntities;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;

import gregicality.multiblocks.common.metatileentities.GCYMMetaTileEntities;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;

public class MetaTileEntityEcosystemEmulator extends MetaTileEntityBaseChamber {

    public MetaTileEntityEcosystemEmulator(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.ECOSYSTEM_EMULATOR_RECIPES);

        voltageTier = GTValues.LuV;
        canParallel = true;

        baseTiers = new ArrayList<>();
        baseTiers.add(Pair.of(LabsMetaTileEntities.GROWTH_BASE_1, (byte) 1));

        specialTiers = new ArrayList<>();
        specialTiers.add(Triple.of(LabsMetaTileEntities.GROWTH_LAMP_1,
                LabsMetaTileEntities.GROWTH_VENT_1, (byte) 1));
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityEcosystemEmulator(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCCCCCC", "BGGGGGB", "BGGGGGB", "#BGGGB#", "##VLV##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##").setRepeatable(3)
                .aisle("CDDPDDC", "B-----B", "B-----B", "#B---B#", "##VLV##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##").setRepeatable(3)
                .aisle("CCCSCCC", "BGGGGGB", "BGGGGGB", "#BGGGB#", "##VLV##")
                .where('S', selfPredicate())
                .where('C',
                        states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST))
                                .setMinGlobalLimited(20).or(autoAbilities()))
                .where('B',
                        states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST))) // Like
                                                                                                                         // C,
                                                                                                                         // but
                                                                                                                         // only
                                                                                                                         // accepts
                                                                                                                         // casing
                .where('G', getCasingPredicateGlass())
                .where('D', getCasingPredicateBase())
                .where('L', getCasingPredicateLamp())
                .where('V', getCasingPredicateVent())
                .where('P', getCasingPredicatePipe())
                .where('-', air())
                .where('#', any())
                .build();
    }

    @Override
    public MultiblockShapeInfo.Builder getBuilder() {
        return MultiblockShapeInfo.builder()
                .aisle("CCMCECC", "CGGGGGC", "CGGGGGC", "#CGGGC#", "##VLV##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CDDPDDC", "C-----C", "C-----C", "#C---C#", "##VLV##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CDDPDDC", "G-----G", "G-----G", "#G---G#", "##GGG##")
                .aisle("CFISOHC", "CGGGGGC", "CGGGGGC", "#CGGGC#", "##VLV##")
                .where('S', LabsMetaTileEntities.ECOSYSTEM_EMULATOR, EnumFacing.SOUTH)
                .where('#', Blocks.AIR.getDefaultState())
                .where('C', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST))
                .where('G', MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS))
                .where('M', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH :
                        MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST),
                        EnumFacing.NORTH)
                .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.LV], EnumFacing.NORTH)
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                .where('O', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.LV], EnumFacing.SOUTH)
                .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[GTValues.LV], EnumFacing.SOUTH)
                .where('H', GCYMMetaTileEntities.PARALLEL_HATCH[0], EnumFacing.SOUTH)
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE));
    }

    @Override
    public MultiblockShapeInfo.Builder getBaseShapeFromBuilder(MultiblockShapeInfo.Builder builder, MetaTileEntity state) {
        return builder.where('D', state, EnumFacing.UP);
    }

    @Override
    public MultiblockShapeInfo.Builder getSpecialShapeFromBuilder(MultiblockShapeInfo.Builder builder,
                                                                  MetaTileEntity light, MetaTileEntity vent) {
        return builder.where('L', light, EnumFacing.UP).where('V', vent, EnumFacing.UP);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.ROBUST_TUNGSTENSTEEL_CASING;
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        return states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS));
    }

    protected TraceabilityPredicate getCasingPredicatePipe() {
        return states(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, @NotNull List<String> tooltip,
                               boolean advanced) {
        tooltip.add(translate("tooltip.nomilabs.ecosystem_emulator.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }
}
