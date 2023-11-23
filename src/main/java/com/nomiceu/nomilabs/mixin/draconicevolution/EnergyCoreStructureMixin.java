package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.world.EnergyCoreStructure;
import com.nomiceu.nomilabs.integration.draconicevolution.BlockStateEnergyCoreStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnergyCoreStructure.class, remap = false)
public class EnergyCoreStructureMixin {
    /**
     * Overrides the initialize function so we can return our own.
     */
    @Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
    public void initialize(TileEnergyStorageCore core, CallbackInfoReturnable<EnergyCoreStructure> cir) {
        cir.setReturnValue(new BlockStateEnergyCoreStructure(core));
    }
}
