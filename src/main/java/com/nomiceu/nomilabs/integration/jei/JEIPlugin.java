package com.nomiceu.nomilabs.integration.jei;

import com.cleanroommc.groovyscript.compat.mods.jei.ShapedRecipeWrapper;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
    private static final Map<ItemStack, String[]> DESCRIPTIONS = new HashMap<>();
    @Override
    public void register(IModRegistry registry) {
        var jeiHelpers = registry.getJeiHelpers();

        // JEI does not recognise Custom Recipe Classes on its own
        // Uses the shaped recipe wrapper from GroovyScript
        registry.handleRecipes(PartialRecipe.class, recipe -> new ShapedRecipeWrapper(jeiHelpers, recipe), VanillaRecipeCategoryUid.CRAFTING);

        // Add Descriptions
        DESCRIPTIONS.forEach(((itemStack, strings) -> registry.addIngredientInfo(itemStack, VanillaTypes.ITEM, strings)));
    }

    public static void addDescription(@NotNull ItemStack stack, String... description) {
        DESCRIPTIONS.put(stack, description);
    }
}
