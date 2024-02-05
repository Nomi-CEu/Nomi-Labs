package com.nomiceu.nomilabs.mixin.gregtech;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.gregtech.AccessibleMaterial;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Allows setting material components.
 */
@Mixin(value = Material.class, remap = false)
public abstract class MaterialMixin implements AccessibleMaterial {
    @Unique
    private String setChemicalFormula;

    @Shadow
    private String chemicalFormula;

    @Shadow
    @NotNull
    protected abstract String calculateChemicalFormula();

    @Shadow
    protected abstract void calculateDecompositionType();

    @Override
    public void setComponents(List<MaterialStack> components) {
        try {
            // Java reflection because mixin dies at shadowing fields with private types
            Field f = Material.class.getDeclaredField("materialInfo");
            ((AccessibleMaterialInfo) f.get(this)).setComponentList(ImmutableList.copyOf(components));
            // Recalculate Chemical Formula and Decomposition Type
            // First set chemical formula to CT/Addon Set Formula (If Exists)
            chemicalFormula = setChemicalFormula;
            // Then Recalculate (Returns the set formula if it exists)
            chemicalFormula = calculateChemicalFormula();
            calculateDecompositionType();
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {}
    }

    @Inject(method = "setFormula(Ljava/lang/String;Z)Lgregtech/api/unification/material/Material;", at = @At("RETURN"))
    public void setChemicalFormula(String formula, boolean withFormatting, CallbackInfoReturnable<Material> cir) {
        setChemicalFormula = chemicalFormula;
    }
}
