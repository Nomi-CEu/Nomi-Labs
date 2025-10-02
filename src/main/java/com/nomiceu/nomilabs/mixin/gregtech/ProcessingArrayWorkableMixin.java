package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleProcessingArrayWorkable;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.ICleanroomProvider;
import gregtech.api.metatileentity.multiblock.ICleanroomReceiver;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;

/**
 * Fixes Edge Cases where Processing Arrays would not run recipes, due to Cleanroom States not being synced
 * correctly.<br>
 * To be removed when a release is made containing <a href="https://github.com/GregTechCEu/GregTech/pull/2566">GT PR
 * #2566</a>.
 */
@Mixin(targets = "gregtech.common.metatileentities.multi.electric.MetaTileEntityProcessingArray$ProcessingArrayWorkable",
       remap = false)
public abstract class ProcessingArrayWorkableMixin extends MultiblockRecipeLogic
                                                   implements AccessibleProcessingArrayWorkable {

    @Shadow
    MetaTileEntity mte;

    /**
     * Mandatory Ignored Constructor
     */
    private ProcessingArrayWorkableMixin(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    @Unique
    @Override
    public void labs$setCleanroomForMTE(ICleanroomProvider cleanroom) {
        if (mte != null && mte instanceof ICleanroomReceiver receiver) {
            receiver.setCleanroom(cleanroom);
        }
    }
}
