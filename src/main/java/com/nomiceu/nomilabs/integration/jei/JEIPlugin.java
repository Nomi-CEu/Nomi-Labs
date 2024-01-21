package com.nomiceu.nomilabs.integration.jei;

import com.nomiceu.nomilabs.groovy.PartialRecipe;
import com.nomiceu.nomilabs.util.ItemTagMeta;
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
    private static final Map<ItemTagMeta, String[]> RECIPE_OUTPUT_TOOLTIPS = new HashMap<>();

    private static IModRegistry registry;

    @Override
    public void register(IModRegistry registry) {
        JEIPlugin.registry = registry;
        var jeiHelpers = registry.getJeiHelpers();

        // JEI does not recognise Custom Recipe Classes on its own
        registry.handleRecipes(PartialRecipe.class, recipe -> new PartialRecipeWrapper(jeiHelpers, recipe), VanillaRecipeCategoryUid.CRAFTING);

        // Add Descriptions
        DESCRIPTIONS.forEach(((itemStack, strings) ->
                registry.addIngredientInfo(itemStack, VanillaTypes.ITEM, String.join("\n\n", strings))));
    }

    public static void addDescription(@NotNull ItemStack stack, String... description) {
        DESCRIPTIONS.put(stack, description);
    }

    public static void addRecipeOutputTooltip(@NotNull ItemStack stack, String... tooltip) {
        RECIPE_OUTPUT_TOOLTIPS.put(new ItemTagMeta(stack), tooltip);
    }

    public static Map<ItemTagMeta, String[]> getRecipeOutputTooltips() {
        return RECIPE_OUTPUT_TOOLTIPS;
    }
}
