package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleProcessingArrayWorkable;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.ICleanroomProvider;
import gregtech.api.metatileentity.multiblock.ICleanroomReceiver;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;

@Mixin(targets = "gregtech.common.metatileentities.multi.electric.MetaTileEntityProcessingArray$ProcessingArrayWorkable",
       remap = false)
public abstract class ProcessingArrayWorkableMixin extends MultiblockRecipeLogic
                                                   implements AccessibleProcessingArrayWorkable {

    @Shadow
    MetaTileEntity mte;

    /**
     * Mandatory Ignored Constructor
     */
    public ProcessingArrayWorkableMixin(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    @Override
    public void setCleanroomForMTE(ICleanroomProvider cleanroom) {
        if (mte != null && mte instanceof ICleanroomReceiver receiver) {
            receiver.setCleanroom(cleanroom);
        }
    }
}
