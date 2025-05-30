package com.nomiceu.nomilabs.mixin.gcym;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

import gregicality.multiblocks.common.metatileentities.multiblock.standard.MetaTileEntityAlloyBlastSmelter;
import gregtech.api.GTValues;

/**
 * Stops ABS JEI Preview showing Muffler as HV, leading to confusion.
 */
@Mixin(value = MetaTileEntityAlloyBlastSmelter.class, remap = false)
public class MetaTileEntityAlloyBlastSmelterMixin {

    // We have to make sure our injection point is after aisle specification,
    // which as arrays which we could accidentally inject into
    // We slice at the first `.where` call to prevent easy-to-break large ordinals with `.aisle` calls.
    @ModifyConstant(method = "getMatchingShapes",
                    constant = @Constant(intValue = GTValues.HV, ordinal = 0),
                    slice = @Slice(from = @At(value = "INVOKE",
                                              target = "Lgregtech/api/pattern/MultiblockShapeInfo$Builder;where(CLgregtech/api/metatileentity/MetaTileEntity;Lnet/minecraft/util/EnumFacing;)Lgregtech/api/pattern/MultiblockShapeInfo$Builder;",
                                              ordinal = 0)),
                    require = 1)
    private int useLV(int value) {
        return GTValues.LV;
    }
}
