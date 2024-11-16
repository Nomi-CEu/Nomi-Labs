package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import com.blakebr0.extendedcrafting.block.BlockStorage;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import com.nomiceu.nomilabs.gregtech.recipe.LabsRecipeMaps;
import com.nomiceu.nomilabs.gregtech.recipe.recipelogic.NaqRecipeLogic;

import appeng.core.Api;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.FuelMultiblockController;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;

public abstract class MetaTileEntityNaquadahReactor extends FuelMultiblockController {

    public final int numSpatial;
    public final int voltageTier;

    public static final int AMP = 3;

    public MetaTileEntityNaquadahReactor(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, int voltageTier,
                                         int numSpatial) {
        super(metaTileEntityId, recipeMap, voltageTier);
        this.voltageTier = voltageTier;
        this.numSpatial = numSpatial;
        this.recipeMapWorkable = new NaqRecipeLogic(this);
    }

    @Override
    @NotNull
    protected BlockPattern createStructurePattern() {
        String[] aisle1 = new String[this.numSpatial + 2];
        String[] aisle2 = new String[this.numSpatial + 2];
        String[] aisle3 = new String[this.numSpatial + 2];

        aisle1[0] = "CCC";
        aisle2[0] = "CCC";
        aisle3[0] = "CSC";

        // Loop for numSpatial times, starting from index 1, adding spatials to outside aisles
        for (int i = 1; i <= numSpatial; i++) {
            aisle1[i] = "PGP";
            aisle3[i] = "PGP";
        }
        // Loop for numSpatial - 1 times, starting from index 1, adding bottomFillers to inside aisle
        for (int i = 1; i < numSpatial; i++) {
            aisle2[i] = "GBG";
        }
        // Add top filler
        aisle2[numSpatial] = "GTG";

        aisle1[numSpatial + 1] = "CCC";
        aisle2[numSpatial + 1] = "CCC";
        aisle3[numSpatial + 1] = "CCC";

        return FactoryBlockPattern.start()
                .aisle(aisle1)
                .aisle(aisle2)
                .aisle(aisle3)
                .where('S', selfPredicate())
                .where('G', getCasingPredicateGlass())
                .where('P', getCasingPredicateSpatial())
                .where('T', getCasingPredicateTop())
                .where('B', getCasingPredicateBottom())
                .where('C', getCasingPredicateMain().setMinGlobalLimited(10)
                        .or(abilities(MultiblockAbility.OUTPUT_ENERGY).setExactLimit(1))
                        .or(autoAbilities(false, true, true, true, false, false, false)))
                .build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GCYMTextures.MIXER_CASING;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @NotNull
    protected ICubeRenderer getFrontOverlay() {
        return Textures.FUSION_REACTOR_OVERLAY;
    }

    protected TraceabilityPredicate getCasingPredicateMain() {
        return states(
                GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.MIXER_CASING));
    }

