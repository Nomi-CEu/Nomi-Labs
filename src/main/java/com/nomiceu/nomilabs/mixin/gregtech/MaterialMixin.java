package com.nomiceu.nomilabs.mixin.gregtech;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMaterial;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMaterialFlags;
import com.nomiceu.nomilabs.gregtech.mixinhelper.CompositionRecipeType;
import com.nomiceu.nomilabs.groovy.CompositionBuilder;

import gregtech.api.recipes.Recipe;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialFlag;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.stack.MaterialStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Allows setting material components, and saving original components.
 */
@Mixin(value = Material.class, remap = false)
public abstract class MaterialMixin implements AccessibleMaterial {

    @Unique
    private String setChemicalFormula;

    @Shadow
    private String chemicalFormula;

    @Unique
    private ImmutableList<MaterialStack> originalComponents = null;

    @Unique
    private boolean hasSetFlags = false;

    @Unique
    private boolean calculatedDecomp = false;

    @Unique
    private final Map<CompositionRecipeType, List<Recipe>> originalRecipes = new Object2ObjectOpenHashMap<>();

    @Unique
    private final MaterialFlag[] decompFlags = new MaterialFlag[] {
            MaterialFlags.DISABLE_DECOMPOSITION,
            MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING,
            MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING };

    @Shadow
    @NotNull
    protected abstract String calculateChemicalFormula();

    @Shadow
    protected abstract void calculateDecompositionType();

    @Shadow
    public abstract ImmutableList<MaterialStack> getMaterialComponents();

    @Shadow
    @Final
    @NotNull
    private MaterialFlags flags;

    @Unique
    @Override
    public void setComponents(ImmutableList<MaterialStack> components, boolean changeFormula) {
        setComponents(components);
        if (changeFormula) {
            // Recalculate Chemical Formula and Decomposition Type
            // First set chemical formula to CT/Addon Set Formula (If Exists)
            chemicalFormula = setChemicalFormula;
            // Then Recalculate (Returns the set formula if it exists)
            chemicalFormula = calculateChemicalFormula();
        }
        if (chemicalFormula == null) chemicalFormula = "";
        recalculateDecompositionType();
    }

    @Unique
    @Override
    public void setComponents(ImmutableList<MaterialStack> components) {
        if (originalComponents == null) originalComponents = getMaterialComponents();
        try {
            // Java reflection because mixin dies at shadowing fields with private types
            Field f = Material.class.getDeclaredField("materialInfo");
            ((AccessibleMaterialInfo) f.get(this)).setComponentList(components);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {}
    }

    @Inject(method = "setFormula(Ljava/lang/String;Z)Lgregtech/api/unification/material/Material;", at = @At("RETURN"))
    private void setChemicalFormula(String formula, boolean withFormatting, CallbackInfoReturnable<Material> cir) {
        setChemicalFormula = chemicalFormula;
    }

    @Inject(method = "calculateDecompositionType", at = @At("HEAD"))
    private void saveSetFlags(CallbackInfo ci) {
        boolean hasDecompFlags = false;
        for (var flag : decompFlags) {
            if (!flags.hasFlag(flag)) continue;
            hasDecompFlags = true;
            break;
        }
        if (hasDecompFlags && !calculatedDecomp)
            hasSetFlags = true;

        calculatedDecomp = true;
    }

    @Inject(method = "addFlags([Lgregtech/api/unification/material/info/MaterialFlag;)V", at = @At("TAIL"))
    private void checkFlags(MaterialFlag[] flags, CallbackInfo ci) {
        if (hasSetFlags) return;

        for (var flag : decompFlags) {
            if (!ArrayUtils.contains(flags, flag)) continue;
            hasSetFlags = true;
            break;
        }
    }

    @Override
    @Unique
    public ImmutableList<MaterialStack> getOriginalComponents() {
        return originalComponents == null ? getMaterialComponents() : originalComponents;
    }

    @Override
    @Unique
    public void recalculateDecompositionType() {
        if (!hasSetFlags && calculatedDecomp) {
            ((AccessibleMaterialFlags) flags).removeFlags(MaterialFlags.DISABLE_DECOMPOSITION,
                    MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING,
                    MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING);
        }

        calculateDecompositionType();
    }

    @Unique
    @SuppressWarnings("unused")
    public CompositionBuilder changeComposition() {
        return new CompositionBuilder((Material) (Object) this);
    }

    @Override
    public void setOriginalRecipes(CompositionRecipeType type, List<Recipe> originals) {
        if (originalRecipes.containsKey(type)) return;
        originalRecipes.put(type, originals);
    }

    @Override
    public Map<CompositionRecipeType, List<Recipe>> getOriginalRecipes() {
        return ImmutableMap.copyOf(originalRecipes);
    }
}
