package com.nomiceu.nomilabs.mixin.draconicevolution;

import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCoreStabilizer;
import com.nomiceu.nomilabs.integration.draconicevolution.TileEnergyCoreStabilizerLogic;

@Mixin(value = TileEnergyCoreStabilizer.class, remap = false)
public abstract class TileEnergyCoreStabilizerMixin {

    @Inject(method = "getBlocksForFrameMove", at = @At("HEAD"), cancellable = true)
    private void getBlocksForFrameMove(CallbackInfoReturnable<Iterable<BlockPos>> cir) {
        cir.setReturnValue(
                TileEnergyCoreStabilizerLogic.getBlocksForFrameMove((TileEnergyCoreStabilizer) (Object) this));
    }
}
