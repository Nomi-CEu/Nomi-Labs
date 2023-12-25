package com.nomiceu.nomilabs.gregtech.multiblock;

import appeng.core.Api;
import com.blakebr0.extendedcrafting.block.BlockStorage;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.nomiceu.nomilabs.gregtech.LabsRecipeMaps;
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
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaTileEntityDMESimChamber extends RecipeMapMultiblockController {
    public MetaTileEntityDMESimChamber(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, LabsRecipeMaps.DME_SIM_CHAMBER_RECIPES);
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
                .where('X', states(getCasingStateMain()).setMinGlobalLimited(30).or(autoAbilities()))
                .where('#', states(getCasingStateAir()))
                .where('V', states(getCasingStateVibration()))
                .where('E', states(getCasingStateEnderium()))
                .where('G', states(getCasingStateGlass()))
                .where('O', states(getCasingStateOmnium()))
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ATOMIC_CASING;
    }

    protected IBlockState getCasingStateAir() {
        assert Blocks.AIR != null;
        return Blocks.AIR.getDefaultState();
    }

    protected IBlockState getCasingStateMain() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ATOMIC_CASING);
    }

    protected IBlockState getCasingStateVibration() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.VIBRATION_SAFE_CASING);
    }

    protected IBlockState getCasingStateEnderium() {
        return MetaBlocks.COMPRESSED.get(LabsMaterials.Enderium).getBlock(LabsMaterials.Enderium);
    }

    protected IBlockState getCasingStateGlass() {
        if (Api.INSTANCE.definitions().blocks().quartzVibrantGlass().maybeBlock().isPresent()) {
            return Api.INSTANCE.definitions().blocks().quartzVibrantGlass().maybeBlock().get().getDefaultState();
        } else {
            assert Blocks.AIR != null;
            return Blocks.AIR.getDefaultState();
        }
    }

    protected IBlockState getCasingStateOmnium() {
        return ModBlocks.blockStorage.getStateFromMeta(BlockStorage.Type.ULTIMATE.getMetadata());
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
    public void addInformation(ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, boolean advanced) {
        tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.nomilabs.dme_sim_chamber.description") + TextFormatting.RESET);
        super.addInformation(stack, world, tooltip, advanced);
    }
}
