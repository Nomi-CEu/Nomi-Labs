package com.nomiceu.nomilabs.mixin;

import com.google.common.collect.BiMap;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

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

    @Inject(method = "loadFluidDefaults(Lcom/google/common/collect/BiMap;Ljava/util/Set;)V", at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraftforge/fluids/FluidRegistry;currentBucketFluids:Ljava/util/Set;",
                    shift = At.Shift.AFTER),
            require = 1)
    private static void loadSpecifiedFluidDefaults(BiMap<Fluid, Integer> localFluidIDs, Set<String> defaultNames, CallbackInfo ci) {
        if (LabsConfig.advanced.defaultFluids.length == 0) return;
        int changed = 0;
        for (var fluidName : LabsConfig.advanced.defaultFluids) {
            NomiLabs.LOGGER.debug("Processing Input {}", fluidName);

            var fluid = masterFluidReference.get(fluidName);
            if (fluid == null)
                throw new IllegalArgumentException("Nomi Labs: Config Entry " + fluidName + "in Default Fluids Config doesn't exist!");
            var oldFluid = fluids.get(fluid.getName());
            if (oldFluid == null) continue;

            var oldName = masterFluidReference.inverse().get(oldFluid);
            if (oldName.equals(fluidName)) {
                NomiLabs.LOGGER.debug("Default fluid of {} is already {}. Not Changing...", fluid.getName(), fluidName);
                continue;
            }

            NomiLabs.LOGGER.debug("Changing default fluid of {}, from {} to {}.", fluid.getName(), oldName, fluidName);
            changed++;

            fluids.forcePut(fluid.getName(), fluid);
            defaultFluidName.forcePut(fluid.getName(), fluidName);
            Integer id = localFluidIDs.remove(oldFluid);
            localFluidIDs.forcePut(fluid, id);
            fluidNames.forcePut(id, fluid.getName());
        }
        if (changed == 0) {
            NomiLabs.LOGGER.info("No Fluids Changed.");
            return;
        }
        NomiLabs.LOGGER.info("Changed {} Default Fluid(s)!", changed);
        fluidIDs = localFluidIDs;
    }
}
