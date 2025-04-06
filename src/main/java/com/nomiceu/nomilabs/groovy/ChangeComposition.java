package com.nomiceu.nomilabs.groovy;

import java.util.Collections;
import java.util.Deque;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMaterial;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.CompositionRecipeType;
import com.nomiceu.nomilabs.mixin.gregtech.AccessibleDecompositionRecipeHandler;

import gregicality.multiblocks.api.fluids.GCYMFluidStorageKeys;
import gregicality.multiblocks.api.recipes.GCYMRecipeMaps;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeFluidInput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@GroovyBlacklist
public class ChangeComposition {

    public static void reloadCompositionRecipes() {
        var specs = LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER.needReloading;
        if (specs.isEmpty()) return;

        var time = System.currentTimeMillis();

        changeDecomp(specs);
        changeABS(specs);
        changeMixer(specs);
        finalize(specs);

        NomiLabs.LOGGER.info("Reloading Composition Recipes took {}ms", System.currentTimeMillis() - time);
    }

    public static void restore(CompositionBuilder.CompositionSpecification spec) {
        var mat = (AccessibleMaterial) spec.material;

        // Replace Chemical Formula and components
        mat.labs$setComponents(mat.labs$getOriginalComponents(), true);

        // Remove New Generated Recipes & Restore Old Recipes
        for (var entry : mat.labs$getOriginalRecipes().entrySet()) {
            entry.getKey().remove(spec.material);
            if (entry.getValue() == null) continue;
            for (var recipe : entry.getValue()) {
                entry.getKey().map.compileRecipe(recipe);
            }
        }
    }

    private static void changeDecomp(Deque<CompositionBuilder.CompositionSpecification> specs) {
        NomiLabs.LOGGER.debug("Replacing Decomp Recipes...");
        specs.stream().filter((spec) -> spec.changeDecomposition)
                .distinct() // Should take the newest version, since isDistinct preserves first dupe, and we are using
                            // the Deque as a LIFO structure
                .forEach((spec) -> {
                    NomiLabs.LOGGER.debug("---------------------------");
                    NomiLabs.LOGGER.debug("Processing Spec For Decomp:");
                    NomiLabs.LOGGER.debug(spec);
                    NomiLabs.LOGGER.debug("---------------------------");

                    var material = spec.material;
                    NomiLabs.LOGGER.debug("Removing Decomp Recipes for {}...", material.getRegistryName());

                    removeDecompRecipe(CompositionRecipeType.ELECTROLYZER, material);
                    removeDecompRecipe(CompositionRecipeType.CENTRIFUGE, material);

                    if (spec.components.isEmpty()) return;

                    var mat = (AccessibleMaterial) material;
                    // Temp Set Components so the handler makes correct recipe
                    mat.labs$setComponents(spec.components);
                    mat.labs$recalculateDecompositionType();

                    NomiLabs.LOGGER.debug("Adding Decomp Recipes for {}...", material.getRegistryName());
                    OrePrefix prefix = material.hasProperty(PropertyKey.DUST) ? OrePrefix.dust : null;
                    AccessibleDecompositionRecipeHandler.processDecomposition(prefix, material);
                });

        NomiLabs.LOGGER.debug("");
    }

