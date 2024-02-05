package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.compat.mods.jei.JeiPlugin;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.sandbox.ClosureHelper;
import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import com.nomiceu.nomilabs.util.LabsTranslate;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.util.GTUtility;
import gregtech.client.utils.TooltipHelper;
import groovy.lang.Closure;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The interface for groovy to interact with.
 */
@SuppressWarnings("unused")
public class GroovyHelpers {
    public static class TranslationHelpers {
        public static String translate(String key, Object... params) {
            return LabsTranslate.translate(key, params);
        }
        public static String translateWithBackup(String key, String backup, Object... params) {
            return LabsTranslate.translateWithBackup(key, backup, params);
        }

        public static String translateFormat(String key, TooltipHelper.GTFormatCode format, Object... params) {
            return LabsTranslate.translateFormat(key, format, params);
        }
        public static String translateWithBackupFormat(String key, String backup, TooltipHelper.GTFormatCode format, Object... params) {
            return LabsTranslate.translateWithBackupFormat(key, backup, format, params);
        }

        public static String format(String str, TextFormatting... formats) {
            return LabsTranslate.format(str, formats);
        }

        public static String format(String str, TooltipHelper.GTFormatCode... formats) {
            return LabsTranslate.format(str, formats);
        }

        public static String format(String str, LabsTranslate.Format... formats) {
            return LabsTranslate.format(str, formats);
        }
    }
    public static class JEIHelpers {
        public static void addDescription(ItemStack stack, String... description) {
            JEIPlugin.addGroovyDescription(stack, description);
        }
        public static void addRecipeOutputTooltip(ItemStack stack, String... tooltip) {
            JEIPlugin.addGroovyRecipeOutputTooltip(stack, tooltip);
        }
        public static void addRecipeOutputTooltip(ItemStack stack, ResourceLocation recipeName, String... tooltip) {
            JEIPlugin.addGroovyRecipeOutputTooltip(stack, recipeName, tooltip);
        }
    }
    public static class MaterialHelpers {
        public static void hideMaterial(Material material) {
            MaterialHelper.forMaterialItem(material, JeiPlugin::hideItem);
            MaterialHelper.forMaterialFluid(material, (fluid) -> JeiPlugin.HIDDEN_FLUIDS.add(toFluidStack(fluid)));
        }
        public static void removeAndHideMaterial(Material material) {
            MaterialHelper.forMaterialItem(material, (stack) ->
                    ModSupport.JEI.get().removeAndHide(IngredientHelper.toIIngredient(stack)));
            // Normal Hiding for Fluids, they don't have recipes
            MaterialHelper.forMaterialFluid(material, (fluid) -> JeiPlugin.HIDDEN_FLUIDS.add(toFluidStack(fluid)));
        }
        public static void yeetMaterial(Material material) {
            removeAndHideMaterial(material);
        }
        public static void forMaterial(Material material, Closure<ItemStack> itemAction, Closure<Fluid> fluidAction) {
            forMaterialItem(material, itemAction);
            forMaterialFluid(material, fluidAction);
        }
        public static void forMaterialItem(Material material, Closure<ItemStack> action) {
            MaterialHelper.forMaterialItem(material, (stack) -> ClosureHelper.call(action, stack));
        }
        public static void forMaterialFluid(Material material, Closure<Fluid> action) {
            MaterialHelper.forMaterialFluid(material, (fluid) -> ClosureHelper.call(action, fluid));
        }

        private static FluidStack toFluidStack(Fluid fluid) {
            return new FluidStack(fluid, 1);
        }
    }
    public static class RecipeRecyclingHelpers {
        public static void replaceRecipeShaped(String name, ItemStack output, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeShaped(new ResourceLocation(name), output, inputs);
            else
                replaceRecipeShaped(GTUtility.gregtechId(name), output, inputs);
        }

        public static void replaceRecipeShaped(ResourceLocation name, ItemStack output, List<List<IIngredient>> inputs) {
            ReplaceRecipe.replaceRecipeShaped(name, output, inputs);
        }

        public static void replaceRecipeShaped(ItemStack oldOutput, ItemStack newOutput, List<List<IIngredient>> inputs) {
            ReplaceRecipe.replaceRecipeShaped(oldOutput, newOutput,inputs);
        }

        public static void replaceRecipeOutput(String name, ItemStack output) {
            if (name.contains(":"))
                replaceRecipeOutput(new ResourceLocation(name), output);
            else
                replaceRecipeOutput(GTUtility.gregtechId(name), output);
        }

        public static void replaceRecipeOutput(ResourceLocation name, ItemStack newOutput) {
            ReplaceRecipe.replaceRecipeOutput(name, newOutput);
        }

        public static void replaceRecipeOutput(ItemStack oldOutput, ItemStack newOutput) {
            ReplaceRecipe.replaceRecipeOutput(oldOutput, newOutput);
        }

