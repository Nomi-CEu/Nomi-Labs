package com.nomiceu.nomilabs.mixin.nuclearcraft;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.groovy.NCActiveCoolerHelper;

import nc.tile.fluid.TileActiveCooler;

/**
 * Allows for Tile Active Coolers to accept custom fluids.
 */
@Mixin(value = TileActiveCooler.class, remap = false)
public class TileActiveCoolerMixin {

    @Inject(method = "validFluids", at = @At("HEAD"), cancellable = true)
    private static void getProperValidFluids(CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(NCActiveCoolerHelper.getFluidNamesFromIDs());
    }
}