    protected TraceabilityPredicate getCasingPredicateGlass() {
        return states(MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS));
    }

    protected abstract TraceabilityPredicate getCasingPredicateBottom();

    protected abstract TraceabilityPredicate getCasingPredicateTop();

    protected TraceabilityPredicate getCasingPredicateSpatial() {
        return Loader.isModLoaded(LabsValues.AE2_MODID) &&
                Api.INSTANCE.definitions().blocks().spatialPylon().maybeBlock().isPresent() ?
                        states(Api.INSTANCE.definitions().blocks().spatialPylon().maybeBlock().get()
                                .getDefaultState()) :
                        air();
    }

    public void addSharedInfo(@NotNull List<String> tooltip) {
        tooltip.add(translate("tooltip.nomilabs.naquadah_reactor.produces", AMP,
                GTValues.VNF[voltageTier] + TextFormatting.RESET));
        tooltip.add(translateFormat("tooltip.nomilabs.naquadah_reactor.overclock", TooltipHelper.RAINBOW_SLOW));
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addDisplayText(List<ITextComponent> textList) {
        if (!isStructureFormed()) {
            ITextComponent tooltip = new TextComponentTranslation("gregtech.multiblock.invalid_structure.tooltip");
            tooltip.setStyle(new Style().setColor(TextFormatting.GRAY));
            textList.add(new TextComponentTranslation("gregtech.multiblock.invalid_structure")
                    .setStyle(new Style().setColor(TextFormatting.RED)
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))));
        } else {
            long produces = GTValues.V[voltageTier] * AMP;
            String voltageName = GTValues.VNF[GTUtility.getFloorTierByVoltage(produces)] + TextFormatting.RESET;
            textList.add(new TextComponentTranslation("gregtech.multiblock.max_energy_per_tick",
                    TextFormattingUtil.formatNumbers(produces), voltageName));

            if (!recipeMapWorkable.isWorkingEnabled()) {
                textList.add(new TextComponentTranslation("gregtech.multiblock.work_paused"));
            } else if (recipeMapWorkable.isActive()) {
                textList.add(new TextComponentTranslation("gregtech.multiblock.running"));
                int currentProgress = (int) (recipeMapWorkable.getProgressPercent() * 100);
                textList.add(new TextComponentTranslation("gregtech.multiblock.progress", currentProgress));
            } else {
                textList.add(new TextComponentTranslation("gregtech.multiblock.idling"));
            }
        }
    }

    public static class NaquadahReactor1 extends MetaTileEntityNaquadahReactor {

        public NaquadahReactor1(ResourceLocation metaTileEntityId) {
            super(metaTileEntityId, LabsRecipeMaps.NAQUADAH_REACTOR_RECIPES.get(0), GTValues.ZPM, 3);
        }

        @Override
        public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new MetaTileEntityNaquadahReactor.NaquadahReactor1(metaTileEntityId);
        }

        @Override
        protected TraceabilityPredicate getCasingPredicateBottom() {
            return states(MetaBlocks.COMPRESSED.get(Materials.Duranium).getBlock(Materials.Duranium));
        }

        @Override
        protected TraceabilityPredicate getCasingPredicateTop() {
            return Loader.isModLoaded(LabsValues.EXTENDED_CRAFTING_MODID) ?
                    states(ModBlocks.blockTrimmed.getStateFromMeta(BlockTrimmed.Type.ULTIMATE_TRIMMED.getMetadata())) :
                    air();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(ItemStack stack, World player, @NotNull List<String> tooltip, boolean advanced) {
            tooltip.add(translate("tooltip.nomilabs.naquadah_reactor_1.description"));
            addSharedInfo(tooltip);

            super.addInformation(stack, player, tooltip, advanced);
        }
    }

    public static class NaquadahReactor2 extends MetaTileEntityNaquadahReactor {

        public NaquadahReactor2(ResourceLocation metaTileEntityId) {
            super(metaTileEntityId, LabsRecipeMaps.NAQUADAH_REACTOR_RECIPES.get(1), GTValues.UV, 4);
        }

        @Override
        public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new MetaTileEntityNaquadahReactor.NaquadahReactor2(metaTileEntityId);
        }

        @Override
        protected TraceabilityPredicate getCasingPredicateBottom() {
            Material material;
            material = LabsMaterials.Taranium;
            return states(MetaBlocks.COMPRESSED.get(material).getBlock(material));
        }

        @Override
        protected TraceabilityPredicate getCasingPredicateTop() {
            return Loader.isModLoaded(LabsValues.EXTENDED_CRAFTING_MODID) ?
                    states(ModBlocks.blockStorage.getStateFromMeta(BlockStorage.Type.ULTIMATE.getMetadata())) :
                    air();
        }

        @Override
        @SideOnly(value = Side.CLIENT)
        public void addInformation(ItemStack stack, World player, @NotNull List<String> tooltip, boolean advanced) {
            super.addInformation(stack, player, tooltip, advanced);

            tooltip.add(translate("tooltip.nomilabs.naquadah_reactor_2.description"));
            addSharedInfo(tooltip);
        }
    }
}
