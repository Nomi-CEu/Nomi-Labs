package com.nomiceu.nomilabs.mixin.nae2;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.definitions.IItemDefinition;
import appeng.recipes.game.DisassembleRecipe;
import co.neeve.nae2.NAE2;

/**
 * Fixes Fluid Storage Cells disassembling into Item Storage Components.
 */
@Mixin(value = DisassembleRecipe.class, remap = false)
public class DisassembleRecipeMixin {

    @Shadow
    @Final
    private Map<IItemDefinition, IItemDefinition> cellMappings;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void replaceDisassemble(CallbackInfo ci) {
        var definitions = NAE2.definitions();
        var items = definitions.items();
        var mats = definitions.materials();

        cellMappings.put(items.storageCellFluid256K(), mats.cellFluidPart256K());
        cellMappings.put(items.storageCellFluid1024K(), mats.cellFluidPart1024K());
        cellMappings.put(items.storageCellFluid4096K(), mats.cellFluidPart4096K());
        cellMappings.put(items.storageCellFluid16384K(), mats.cellFluidPart16384K());
    }
}
