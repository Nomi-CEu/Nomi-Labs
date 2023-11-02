package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.IIngredient;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * The interface for groovy to interact with.
 */
@SuppressWarnings("unused")
public class GroovyHelpers {
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
