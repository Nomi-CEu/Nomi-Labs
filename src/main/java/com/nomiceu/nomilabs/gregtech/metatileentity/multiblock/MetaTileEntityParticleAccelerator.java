package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;
import com.nomiceu.nomilabs.util.LabsModeHelper;

import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregicality.multiblocks.common.block.blocks.BlockUniqueCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityParticleAccelerator extends GCYMRecipeMapMultiblockController
                                               implements ConditionalJEIMultiblock {

    public MetaTileEntityParticleAccelerator(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.PARTICLE_ACCELERATOR_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityParticleAccelerator(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("#####", "XXXXX", "XFFFX", "XAAAX", "XFFFX", "XXXXX", "#####")
                .aisle("#XXX#", "XCCCX", "FCCCF", "AAAAA", "FCCCF", "XCCCX", "#XXX#")
                .aisle("#XXX#", "XC#CX", "FC#CF", "AA#AA", "FC#CF", "XC#CX", "#XXX#")
                .aisle("#XXX#", "XCCCX", "FCCCF", "AAAAA", "FCCCF", "XCCCX", "#XXX#")
                .aisle("#####", "XXZXX", "XFFFX", "XAAAX", "XFFFX", "XXXXX", "#####")
                .where('Z', selfPredicate())
                .where('X', getCasingPredicateMain().setMinGlobalLimited(30).or(autoAbilities()))
                .where('#', air())
                .where('C', getCasingPredicateMagnet())
                .where('S', getCasingPredicateSuperConduct())
                .where('F', getCasingPredicateFusion())
                .where('A', getCasingPredicateAcceleration())
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ATOMIC_CASING;
    }

    protected TraceabilityPredicate getCasingPredicateMain() {
        return states(
                GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ATOMIC_CASING));
    }

    protected TraceabilityPredicate getCasingPredicateMagnet() {
        return states(GCYMMetaBlocks.UNIQUE_CASING
                .getState(BlockUniqueCasing.UniqueCasingType.MOLYBDENUM_DISILICIDE_COIL));
    }

    protected TraceabilityPredicate getCasingPredicateSuperConduct() {
        return states(MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL));
    }

    protected TraceabilityPredicate getCasingPredicateAcceleration() {
        return states(LabsBlocks.ACCELERATION_COIL.getDefaultState());
    }

    protected TraceabilityPredicate getCasingPredicateFusion() {
        return states(MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_COIL));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.FUSION_REACTOR_OVERLAY;
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, @NotNull List<String> tooltip,
                               boolean advanced) {
        tooltip.add(translate("tooltip.nomilabs.cyclotron.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public boolean shouldShowInJEI() {
        return LabsModeHelper.isNormal();
    }
}
