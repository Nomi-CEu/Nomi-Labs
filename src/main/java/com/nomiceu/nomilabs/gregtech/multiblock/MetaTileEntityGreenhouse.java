package com.nomiceu.nomilabs.gregtech.multiblock;

import com.nomiceu.nomilabs.gregtech.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.LabsTextures;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaTileEntityGreenhouse extends GCYMRecipeMapMultiblockController {

    public MetaTileEntityGreenhouse(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.GREENHOUSE_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityGreenhouse(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCCCCCC", "BGGGGGB", "BGGGGGB", "#BGGGB#", "##VLV##")
                .aisle("CDDPDDC", "GAAAAAG", "GAAAAAG", "#GAAAG#", "##GGG##").setRepeatable(3)
                .aisle("CDDPDDC", "BAAAAAB", "BAAAAAB", "#BAAAB#", "##VLV##")
                .aisle("CDDPDDC", "GAAAAAG", "GAAAAAG", "#GAAAG#", "##GGG##").setRepeatable(3)
                .aisle("CCCSCCC", "BGGGGGB", "BGGGGGB", "#BGGGB#", "##VLV##")
                .where('S', selfPredicate())
                .where('C', states(getCasingStateMain()).setMinGlobalLimited(20).or(autoAbilities()))
                .where('B', states(getCasingStateMain())) // Like C, but only accepts solid steel
                .where('G', states(getCasingStateGlass()))
                .where('D', states(getCasingStateDirt()))
                .where('L', states(getCasingStateLamp()))
                .where('V', states(getCasingStateVent()))
                .where('P', states(getCasingStatePipe()))
                .where('A', air())
                .where('#', any())
                .build();

    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.SOLID_STEEL_CASING;
    }

    protected IBlockState getCasingStateMain() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID);
    }

    protected IBlockState getCasingStateGlass() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS);
    }

    protected IBlockState[] getCasingStateDirt() {
        assert Blocks.DIRT != null;
        assert Blocks.GRASS != null;
        assert Blocks.FARMLAND != null;
        return new IBlockState[] {Blocks.DIRT.getDefaultState(), Blocks.GRASS.getDefaultState(), Blocks.FARMLAND.getDefaultState()}; // Allow dirt or grass
    }

    protected IBlockState getCasingStateLamp() {
        assert Blocks.AIR != null;
        return LabsMetaBlocks.UNIQUE_CASING == null ? Blocks.AIR.getDefaultState() : LabsMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.GROWTH_LIGHT);
    }

    protected IBlockState getCasingStateVent() {
        assert Blocks.AIR != null;
        return LabsMetaBlocks.UNIQUE_CASING == null ? Blocks.AIR.getDefaultState() : LabsMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.AIR_VENT);
    }

    protected IBlockState getCasingStatePipe() {
        return MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE);
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
    public void addInformation(ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, boolean advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nomilabs.greenhouse.description") + TextFormatting.RESET);
    }
}
