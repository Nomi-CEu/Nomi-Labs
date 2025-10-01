package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleProcessingArrayWorkable;

import gregtech.api.metatileentity.multiblock.ICleanroomProvider;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.RecipeMap;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityProcessingArray;

/**
 * Fixes Edge Cases where Processing Arrays would not run recipes, due to Cleanroom States not being synced
 * correctly.<br>
 * To be removed when a release is made containing <a href="https://github.com/GregTechCEu/GregTech/pull/2566">GT PR
 * #2566</a>.
 */
@Mixin(value = MetaTileEntityProcessingArray.class, remap = false)
public abstract class MetaTileEntityProcessingArrayMixin extends RecipeMapMultiblockController {

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityProcessingArrayMixin(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
    }

    @Unique
    @Override
    public void setCleanroom(ICleanroomProvider provider) {
        super.setCleanroom(provider);
        if (recipeMapWorkable instanceof AccessibleProcessingArrayWorkable apw) {
            apw.labs$setCleanroomForMTE(provider);
        }
    }
}
