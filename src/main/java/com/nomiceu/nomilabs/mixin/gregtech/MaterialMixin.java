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
    private String labs$setChemicalFormula;

    @Shadow
    private String chemicalFormula;

    @Unique
    private ImmutableList<MaterialStack> labs$originalComponents = null;

    @Unique
    private boolean labs$hasSetFlags = false;

    @Unique
    private boolean labs$calculatedDecomp = false;

    @Unique
    private final Map<CompositionRecipeType, List<Recipe>> labs$originalRecipes = new Object2ObjectOpenHashMap<>();

    @Unique
    private final MaterialFlag[] labs$decompFlags = new MaterialFlag[] {
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
    public void labs$setComponents(ImmutableList<MaterialStack> components, boolean changeFormula) {
        labs$setComponents(components);
        if (changeFormula) {
            // Recalculate Chemical Formula and Decomposition Type
            // First set chemical formula to CT/Addon Set Formula (If Exists)
            chemicalFormula = labs$setChemicalFormula;
            // Then Recalculate (Returns the set formula if it exists)
            chemicalFormula = calculateChemicalFormula();
        }
        if (chemicalFormula == null) chemicalFormula = "";
        labs$recalculateDecompositionType();
    }

    @Unique
    @Override
    public void labs$setComponents(ImmutableList<MaterialStack> components) {
        if (labs$originalComponents == null) labs$originalComponents = getMaterialComponents();
        try {
            // Java reflection because mixin dies at shadowing fields with private types
            Field f = Material.class.getDeclaredField("materialInfo");
            ((AccessibleMaterialInfo) f.get(this)).setComponentList(components);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {}
    }

    @Inject(method = "setFormula(Ljava/lang/String;Z)Lgregtech/api/unification/material/Material;", at = @At("RETURN"))
    private void setChemicalFormula(String formula, boolean withFormatting, CallbackInfoReturnable<Material> cir) {
        labs$setChemicalFormula = chemicalFormula;
    }

    @Inject(method = "calculateDecompositionType", at = @At("HEAD"))
    private void saveSetFlags(CallbackInfo ci) {
        boolean hasDecompFlags = false;
        for (var flag : labs$decompFlags) {
            if (!flags.hasFlag(flag)) continue;
            hasDecompFlags = true;
            break;
        }
        if (hasDecompFlags && !labs$calculatedDecomp)
            labs$hasSetFlags = true;

        labs$calculatedDecomp = true;
    }

    @Inject(method = "addFlags([Lgregtech/api/unification/material/info/MaterialFlag;)V", at = @At("TAIL"))
    private void checkFlags(MaterialFlag[] flags, CallbackInfo ci) {
        if (labs$hasSetFlags) return;

        for (var flag : labs$decompFlags) {
            if (!ArrayUtils.contains(flags, flag)) continue;
            labs$hasSetFlags = true;
            break;
        }
    }

    @Override
    @Unique
    public ImmutableList<MaterialStack> labs$getOriginalComponents() {
        return labs$originalComponents == null ? getMaterialComponents() : labs$originalComponents;
    }

    @Override
    @Unique
    public void labs$recalculateDecompositionType() {
        if (!labs$hasSetFlags && labs$calculatedDecomp) {
            ((AccessibleMaterialFlags) flags).labs$removeFlags(MaterialFlags.DISABLE_DECOMPOSITION,
                    MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING,
                    MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING);
        }

        calculateDecompositionType();
    }

    @Unique
    @SuppressWarnings({ "unused", "AddedMixinMembersNamePattern" })
    public CompositionBuilder changeComposition() {
        return new CompositionBuilder((Material) (Object) this);
    }

    @Unique
    @Override
    public void labs$setOriginalRecipes(CompositionRecipeType type, List<Recipe> originals) {
        if (labs$originalRecipes.containsKey(type)) return;
        labs$originalRecipes.put(type, originals);
    }

    @Unique
    @Override
    public Map<CompositionRecipeType, List<Recipe>> labs$getOriginalRecipes() {
        return ImmutableMap.copyOf(labs$originalRecipes);
    }
}
