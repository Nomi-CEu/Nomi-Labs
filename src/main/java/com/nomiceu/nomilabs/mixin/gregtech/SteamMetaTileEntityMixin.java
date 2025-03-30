package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleSteamMetaTileEntity;

import gregtech.api.capability.impl.RecipeLogicSteam;
import gregtech.api.metatileentity.SteamMetaTileEntity;

/**
 * Allows seeing if venting stuck.
 */
@Mixin(value = SteamMetaTileEntity.class, remap = false)
public class SteamMetaTileEntityMixin implements AccessibleSteamMetaTileEntity {

    @Shadow
    protected RecipeLogicSteam workableHandler;

    @Override
    @Unique
    public boolean labs$ventingStuck() {
        return workableHandler.isVentingStuck();
    }
}
