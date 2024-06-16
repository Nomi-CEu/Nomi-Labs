package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import java.util.*;
import java.util.stream.Collectors;

import com.nomiceu.nomilabs.gregtech.mixinhelper.IJEISpecialMultiblock;
import gregicality.multiblocks.api.metatileentity.GCYMMultiblockAbility;
import gregicality.multiblocks.common.GCYMConfigHolder;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.util.GTUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.gregtech.LabsTextures;

import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.pattern.*;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.BlockInfo;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.sound.GTSoundEvents;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public abstract class MetaTileEntityBaseChamber extends GCYMRecipeMapMultiblockController
                                                implements IJEISpecialMultiblock {

    // Constants
    public static final String BASE_TIER_KEY = "BaseTier";
    public static final String SPECIAL_TIER_KEY = "SpecialTier";

    // Values that will change (default set)
    protected byte baseTier = 1;
    protected byte specialTier = 1;

    // Values that will not change (must be set by super class)
    protected int voltageTier;
    protected boolean canParallel;
    protected List<Pair<IBlockState, Byte>> baseTiers;
    // First State: Light, Second State: Vent
    protected List<Triple<MetaTileEntity, MetaTileEntity, Byte>> specialTiers;

    // States (don't set in super class)
    protected Map<IBlockState, Byte> baseTierMap;
    protected Map<ResourceLocation, Byte> specialLightTierMap;
    protected Map<ResourceLocation, Byte> specialVentTierMap;

    protected MetaTileEntityBaseChamber(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        baseTier = context.get(BASE_TIER_KEY);
        specialTier = context.get(SPECIAL_TIER_KEY);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        baseTier = 1;
        specialTier = 1;
    }

    protected TraceabilityPredicate getCasingPredicateBase() {
        baseTierMap = new Object2ObjectOpenHashMap<>();
        baseTiers.forEach((pair) -> baseTierMap.put(pair.getKey(), pair.getValue()));
        return getTieredPredicateState(baseTierMap, baseTiers.stream().map(Pair::getKey).collect(Collectors.toList()),
                "nomilabs.multiblock.pattern.error.base_tier", BASE_TIER_KEY);
    }

    protected TraceabilityPredicate getCasingPredicateLamp() {
        specialLightTierMap = new Object2ObjectOpenHashMap<>();
        specialTiers.forEach((triple) -> specialLightTierMap.put(triple.getLeft().metaTileEntityId, triple.getRight()));
        return getTieredPredicateMetaTileEntity(specialLightTierMap,
                specialTiers.stream().map(Triple::getLeft).collect(Collectors.toList()),
                "nomilabs.multiblock.pattern.error.special_tier.tier",
                "nomilabs.multiblock.pattern.error.special_tier.direction", SPECIAL_TIER_KEY);
    }

    protected TraceabilityPredicate getCasingPredicateVent() {
        specialVentTierMap = new Object2ObjectOpenHashMap<>();
        specialTiers.forEach((triple) -> specialVentTierMap.put(triple.getMiddle().metaTileEntityId, triple.getRight()));
        return getTieredPredicateMetaTileEntity(specialVentTierMap,
                specialTiers.stream().map(Triple::getMiddle).collect(Collectors.toList()),
                "nomilabs.multiblock.pattern.error.special_tier", "nomilabs.multiblock.pattern.error.special_tier.direction", SPECIAL_TIER_KEY);
    }

    protected TraceabilityPredicate getTieredPredicateState(Map<IBlockState, Byte> tierMap, List<IBlockState> states,
                                                            String langKey, String contextKey) {
        return new TraceabilityPredicate(blockWorldState -> {
            IBlockState blockState = blockWorldState.getBlockState();
            if (tierMap.containsKey(blockState)) {
                byte tier = tierMap.get(blockState);
                byte currentTier = blockWorldState.getMatchContext().getOrPut(contextKey, tier);
                if (currentTier != tier) {
                    blockWorldState.setError(new PatternStringError(langKey));
                    return false;
                }
                return true;
            }
            return false;
        }, () -> states.stream()
                .map(state -> new BlockInfo(state, null))
                .toArray(BlockInfo[]::new))
                        .addTooltips(langKey);
    }

    protected TraceabilityPredicate getTieredPredicateMetaTileEntity(Map<ResourceLocation, Byte> tierMap,
                                                                     List<MetaTileEntity> metaTileEntities,
                                                                     String wrongTierKey, String wrongDirectionKey,
                                                                     String contextKey) {
        return tilePredicate((blockWorldState, mte) -> {
            if (tierMap.containsKey(mte.metaTileEntityId)) {
                byte tier = tierMap.get(mte.metaTileEntityId);
                byte currentTier = blockWorldState.getMatchContext().getOrPut(contextKey, tier);
                if (currentTier != tier) {
                    blockWorldState.setError(new PatternStringError(wrongTierKey));
                    return false;
                }
                if (mte.getFrontFacing() != getTopAbsolute()) {
                    blockWorldState.setError(new PatternStringError(wrongDirectionKey));
                    return false;
                }
                return true;
            }
            return false;
        }, () -> metaTileEntities.stream().filter(Objects::nonNull).map(tile -> {
            MetaTileEntityHolder holder = new MetaTileEntityHolder();
            holder.setMetaTileEntity(tile);
            holder.getMetaTileEntity().onPlacement();
            holder.getMetaTileEntity().setFrontFacing(EnumFacing.SOUTH);
            return new BlockInfo(MetaBlocks.MACHINE.getDefaultState(), holder);
        }).toArray(BlockInfo[]::new));
    }

    public EnumFacing getTopAbsolute() {
        if (frontFacing.getAxis() == EnumFacing.Axis.Y) return upwardsFacing;

        if (upwardsFacing.getAxis() == EnumFacing.Axis.Z)
            return upwardsFacing == EnumFacing.SOUTH ? EnumFacing.DOWN : EnumFacing.UP;
        return upwardsFacing == EnumFacing.EAST ? frontFacing.rotateYCCW() : frontFacing.rotateY();
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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        // We can't separate the base & special
        // This is because we can't apply half a pattern...
        // we need to generate a list (which is secretly a matrix, special are rows, base are columns)
        // Then we need to figure out how to traverse and limit that matrix properly with both special & base
        // Then we don't need to mess with rendering
        var builder = getBuilder();
        List<MultiblockShapeInfo> result = new ArrayList<>();
        for (var special : specialTiers) {
            var specialBuilder = getSpecialShapeFromBuilder(builder.shallowCopy(), special.getLeft(),
                    special.getMiddle());
            for (var base : baseTiers) {
                result.add(getBaseShapeFromBuilder(specialBuilder.shallowCopy(), base.getKey()).build());
            }
        }
        return result;
    }

    public abstract MultiblockShapeInfo.Builder getBuilder();

    public abstract MultiblockShapeInfo.Builder getBaseShapeFromBuilder(MultiblockShapeInfo.Builder builder,
                                                                        IBlockState state);

    public abstract MultiblockShapeInfo.Builder getSpecialShapeFromBuilder(MultiblockShapeInfo.Builder builder,
                                                                           MetaTileEntity light, MetaTileEntity vent);

    @Override
    public boolean isParallel() {
        return canParallel;
    }

    @Override
    public MultiblockInfoRecipeWrapper getWrapper() {
        return new BaseChamberWrapper(this, baseTiers.size(), specialTiers.size());
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        MultiblockDisplayText.builder(textList, isStructureFormed())
                .setWorkingStatus(recipeMapWorkable.isWorkingEnabled(), recipeMapWorkable.isActive())
                .addEnergyUsageLine(getEnergyContainer())
                .addEnergyTierLine(GTUtility.getTierByVoltage(recipeMapWorkable.getMaxVoltage()))
                .addCustom(tl -> {
                    // Tiered Hatch Line
                    if (isStructureFormed()) {
                        List<ITieredMetaTileEntity> list = getAbilities(GCYMMultiblockAbility.TIERED_HATCH);
                        if (GCYMConfigHolder.globalMultiblocks.enableTieredCasings && !list.isEmpty()) {
                            long maxVoltage = Math.min(GTValues.V[list.get(0).getTier()],
                                    Math.max(energyContainer.getInputVoltage(), energyContainer.getOutputVoltage()));
                            String voltageName = GTValues.VNF[list.get(0).getTier()];
                            tl.add(new TextComponentTranslation("gcym.multiblock.tiered_hatch.tooltip", maxVoltage,
                                    voltageName));
                        }
                    }
                })
                .addCustom(tl -> {
                    tl.add(new TextComponentString(getTopAbsolute().getName()));
                })
                .addParallelsLine(recipeMapWorkable.getParallelLimit())
                .addWorkingStatusLine()
                .addProgressLine(recipeMapWorkable.getProgressPercent());
    }
}