    private static void changeABS(Deque<CompositionBuilder.CompositionSpecification> specs) {
        NomiLabs.LOGGER.debug("Replacing ABS Recipes...");

        // Since we already checked to see if the ABS flags are valid during the builder check phase, it should be fine
        // now
        specs.stream().filter((spec) -> spec.changeABS)
                .distinct() // Should take the newest version, since isDistinct preserves first dupe, and we are using
                            // the Deque as a LIFO structure
                .forEach((spec) -> {
                    NomiLabs.LOGGER.debug("------------------------");
                    NomiLabs.LOGGER.debug("Processing Spec For ABS:");
                    NomiLabs.LOGGER.debug(spec);
                    NomiLabs.LOGGER.debug("------------------------");

                    var material = spec.material;
                    NomiLabs.LOGGER.debug("Removing ABS Recipes for {}...", material.getRegistryName());

                    removeABSRecipe(material);

                    if (spec.components.isEmpty()) return;

                    var mat = (AccessibleMaterial) material;
                    // Temp Set Components so the handler makes correct recipe
                    mat.labs$setComponents(spec.components);

                    NomiLabs.LOGGER.debug("Adding ABS Recipes for {}...", material.getRegistryName());
                    ABSRecipeReplacer.REPLACE_PRODUCER.produce(material, material.getProperty(PropertyKey.BLAST));
                });

        NomiLabs.LOGGER.debug("");
    }

    private static void changeMixer(Deque<CompositionBuilder.CompositionSpecification> specs) {
        NomiLabs.LOGGER.debug("Replacing Mixer Recipes...");

        // Since we already checked to see if the material has a dust property during the builder check phase, it should
        // be fine now
        specs.stream().filter((spec) -> spec.changeMixer)
                .distinct() // Should take the newest version, since isDistinct preserves first dupe, and we are using
                            // the Deque as a LIFO structure
                .forEach((spec) -> {
                    NomiLabs.LOGGER.debug("--------------------------");
                    NomiLabs.LOGGER.debug("Processing Spec For Mixer:");
                    NomiLabs.LOGGER.debug(spec);
                    NomiLabs.LOGGER.debug("--------------------------");

                    var material = spec.material;
                    NomiLabs.LOGGER.debug("Removing Mixer Recipes for {}...", material.getRegistryName());

                    var originalRecipe = removeMixerRecipe(material);
                    if (originalRecipe == null) return;

                    var EUt = spec.mixerEUt == -1 ? originalRecipe.getEUt() : spec.mixerEUt;
                    var duration = spec.mixerDuration == -1 ? originalRecipe.getDuration() : spec.mixerDuration;
                    var circuit = spec.mixerCircuit == -1 ? getCircuit(originalRecipe.getInputs()) : spec.mixerCircuit;
                    var outputAmount = spec.mixerOutputAmount == -1 ? spec.components.stream()
                            .mapToInt((mat) -> (int) mat.amount).sum() : spec.mixerOutputAmount;

                    if (spec.components.isEmpty()) return;

                    NomiLabs.LOGGER.debug("Adding Mixer Recipes for {}...", material.getRegistryName());
                    var builder = RecipeMaps.MIXER_RECIPES.recipeBuilder()
                            .inputs(getItemInputsFromComponents(spec.components).toArray(new GTRecipeInput[0]))
                            .fluidInputs(getFluidInputsFromComponents(spec.components))
                            .outputs(OreDictUnifier.get(OrePrefix.dust, material, outputAmount))
                            .EUt(EUt).duration(duration);

                    if (circuit != 0) builder.circuitMeta(circuit);
                    builder.buildAndRegister();
                });

        NomiLabs.LOGGER.debug("");
    }

    private static void finalize(Deque<CompositionBuilder.CompositionSpecification> specs) {
        // Replace Chemical Formula for those that need it, else revert the change to the material's components
        var iter = specs.descendingIterator();
        while (iter.hasNext()) {
            var spec = iter.next();
            var mat = (AccessibleMaterial) spec.material;
            if (spec.changeChemicalFormula)
                mat.labs$setComponents(spec.components, true);
            else
                mat.labs$setComponents(mat.labs$getOriginalComponents());
        }
    }

