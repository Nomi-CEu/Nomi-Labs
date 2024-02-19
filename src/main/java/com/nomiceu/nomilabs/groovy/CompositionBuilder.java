package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMaterial;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;
import gregicality.multiblocks.api.fluids.GCYMFluidStorageKeys;
import gregicality.multiblocks.api.unification.GCYMMaterialFlags;
import gregicality.multiblocks.api.unification.properties.GCYMPropertyKey;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("unused")
public class CompositionBuilder {
    private final Material material;

    private List<MaterialStack> components = null;
    private boolean changeChemicalFormula = false;
    private boolean changeDecomposition = false;
    private boolean changeABS = false;
    private MixerSpecification changeMixer = null;

    public CompositionBuilder(@NotNull Material material) {
        this.material = material;
    }

    public CompositionBuilder setComponents(List<IIngredient> components) {
        this.components = getMatFromIIngredient(components);
        return this;
    }

    public CompositionBuilder removeComponents() {
        components = Collections.emptyList();
        return this;
    }

    public CompositionBuilder reloadComponents() {
        components = ((AccessibleMaterial) material).getOriginalComponents();
        return this;
    }

    public CompositionBuilder changeChemicalFormula() {
        changeChemicalFormula = true;
        return this;
    }

    public CompositionBuilder changeDecompositionRecipes() {
        changeDecomposition = true;
        return this;
    }

    public CompositionBuilder changeABS() {
        changeABS = true;
        return this;
    }

    public CompositionBuilder changeMixer(){
        changeMixer = new MixerSpecification();
        return this;
    }

    public CompositionBuilder changeMixer(MixerSpecification builder) {
        changeMixer = builder;
        return this;
    }

    public void change() {
        /* Checks */
        if (components == null) {
            throwIfShould(new IllegalArgumentException("Cannot change when components, or removal, has not been specified!"));
            return;
        }
        if (!changeChemicalFormula && !changeDecomposition && !changeABS && changeMixer == null) {
            throwIfShould(new IllegalArgumentException("Cannot change when change method(s) have not been specified!"));
            return;
        }
        if (changeDecomposition && material.hasFlag(MaterialFlags.DISABLE_DECOMPOSITION)) {
            throwIfShould(new IllegalArgumentException("Cannot change when decomposition changing is specified, but the material has DISABLE_DECOMPOSITION flag!"));
            return;
        }
        if (changeMixer != null && !material.hasProperty(PropertyKey.DUST)){
            throwIfShould(new IllegalArgumentException("Cannot change when mixer changing is specified, but the material does not have a Dust Property!"));
            return;
        }
        if (changeABS &&
                (!material.hasProperty(GCYMPropertyKey.ALLOY_BLAST) || !material.hasProperty(PropertyKey.BLAST)
                        || !material.hasFluid() ||
                        (material.getFluid(GCYMFluidStorageKeys.MOLTEN) == null &&  material.getFluid(FluidStorageKeys.LIQUID) == null)
                        || material.hasFlag(GCYMMaterialFlags.NO_ALLOY_BLAST_RECIPES))) {
            throwIfShould(new IllegalArgumentException("Cannot change when ABS changing is specified, but the material cannot generate ABS recipes to a Fluid!"));
            return;
        }

        LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER.changeMaterialDecomp(new CompositionSpecification(this));
    }

    public static void throwIfShould(Exception e) {
        if (LabsGroovyHelper.isRunningGroovyScripts()) {
            GroovyLog.get().exception(e);
        }
    }

    /* Helpers */
    public static List<MaterialStack> getMatFromIIngredient(List<IIngredient> ingredients) {
        List<MaterialStack> result = new ArrayList<>();
        for (var ingredient : ingredients) {
            //noinspection ConstantValue
            if ((Object) ingredient instanceof ItemStack stack) {
                var mat = getMatFromStack(stack);
                result.add(mat);
                continue;
            }
            if ((Object) ingredient instanceof MaterialStack stack) {
                result.add(stack);
                continue;
            }
            if ((Object) ingredient instanceof FluidStack stack) {
                result.add(getMatFromFluid(stack));
                continue;
            }
            IllegalArgumentException e = new IllegalArgumentException("Component Specification must be an Item Stack, a Material Stack, or a Fluid Stack!");

            if (!LabsGroovyHelper.isRunningGroovyScripts()) throw e;

            GroovyLog.get().exception(e);
            result.add(null);
        }
        if (result.stream().anyMatch(Objects::isNull)) return null;
        return result;
    }

    @Nullable
    public static MaterialStack getMatFromStack(ItemStack stack) {
        MaterialStack material = OreDictUnifier.getMaterial(stack);
        if (material == null) {
            IllegalArgumentException e = new IllegalArgumentException(
                    String.format("Could not find Material for Stack %s @ %s, with %s.",
                            stack.getItem().getRegistryName(), stack.getMetadata(),
                            stack.hasTagCompound() ? "Tag " + stack.getTagCompound() : "No Tag"));

            if (!LabsGroovyHelper.isRunningGroovyScripts()) throw e;

            GroovyLog.get().exception(e);
            return null;
        }
        return material.copy(stack.getCount());
    }

