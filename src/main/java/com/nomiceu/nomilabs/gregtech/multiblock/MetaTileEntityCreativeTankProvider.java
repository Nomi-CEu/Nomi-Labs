package com.nomiceu.nomilabs.gregtech.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;
import com.nomiceu.nomilabs.util.LabsModeHelper;

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
import gregtech.common.blocks.*;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityCreativeTankProvider extends RecipeMapMultiblockController
                                                implements ConditionalJEIMultiblock {

    public MetaTileEntityCreativeTankProvider(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.CREATIVE_TANK_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityCreativeTankProvider(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "XFX", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .where('S', selfPredicate())
                .where('X', states(getCasingStateMain()).setMinGlobalLimited(15).or(autoAbilities()))
                .where('F', states(getCasingStateFrame()))
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ATOMIC_CASING;
    }

    protected IBlockState getCasingStateMain() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ATOMIC_CASING);
    }

    protected IBlockState getCasingStateFrame() {
        return MetaBlocks.FRAMES.get(Materials.TungstenCarbide).getBlock(Materials.TungstenCarbide);
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
    public void addInformation(ItemStack stack, @Nullable World world, @NotNull List<String> tooltip,
                               boolean advanced) {
        tooltip.add(translate("tooltip.nomilabs.creative_tank_provider.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public boolean shouldShowInJEI() {
        return LabsModeHelper.isNormal();
    }
}
