package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityObjectHolder;

/**
 * Allows accessing whether the slots are blocked.
 */
@Mixin(value = MetaTileEntityObjectHolder.class, remap = false)
public interface MetaTileEntityObjectHolderAccessor {

    @Invoker
    boolean invokeIsSlotBlocked();
}
