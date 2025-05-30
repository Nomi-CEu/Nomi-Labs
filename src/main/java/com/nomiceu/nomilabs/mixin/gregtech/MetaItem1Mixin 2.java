package com.nomiceu.nomilabs.mixin.gregtech;

import static gregtech.common.items.MetaItems.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMetaValueItem;
import com.nomiceu.nomilabs.gregtech.mixinhelper.BucketItemFluidContainer;

import gregtech.api.items.metaitem.FilteredFluidStats;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.common.items.MetaItem1;

/**
 * Implements <a href="https://github.com/GregTechCEu/GregTech/pull/2660">GTCEu #2660</a>.
 * <p>
 * Also doubles the steel cell's capacity
 */
@Mixin(value = MetaItem1.class, remap = false)
public class MetaItem1Mixin {

    @Inject(method = "registerSubItems", at = @At("RETURN"))
    private void changeCellBehaviour(CallbackInfo ci) {
        labs$editCell(FLUID_CELL);
        labs$editCell(FLUID_CELL_UNIVERSAL);

        // Steel: Buff Capacity to 16000.
        ((AccessibleMetaValueItem) FLUID_CELL_LARGE_STEEL).labs$clearStats();
        FLUID_CELL_LARGE_STEEL.addComponents(
                new FilteredFluidStats(16000,
                        Materials.Steel.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature(), true, false,
                        false, false, true),
                new BucketItemFluidContainer());

        labs$editCell(FLUID_CELL_LARGE_ALUMINIUM);
        labs$editCell(FLUID_CELL_LARGE_STAINLESS_STEEL);
        labs$editCell(FLUID_CELL_LARGE_TITANIUM);
        labs$editCell(FLUID_CELL_LARGE_TUNGSTEN_STEEL);
    }

    @Unique
    private void labs$editCell(MetaItem<?>.MetaValueItem cell) {
        var filtered = cell.getAllStats().get(0);

        ((AccessibleMetaValueItem) cell).labs$clearStats();
        cell.addComponents(filtered, new BucketItemFluidContainer());
    }
}
