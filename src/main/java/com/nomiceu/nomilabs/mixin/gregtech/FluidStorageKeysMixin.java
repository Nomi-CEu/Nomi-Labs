package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.function.Function;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.fluids.FluidState;
import gregtech.api.fluids.store.FluidStorageKey;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.PropertyKey;

/**
 * Temporary fix for Wrong Gas Translation Keys.
 */
@Mixin(value = FluidStorageKeys.class, remap = false)
public class FluidStorageKeysMixin {

    @Redirect(method = "<clinit>",
              at = @At(value = "NEW",
                       target = "(Lnet/minecraft/util/ResourceLocation;Lgregtech/api/unification/material/info/MaterialIconType;Ljava/util/function/Function;Ljava/util/function/Function;Lgregtech/api/fluids/FluidState;)Lgregtech/api/fluids/store/FluidStorageKey;"))
    private static FluidStorageKey fixGasTranslation(ResourceLocation resourceLocation, MaterialIconType iconType,
                                                     Function<Material, String> registryNameFunction,
                                                     Function<Material, String> translationKeyFunction,
                                                     FluidState defaultFluidState) {
        // Only Change for Gas
        if (resourceLocation.getPath().equals("gas")) {
            return new FluidStorageKey(resourceLocation, iconType, registryNameFunction,
                    m -> {
                        if (m.hasProperty(PropertyKey.DUST)) {
                            return "gregtech.fluid.gas_vapor";
                        }

                        FluidProperty property = m.getProperty(PropertyKey.FLUID);
                        if (m.isElement() || property == null || property.getPrimaryKey() != FluidStorageKeys.GAS) {
                            return "gregtech.fluid.gas_generic";
                        }
                        return "gregtech.fluid.generic";
                    },
                    defaultFluidState);
        }
        return new FluidStorageKey(resourceLocation, iconType, registryNameFunction, translationKeyFunction,
                defaultFluidState);
    }
}