        public static void replaceRecipeInput(String name, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeInput(new ResourceLocation(name), inputs);
            else
                replaceRecipeInput(GTUtility.gregtechId(name), inputs);
        }

        public static void replaceRecipeInput(ResourceLocation name, List<List<IIngredient>> newInputs) {
            ReplaceRecipe.replaceRecipeInput(name, newInputs);
        }

        public static void replaceRecipeInput(ItemStack oldOutput, List<List<IIngredient>> newInputs) {
            ReplaceRecipe.replaceRecipeInput(oldOutput, newInputs);
        }

        public static void createRecipe(String name, ItemStack output, List<List<IIngredient>> input) {
            ReplaceRecipe.createRecipe(name, output, input);
        }

        public static void createRecipe(ItemStack output, List<List<IIngredient>> input) {
            ReplaceRecipe.createRecipe(output, input);
        }

        public static void changeStackRecycling(ItemStack output, List<IIngredient> ingredients) {
            ReplaceRecipe.changeStackRecycling(output, ingredients);
        }
        public static void removeStackRecycling(ItemStack output) {
            ReplaceRecipe.changeStackRecycling(output, Collections.emptyList());
        }
    }
    public static class ReplaceDecompositionHelpers {
        public static void replaceDecomposition(Material material, List<IIngredient> components) {
            ReplaceDecomposition.replaceDecomposition(material, getMatFromIIngredient(components));
        }

        public static void replaceDecomposition(MaterialStack material, List<IIngredient> components) {
            ReplaceDecomposition.replaceDecomposition(material.material, getMatFromIIngredient(components));
        }

        public static void replaceDecomposition(ItemStack stack, List<IIngredient> components) {
            MaterialStack mat = getMatFromStack(stack);
            if (mat == null) return;

            ReplaceDecomposition.replaceDecomposition(mat.material, getMatFromIIngredient(components));
        }

        public static void replaceDecomposition(FluidStack stack, List<IIngredient> components) {
            Material mat = getMaterialFromFluid(stack);
            if (mat == null) return;

            ReplaceDecomposition.replaceDecomposition(mat, getMatFromIIngredient(components));
        }

        public static void removeDecomposition(Material material) {
            ReplaceDecomposition.replaceDecomposition(material, Collections.emptyList());
        }

        public static void removeDecomposition(MaterialStack material) {
            ReplaceDecomposition.replaceDecomposition(material.material, Collections.emptyList());
        }

        public static void removeDecomposition(ItemStack stack) {
            MaterialStack mat = getMatFromStack(stack);
            if (mat == null) return;

            ReplaceDecomposition.replaceDecomposition(mat.material, Collections.emptyList());
        }

        public static void removeDecomposition(FluidStack stack) {
            Material mat = getMaterialFromFluid(stack);
            if (mat == null) return;

            ReplaceDecomposition.replaceDecomposition(mat, Collections.emptyList());
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

                if (!GroovyScript.getSandbox().isRunning()) throw e;

                GroovyLog.get().exception(e);
                result.add(null);
            }
            if (result.stream().anyMatch(Objects::isNull)) return null;
            return result;
        }

        @Nullable
        private static MaterialStack getMatFromStack(ItemStack stack) {
            MaterialStack material = OreDictUnifier.getMaterial(stack);
            if (material == null) {
                IllegalArgumentException e = new IllegalArgumentException(
                        String.format("Could not find Material for Stack %s @ %s, with %s.",
                                stack.getItem().getRegistryName(), stack.getMetadata(),
                                stack.hasTagCompound() ? "Tag " + stack.getTagCompound() : "No Tag"));

                if (!GroovyScript.getSandbox().isRunning()) throw e;

                GroovyLog.get().exception(e);
                return null;
            }
            return material.copy(stack.getCount());
        }

        @Nullable
        private static MaterialStack getMatFromFluid(FluidStack fluid) {
            // Material Getting needs modid, which we don't have
            // Recursive through all registries to find it
            Material mat = getMaterialFromFluid(fluid);
            if (mat == null) return null;

            if (fluid.amount % 1000 != 0 || fluid.amount < 1000) {
                IllegalArgumentException e = new IllegalArgumentException("Fluid Amount must be divisible by 1000, and be at least 1000!");

                if (!GroovyScript.getSandbox().isRunning()) throw e;

                GroovyLog.get().exception(e);
                return null;
            }
            return new MaterialStack(mat, fluid.amount / 1000);
        }

        @Nullable
        private static Material getMaterialFromFluid(FluidStack fluid) {
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

                if (!GroovyScript.getSandbox().isRunning()) throw e;

                GroovyLog.get().exception(e);
                return null;
            }
            return mat;
        }
    }
}
