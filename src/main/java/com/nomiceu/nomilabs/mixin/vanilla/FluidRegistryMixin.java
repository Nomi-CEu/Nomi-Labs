package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.Set;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.BiMap;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.fluid.FluidRegistryMixinHelper;

@Mixin(value = FluidRegistry.class, remap = false)
public class FluidRegistryMixin {

    @Shadow
    static BiMap<String, Fluid> fluids;

    @Shadow
    static BiMap<Fluid, Integer> fluidIDs;

    @Shadow
    static BiMap<Integer, String> fluidNames;

    @Shadow
    static BiMap<String, String> defaultFluidName;

    @Shadow
    static BiMap<String, Fluid> masterFluidReference;

    @Inject(method = "loadFluidDefaults(Lcom/google/common/collect/BiMap;Ljava/util/Set;)V",
            at = @At(
                     value = "FIELD",
                     target = "Lnet/minecraftforge/fluids/FluidRegistry;currentBucketFluids:Ljava/util/Set;",
                     shift = At.Shift.AFTER),
            require = 1)
    private static void loadSpecifiedFluidDefaults(BiMap<Fluid, Integer> localFluidIDs, Set<String> defaultNames,
                                                   CallbackInfo ci) {
        var defaultFluids = FluidRegistryMixinHelper.getDefaultFluids();
        if (defaultFluids == null) return;
        int changed = 0;
        for (var entry : defaultFluids.entrySet()) {
            NomiLabs.LOGGER.debug("Processing Conflict {}", entry.getKey());

            var fluid = masterFluidReference.get(entry.getValue());
            var oldFluid = fluids.get(entry.getKey());

            var oldName = masterFluidReference.inverse().get(oldFluid);
            if (oldName.equals(entry.getValue())) {
                NomiLabs.LOGGER.debug("Default fluid of {} is already {}. Not Changing...", entry.getKey(),
                        entry.getValue());
                continue;
            }

            NomiLabs.LOGGER.debug("Changing default fluid of {}, from {} to {}.", entry.getKey(), oldName,
                    entry.getValue());
            changed++;

            fluids.forcePut(entry.getKey(), fluid);
            defaultFluidName.forcePut(entry.getKey(), entry.getValue());
            Integer id = localFluidIDs.remove(oldFluid);
            localFluidIDs.forcePut(fluid, id);
            fluidNames.forcePut(id, entry.getKey());
        }
        if (changed == 0) {
            NomiLabs.LOGGER.info("No Fluids Changed.");
            return;
        }
        NomiLabs.LOGGER.info("Changed {} Default Fluid(s)!", changed);
        fluidIDs = localFluidIDs;
    }
}