    @Nullable
    public static MaterialStack getMatFromFluid(FluidStack fluid) {
        // Material Getting needs modid, which we don't have
        // Recursive through all registries to find it
        Material mat = getMaterialFromFluid(fluid);
        if (mat == null) return null;

        if (fluid.amount % 1000 != 0 || fluid.amount < 1000) {
            IllegalArgumentException e = new IllegalArgumentException("Fluid Amount must be divisible by 1000, and be at least 1000!");

            if (!LabsGroovyHelper.isRunningGroovyScripts()) throw e;

            GroovyLog.get().exception(e);
            return null;
        }
        return new MaterialStack(mat, fluid.amount / 1000);
    }

    @Nullable
    public static Material getMaterialFromFluid(FluidStack fluid) {
        // Material Getting needs modid, which we don't have
        // Recursive through all registries to find it
        var name = fluid.getFluid().getName();
        Material mat = null;
        for (var registry : GregTechAPI.materialManager.getRegistries()) {
            if (!registry.containsKey(name)) continue;

            var foundMat = registry.getObject(name);
            if (foundMat == null || !foundMat.hasProperty(PropertyKey.FLUID)) continue;

            mat = foundMat;
            break;
        }
        if (mat == null) {
            IllegalArgumentException e = new IllegalArgumentException(
                    String.format("Could not find Material for Fluid %s!", fluid.getFluid().getName()));

            if (!LabsGroovyHelper.isRunningGroovyScripts()) throw e;

            GroovyLog.get().exception(e);
            return null;
        }
        return mat;
    }

    public static class MixerSpecification {
        private int EUt = -1;
        private int duration = -1;
        private int circuit = -1;
        private int outputAmount = -1;

        public MixerSpecification overrideEUt(int EUt) {
            if (EUt <= 0) {
                CompositionBuilder.throwIfShould(new IllegalArgumentException("EUt override must be larger than 0!"));
                return this;
            }
            this.EUt = EUt;
            return this;
        }

        public MixerSpecification overrideDuration(int duration) {
            if (duration <= 0) {
                CompositionBuilder.throwIfShould(new IllegalArgumentException("Duration override must be larger than 0!"));
                return this;
            }
            this.duration = duration;
            return this;
        }

        public MixerSpecification overrideCircuit(int circuit) {
            if (circuit < 0) {
                CompositionBuilder.throwIfShould(new IllegalArgumentException("Circuit override must be larger or equal to 0!"));
                return this;
            }
            this.circuit = circuit;
            return this;
        }

        public MixerSpecification overrideOutputAmount(int outputAmount) {
            if (outputAmount <= 0) {
                CompositionBuilder.throwIfShould(new IllegalArgumentException("Output amount override must be larger or equal to 0!"));
                return this;
            }
            this.outputAmount = outputAmount;
            return this;
        }
    }

    public static class CompositionSpecification {
        public final Material material;
        public final ImmutableList<MaterialStack> components;
        public final boolean changeChemicalFormula;
        public final boolean changeDecomposition;
        public final boolean changeABS;
        public final boolean changeMixer;
        public final int mixerEUt;
        public final int mixerDuration;
        public final int mixerCircuit;
        public final int mixerOutputAmount;

        private CompositionSpecification(CompositionBuilder builder) {
            material = builder.material;
            components = ImmutableList.copyOf(builder.components);
            changeChemicalFormula = builder.changeChemicalFormula;
            changeDecomposition = builder.changeDecomposition;
            changeABS = builder.changeABS;
            changeMixer = builder.changeMixer != null;
            if (changeMixer) {
                mixerEUt = builder.changeMixer.EUt;
                mixerDuration = builder.changeMixer.duration;
                mixerCircuit = builder.changeMixer.circuit;
                mixerOutputAmount = builder.changeMixer.outputAmount;
            } else {
                mixerEUt = -1;
                mixerDuration = -1;
                mixerCircuit = -1;
                mixerOutputAmount = -1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositionSpecification that = (CompositionSpecification) o;
            return Objects.equals(material, that.material);
        }

        @Override
        public int hashCode() {
            return Objects.hash(material);
        }

        @Override
        public String toString() {
            return "DecompositionSpecification{" +
                    "material=" + material +
                    ", components=" + components +
                    ", changeChemicalFormula=" + changeChemicalFormula +
                    ", changeDecomposition=" + changeDecomposition +
                    ", changeABS=" + changeABS +
                    ", changeMixer=" + changeMixer +
                    ", mixerEUt=" + mixerEUt +
                    ", mixerDuration=" + mixerDuration +
                    ", mixerCircuit=" + mixerCircuit +
                    ", mixerOutputAmount=" + mixerOutputAmount +
                    '}';
        }
    }
}
