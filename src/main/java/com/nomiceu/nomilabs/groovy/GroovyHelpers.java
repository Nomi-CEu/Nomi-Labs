package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.compat.mods.jei.JeiPlugin;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.sandbox.ClosureHelper;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import groovy.lang.Closure;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * The interface for groovy to interact with.
 */
@SuppressWarnings("unused")
public class GroovyHelpers {
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
        public static void reloadRecyclingRecipes() {
            ReplaceRecipe.reloadRecyclingRecipes();
        }
        public static void replaceRecipeShaped(ResourceLocation name, ItemStack output, List<List<IIngredient>> inputs) {
            ReplaceRecipe.replaceRecipeShaped(name, output, inputs);
        }
        public static void replaceRecipeShaped(String name, ItemStack output, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeShaped(new ResourceLocation(name), output, inputs);
            else
                replaceRecipeShaped(GTUtility.gregtechId(name), output, inputs);
        }
        public static void replaceRecipeOutput(ResourceLocation name, ItemStack output) {
            ReplaceRecipe.replaceRecipeOutput(name, output);
        }
        public static void replaceRecipeOutput(String name, ItemStack output) {
            if (name.contains(":"))
                replaceRecipeOutput(new ResourceLocation(name), output);
            else
                replaceRecipeOutput(GTUtility.gregtechId(name), output);
        }
        public static void replaceRecipeInput(ResourceLocation name, List<List<IIngredient>> inputs) {
            ReplaceRecipe.replaceRecipeInput(name, inputs);
        }
        public static void replaceRecipeInput(String name, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeInput(new ResourceLocation(name), inputs);
            else
                replaceRecipeInput(GTUtility.gregtechId(name), inputs);
        }
    }
}
