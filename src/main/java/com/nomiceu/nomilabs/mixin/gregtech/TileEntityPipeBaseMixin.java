package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.pipenet.tile.PipeCoverableImplementation;
import gregtech.api.pipenet.tile.TileEntityPipeBase;

/**
 * Fixes Transferring of Covers when Pipe from Normal -> Tickable.
 * <p>
 * Covers are cleared instead of transferred.
 */
@Mixin(value = TileEntityPipeBase.class, remap = false)
public class TileEntityPipeBaseMixin {

    @Redirect(method = "transferDataFrom",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/pipenet/tile/PipeCoverableImplementation;transferDataTo(Lgregtech/api/pipenet/tile/PipeCoverableImplementation;)V"),
              require = 1)
    private void transferNotClear(PipeCoverableImplementation instance, PipeCoverableImplementation arg) {
        arg.transferDataTo(instance);
    }
}
