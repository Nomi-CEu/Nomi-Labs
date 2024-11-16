package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

import java.util.ArrayList;
import java.util.Collections;
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

import com.brandon3055.draconicevolution.DEFeatures;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;

import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;

public class MetaTileEntityUniversalCrystalizer extends GCYMRecipeMapMultiblockController
                                                implements ConditionalJEIMultiblock {

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
                .where('X', getCasingPredicateMain()
                        .setMinGlobalLimited(80)
                        .or(autoAbilities()))
                .where('#', air())
                .where('F', getCasingPredicateFrame())
                .where('G', getCasingPredicateGlass())
                .where('T', getCasingPredicateTaranium())
                .where('C', getCasingPredicateCoil())
                .where('B', getCasingPredicateComponent())
                .where('R', getCasingPredicateCore())
                .build();
    }

    /**
     * Allow usage of Laser Hatches.
     */
    @Override
    public TraceabilityPredicate autoAbilities() {
        // Don't get the energy (we do that ourselves) or fluid export (not needed for UC) from super
        return super.autoAbilities(false, true, true, true, true, false, true)
                .or(abilities(MultiblockAbility.INPUT_ENERGY, MultiblockAbility.SUBSTATION_INPUT_ENERGY,
                        MultiblockAbility.INPUT_LASER)
                                .setMinGlobalLimited(1)
                                .setMaxGlobalLimited(2)
                                .setPreviewCount(1));
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        List<IEnergyContainer> list = new ArrayList<>();
        list.addAll(getAbilities(MultiblockAbility.INPUT_ENERGY));
        list.addAll(getAbilities(MultiblockAbility.INPUT_LASER));
        list.addAll(getAbilities(MultiblockAbility.SUBSTATION_INPUT_ENERGY));
        this.energyContainer = new EnergyContainerList(Collections.unmodifiableList(list));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ENGRAVER_CASING;
    }

    protected TraceabilityPredicate getCasingPredicateMain() {
        return states(
                GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ENGRAVER_CASING));
    }

    protected TraceabilityPredicate getCasingPredicateFrame() {
        return states(MetaBlocks.FRAMES.get(Materials.Berkelium).getBlock(Materials.Berkelium));
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        return states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS));
    }

    protected TraceabilityPredicate getCasingPredicateTaranium() {
        return states(MetaBlocks.COMPRESSED.get(LabsMaterials.Taranium).getBlock(LabsMaterials.Taranium));
    }

    protected TraceabilityPredicate getCasingPredicateCoil() {
        return states(MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_COIL));
    }

    protected TraceabilityPredicate getCasingPredicateComponent() {
        return Loader.isModLoaded(LabsValues.DRACONIC_MODID) ? states(DEFeatures.reactorComponent.getDefaultState()) :
                air();
    }

    protected TraceabilityPredicate getCasingPredicateCore() {
        return Loader.isModLoaded(LabsValues.DRACONIC_MODID) ? states(DEFeatures.reactorCore.getDefaultState()) :
                air();
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
        tooltip.add(translate("tooltip.nomilabs.universal_crystallizer.description"));
        tooltip.add(translate("tooltip.nomilabs.universal_crystallizer.description_laser"));
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public boolean shouldShowInJEI() {
        return true;
    }
}