    public static void removeDecompRecipe(CompositionRecipeType type, Material input) {
        ItemStack itemInput = ItemStack.EMPTY;
        FluidStack fluidInput = null;
        if (input.hasProperty(PropertyKey.DUST))
            itemInput = OreDictUnifier.get(OrePrefix.dust, input);
        else
            fluidInput = input.getFluid(1);
        var map = type.map;
        var recipe = map.find(itemInput.isEmpty() ? Collections.emptyList() : Collections.singletonList(itemInput),
                fluidInput == null ? Collections.emptyList() : Collections.singletonList(fluidInput),
                (recipe1) -> true);
        ((AccessibleMaterial) input).labs$setOriginalRecipes(type,
                recipe == null ? Collections.emptyList() : Collections.singletonList(recipe));
        if (recipe == null) return;
        NomiLabs.LOGGER.debug("Removing Decomp Recipe for {} @ {} in recipe map {}.",
                itemInput.getItem().getRegistryName(), itemInput.getMetadata(),
                map.getUnlocalizedName());
        map.removeRecipe(recipe);
    }

    public static void removeABSRecipe(Material input) {
        Fluid fluid = input.getFluid(GCYMFluidStorageKeys.MOLTEN);
        if (fluid == null) {
            fluid = input.getFluid(FluidStorageKeys.LIQUID);
        }
        var recipes = ((AccessibleRecipeMap) GCYMRecipeMaps.ALLOY_BLAST_RECIPES)
                .findByOutput(Collections.emptyList(), Collections.singletonList(new FluidStack(fluid, 1)),
                        Collections.emptyList(), Collections.emptyList(), (r) -> true);
        ((AccessibleMaterial) input).labs$setOriginalRecipes(CompositionRecipeType.ALLOY_BLAST,
                recipes == null ? Collections.emptyList() : recipes);
        if (recipes == null) return;
        for (var recipe : recipes) {
            NomiLabs.LOGGER.debug("Removing ABS Recipe with inputs {} and fluid inputs {}.", recipe.getInputs(),
                    recipe.getFluidInputs());
            GCYMRecipeMaps.ALLOY_BLAST_RECIPES.removeRecipe(recipe);
        }
    }

    @Nullable
    public static Recipe removeMixerRecipe(Material input) {
        var recipes = ((AccessibleRecipeMap) RecipeMaps.MIXER_RECIPES)
                .findByOutput(Collections.singletonList(OreDictUnifier.get(OrePrefix.dust, input)),
                        Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(), (r) -> true);
        ((AccessibleMaterial) input).labs$setOriginalRecipes(CompositionRecipeType.MIXER,
                recipes == null ? Collections.emptyList() : recipes);
        if (recipes == null) return null;
        for (var recipe : recipes) {
            NomiLabs.LOGGER.debug("Removing Mixer Recipe with inputs {} and fluid inputs {}.", recipe.getInputs(),
                    recipe.getFluidInputs());
            RecipeMaps.MIXER_RECIPES.removeRecipe(recipe);
        }
        return recipes.get(0);
    }

    private static int getCircuit(List<GTRecipeInput> inputs) {
        for (var input : inputs) {
            if (!input.isNonConsumable()) continue;
            for (var stack : input.getInputStacks()) {
                var circuit = IntCircuitIngredient.getCircuitConfiguration(stack);
                if (circuit != 0) return circuit;
            }
        }
        return 0;
    }

    @NotNull
    private static List<GTRecipeInput> getItemInputsFromComponents(List<MaterialStack> components) {
        List<GTRecipeInput> result = new ObjectArrayList<>();
        for (var mat : components) {
            if (!mat.material.hasProperty(PropertyKey.DUST)) continue;
            result.add(new GTRecipeItemInput(OreDictUnifier.get(OrePrefix.dust, mat.material, (int) mat.amount)));
        }
        return result;
    }

    @NotNull
    private static List<GTRecipeInput> getFluidInputsFromComponents(List<MaterialStack> components) {
        List<GTRecipeInput> result = new ObjectArrayList<>();
        for (var mat : components) {
            if (mat.material.hasProperty(PropertyKey.DUST) || !mat.material.hasFluid()) continue;
            result.add(new GTRecipeFluidInput(mat.material.getFluid((int) (mat.amount * 1000))));
        }
        return result;
    }
}
