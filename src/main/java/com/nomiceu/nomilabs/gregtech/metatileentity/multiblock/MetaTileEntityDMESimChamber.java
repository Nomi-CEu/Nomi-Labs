package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.blakebr0.extendedcrafting.block.BlockStorage;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.recipe.recipelogic.DMERecipeLogic;

import appeng.core.Api;
import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityDMESimChamber extends GCYMRecipeMapMultiblockController implements ConditionalJEIMultiblock {

    public MetaTileEntityDMESimChamber(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.DME_SIM_CHAMBER_RECIPES);
        this.recipeMapWorkable = new DMERecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityDMESimChamber(metaTileEntityId);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "VEEEV", "VEEEV", "VEEEV", "XXXXX")
                .aisle("XXXXX", "GOOOG", "VOOOV", "GOOOG", "XGGGX")
                .aisle("XXXXX", "GOOOG", "VO#OV", "GOOOG", "XGGGX")
                .aisle("XXXXX", "GOOOG", "VOOOV", "GOOOG", "XGGGX")
                .aisle("XXSXX", "VEGEV", "VGGGV", "VEGEV", "XXXXX")
                .where('S', selfPredicate())
                .where('X', getCasingPredicateMain().setMinGlobalLimited(30).or(autoAbilities()))
                .where('#', air())
                .where('V', getCasingPredicateVibration())
                .where('E', getCasingPredicateEnderium())
                .where('G', getCasingPredicateGlass())
                .where('O', getCasingPredicateOmnium())
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

    protected TraceabilityPredicate getCasingPredicateVibration() {
        return states(GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING
                .getState(BlockLargeMultiblockCasing.CasingType.VIBRATION_SAFE_CASING));
    }

    protected TraceabilityPredicate getCasingPredicateEnderium() {
        return states(MetaBlocks.COMPRESSED.get(LabsMaterials.Enderium).getBlock(LabsMaterials.Enderium));
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        if (Loader.isModLoaded(LabsValues.AE2_MODID) &&
                Api.INSTANCE.definitions().blocks().quartzVibrantGlass().maybeBlock().isPresent())
            return states(
                    Api.INSTANCE.definitions().blocks().quartzVibrantGlass().maybeBlock().get().getDefaultState());

        return air();
    }

    protected TraceabilityPredicate getCasingPredicateOmnium() {
        if (Loader.isModLoaded(LabsValues.EXTENDED_CRAFTING_MODID))
            return states(ModBlocks.blockStorage.getStateFromMeta(BlockStorage.Type.ULTIMATE.getMetadata()));

        return air();
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
        tooltip.add(translate("tooltip.nomilabs.dme_sim_chamber.description"));
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public boolean shouldShowInJEI() {
        return false;
    }
}
