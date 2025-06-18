package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.metatileentities.storage.MetaTileEntityCreativeTank;

/**
 * Makes the default mbPerTick be Int.MAX_VALUE.
 */
@Mixin(value = MetaTileEntityCreativeTank.class, remap = false)
public class MetaTileEntityCreativeTankMixin {

    @Shadow
    private int mBPerCycle;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setDefaultMbPerTick(ResourceLocation metaTileEntityId, CallbackInfo ci) {
        mBPerCycle = Integer.MAX_VALUE;
    }
}
