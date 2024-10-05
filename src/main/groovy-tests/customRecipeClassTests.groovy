import com.nomiceu.nomilabs.groovy.ShapedConversionRecipe
import com.nomiceu.nomilabs.groovy.ShapedDummyRecipe
import net.minecraft.util.text.TextFormatting

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.NBTClearingRecipeHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*

// Custom Recipe Classes. Goes in Post Init.

// Use a Custom Recipe Class in Shaped Recipes!

/* Examples: Using Shaped Conversion Recipe */

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

// Example: Using Dummy Recipe
crafting.shapedBuilder()
        .output(item('minecraft:apple'))
        .matrix([[item('minecraft:leaves')]])
        .recipeClassFunction((output, width, height, ingredients) -> new ShapedDummyRecipe(output, ingredients, width, height, false))
        .register()

// Examples: NBT Clearing
// Note that provided OUTPUT must match the output item in ALL CASES for that recipe!
// Simplest Form, Same Item for Input and Output
nbtClearingRecipe(item('storagedrawers:compdrawers'))

// Different Item for Input and Output
nbtClearingRecipe(item('forge:bucketfilled'), item('minecraft:bucket'))

// Same Input/Output Item, Custom NBT Clearer
nbtClearingRecipe(item('storagedrawers:basicdrawers'), {
    var tag = transferSubTags(it, 'material') // Transfer material, saved generated tag
    tag = transferDrawerUpgradeData(it, tag) // Transfer Upgrades
    it.tagCompound = tag // Remember to Save!
})

// Different Input/Output Item, Custom NBT Clearer, Custom CanClear/Warning Tooltips
nbtClearingRecipe(item('minecraft:water_bucket'), item('minecraft:bucket'),
        {
            it.stackDisplayName = 'Old Water Bucket'
        },
        translatableLiteral('Great Water').addFormat(TextFormatting.AQUA),
        translatableLiteral('Warning: Great Water?').addFormat(TextFormatting.RED)
)

