package com.nomiceu.nomilabs.gregtech.multiblock;

import com.brandon3055.draconicevolution.DEFeatures;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

public class MetaTileEntityUniversalCrystalizer extends RecipeMapMultiblockController {
    public MetaTileEntityUniversalCrystalizer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.UNIVERSAL_CRYSTALIZER_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityUniversalCrystalizer(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {

        return FactoryBlockPattern.start()
                .aisle("XXXXXXX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XXXXXXX")
                .aisle("XXXXXXX", "G#####G", "G#####G", "F#####F", "G#####G", "G#####G", "XGGGGGX")
                .aisle("XXXXXXX", "G#CCC#G", "F#C#C#F", "FTC#CTF", "F#C#C#F", "G#CCC#G", "XGGGGGX")
                .aisle("XXXXXXX", "F#CCC#F", "FT###TF", "FTBRBTF", "FT###TF", "F#CCC#F", "XGGGGGX")
                .aisle("XXXXXXX", "G#CCC#G", "F#C#C#F", "FTC#CTF", "F#C#C#F", "G#CCC#G", "XGGGGGX")
                .aisle("XXXXXXX", "G#####G", "G#####G", "F#####F", "G#####G", "G#####G", "XGGGGGX")
                .aisle("XXXSXXX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XGGGGGX", "XXXXXXX")
                .where('S', selfPredicate())
                .where('X', states(getCasingStateMain()).setMinGlobalLimited(80).or(autoAbilities()))
                .where('#', states(getCasingStateAir()))
                .where('F', states(getCasingStateFrame()))
                .where('G', states(getCasingStateGlass()))
                .where('T', states(getCasingStateTaranium()))
                .where('C', states(getCasingStateCoil()))
                .where('B', states(getCasingStateComponent()))
                .where('R', states(getCasingStateCore()))
                .build();

    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ENGRAVER_CASING;
    }

    protected IBlockState getCasingStateAir() {
        assert Blocks.AIR != null;
        return Blocks.AIR.getDefaultState();
    }

    protected IBlockState getCasingStateMain() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ENGRAVER_CASING);
    }

    protected IBlockState getCasingStateFrame() {
        return MetaBlocks.FRAMES.get(Materials.Berkelium).getBlock(Materials.Berkelium);
    }

    protected IBlockState getCasingStateGlass() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS);
    }

    protected IBlockState getCasingStateTaranium() {
        return MetaBlocks.COMPRESSED.get(LabsMaterials.Taranium).getBlock(LabsMaterials.Taranium);
    }

    protected IBlockState getCasingStateCoil() {
        return MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_COIL);
    }

    protected IBlockState getCasingStateComponent() {
        assert Blocks.AIR != null;

        return Loader.isModLoaded(LabsValues.DRACONIC_MODID)
                ? DEFeatures.reactorComponent.getDefaultState()
                : Blocks.AIR.getDefaultState();
    }

    protected IBlockState getCasingStateCore() {
        assert Blocks.AIR != null;

        return Loader.isModLoaded(LabsValues.DRACONIC_MODID)
                ? DEFeatures.reactorCore.getDefaultState()
                : Blocks.AIR.getDefaultState();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.CREATIVE_CONTAINER_OVERLAY;
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
        tooltip.add(translate("tooltip.nomilabs.universal_crystallizer.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }
}
