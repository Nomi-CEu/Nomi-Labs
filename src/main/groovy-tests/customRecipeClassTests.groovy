import com.nomiceu.nomilabs.groovy.ShapedConversionRecipe

// Custom Recipe Classes. Goes in Post Init.

// Use a Custom Recipe Class in Shaped Recipes!

// Using Key Based Matrix
crafting.shapedBuilder()
        .output(metaitem('dye.white') * 2)
        .matrix('D')
        .key('D', item('minecraft:dye', 15))
        // Two Options:
        // ItemStack output, int width, int height, List<IIngredient> ingredients
        // ItemStack output, int width, int height, List<IIngredient> ingredients, boolean mirrored, Closure<ItemStack> recipeFunction, Closure<Void> recipeAction
        .recipeClassFunction((output, width, height, ingredients, mirrored, recipeFunction, recipeAction) -> new ShapedConversionRecipe(output, ingredients, width, height, mirrored, recipeFunction, recipeAction))
        .register()


// Using Ingredient Matrix
crafting.shapedBuilder()
        .output(metaitem('dye.white'))
        .matrix([[null, null], [item('minecraft:dye', 15), null]])
        // Two Options:
        // ItemStack output, int width, int height, List<IIngredient> ingredients
        // ItemStack output, int width, int height, List<IIngredient> ingredients, boolean mirrored, Closure<ItemStack> recipeFunction, Closure<Void> recipeAction
        .recipeClassFunction((output, width, height, ingredients) -> new ShapedConversionRecipe(output, ingredients, width, height))
        .register()
