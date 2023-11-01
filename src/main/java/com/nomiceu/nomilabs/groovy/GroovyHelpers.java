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
    public static class RecipeHelpers {
        public static void reloadRecyclingRecipes() {
            ReplaceRecipe.reloadRecyclingRecipes();
        }
        public static void replaceRecipeShaped(ResourceLocation name, ItemStack output, List<List<IIngredient>> input) {
            ReplaceRecipe.replaceRecipeShaped(name, output, input);
        }
        public static void replaceRecipeShaped(String name, ItemStack output, List<List<IIngredient>> input) {
            if (name.contains(":"))
                replaceRecipeShaped(new ResourceLocation(name), output, input);
            else
                replaceRecipeShaped(GTUtility.gregtechId(name), output, input);
        }
    }
}
