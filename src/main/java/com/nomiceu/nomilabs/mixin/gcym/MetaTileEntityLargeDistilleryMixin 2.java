package com.nomiceu.nomilabs.mixin.gcym;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.common.metatileentities.multiblock.standard.MetaTileEntityLargeDistillery;
import gregtech.api.capability.impl.DistillationTowerLogicHandler;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;

/**
 * Fixes <a href="https://github.com/Nomi-CEu/Nomi-CEu/issues/1256">Nomi CEu #1256</a>.
 */
@Mixin(value = MetaTileEntityLargeDistillery.class, remap = false)
public abstract class MetaTileEntityLargeDistilleryMixin extends GCYMRecipeMapMultiblockController {

    @Shadow
    @Final
    protected DistillationTowerLogicHandler handler;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityLargeDistilleryMixin(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
    }

    @Override
    public void setRecipeMapIndex(int index) {
        // Properly updates layer counts and fluid output info stored in recipeHandler after map change
        if (getAvailableRecipeMaps()[index] == RecipeMaps.DISTILLATION_RECIPES && structurePattern != null) {
            handler.determineLayerCount(structurePattern);
            handler.determineOrderedFluidOutputs();
        } else
            handler.invalidate();

        super.setRecipeMapIndex(index);
    }
}
