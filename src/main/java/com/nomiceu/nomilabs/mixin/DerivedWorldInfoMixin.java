package com.nomiceu.nomilabs.mixin;

import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.mixinhelper.AccessibleDerivedWorldInfo;

/**
 * Allows Accessing the Delegate of Derived World Infos.
 */
@Mixin(DerivedWorldInfo.class)
public class DerivedWorldInfoMixin implements AccessibleDerivedWorldInfo {

    @Shadow
    @Final
    private WorldInfo delegate;

    @Override
    public WorldInfo getDelegate() {
        return delegate;
    }
}
