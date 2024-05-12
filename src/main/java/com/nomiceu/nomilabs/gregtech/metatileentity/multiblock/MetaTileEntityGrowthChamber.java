package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.gregtech.LabsTextures;
import com.nomiceu.nomilabs.gregtech.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;

import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityGrowthChamber extends GCYMRecipeMapMultiblockController {

    public MetaTileEntityGrowthChamber(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.GROWTH_CHAMBER_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityGrowthChamber(metaTileEntityId);
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
                .where('C', getCasingPredicateMain().setMinGlobalLimited(20).or(autoAbilities()))
                .where('B', getCasingPredicateMain()) // Like C, but only accepts solid steel
                .where('G', getCasingPredicateGlass())
                .where('D', getCasingPredicateDirt())
                .where('L', getCasingPredicateLamp())
                .where('V', getCasingPredicateVent())
                .where('P', getCasingPredicatePipe())
                .where('-', air())
                .where('#', any())
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.SOLID_STEEL_CASING;
    }

    protected TraceabilityPredicate getCasingPredicateMain() {
        return states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID));
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        return states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS));
    }

    protected TraceabilityPredicate getCasingPredicateDirt() {
        return states(Blocks.DIRT.getDefaultState(), Blocks.GRASS.getDefaultState(), Blocks.FARMLAND.getDefaultState()); // Allow
                                                                                                                         // dirt
                                                                                                                         // or
                                                                                                                         // grass
                                                                                                                         // or
                                                                                                                         // farmland
    }

    protected TraceabilityPredicate getCasingPredicateLamp() {
        return LabsMetaBlocks.UNIQUE_CASING == null ? air() :
                states(LabsMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.GROWTH_LIGHT));
    }

    protected TraceabilityPredicate getCasingPredicateVent() {
        return LabsMetaBlocks.UNIQUE_CASING == null ? air() :
                states(LabsMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.AIR_VENT));
    }

    protected TraceabilityPredicate getCasingPredicatePipe() {
        return states(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return LabsTextures.GREENHOUSE_OVERLAY;
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
        tooltip.add(translate("tooltip.nomilabs.growth_chamber.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }
}
