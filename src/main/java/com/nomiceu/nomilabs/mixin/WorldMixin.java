package com.nomiceu.nomilabs.mixin;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixinhelper.AccessibleDerivedWorldInfo;

/**
 * Allows Syncing of Properties Between Dimensions.
 */
@Mixin(World.class)
public class WorldMixin {

    @Shadow
    protected WorldInfo worldInfo;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void findProperWorldInfo(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn,
                                     Profiler profilerIn, boolean client, CallbackInfo ci) {
        if (LabsConfig.advanced.syncDimProperties && info instanceof AccessibleDerivedWorldInfo adri)
            worldInfo = adri.getDelegate();
    }
}
