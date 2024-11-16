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

import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityActualizationChamber extends RecipeMapMultiblockController
                                                implements ConditionalJEIMultiblock {

    public MetaTileEntityActualizationChamber(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.ACTUALIZATION_CHAMBER_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityActualizationChamber(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "GGG", "XXX")
                .aisle("XXX", "GOG", "XXX")
                .aisle("XSX", "GGG", "XXX")
                .where('S', selfPredicate())
                .where('X', getCasingPredicateMain().setMinGlobalLimited(9).or(autoAbilities()))
                .where('G', getCasingPredicateGlass())
                .where('O', getCasingPredicateCoil())
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return recipeMapWorkable.isActive() ? Textures.ACTIVE_FUSION_TEXTURE : Textures.FUSION_TEXTURE;
    }

    protected TraceabilityPredicate getCasingPredicateMain() {
        return states(MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_CASING_MK3));
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        return states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS));
    }

    protected TraceabilityPredicate getCasingPredicateCoil() {
        return states(MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_COIL));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.ENDER_FLUID_LINK;
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
        tooltip.add(translate("tooltip.nomilabs.actualization_chamber.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public boolean shouldShowInJEI() {
        return true;
    }
}
