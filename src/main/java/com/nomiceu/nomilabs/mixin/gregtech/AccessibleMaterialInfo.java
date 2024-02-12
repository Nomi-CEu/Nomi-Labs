package com.nomiceu.nomilabs.mixin.gregtech;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.stack.MaterialStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Allows setting the component list in Material Infos.
 */
@Mixin(targets = "gregtech.api.unification.material.Material$MaterialInfo", remap = false)
public interface AccessibleMaterialInfo {
    @Accessor(value = "componentList")
    void setComponentList(ImmutableList<MaterialStack> components);
}
