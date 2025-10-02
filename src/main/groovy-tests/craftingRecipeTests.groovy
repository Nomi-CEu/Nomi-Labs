import com.nomiceu.nomilabs.LabsValues
import com.nomiceu.nomilabs.groovy.ShapedConversionRecipe
import com.nomiceu.nomilabs.groovy.ShapedDummyRecipe
import com.nomiceu.nomilabs.groovy.SimpleIIngredient
import com.nomiceu.nomilabs.util.ItemMeta
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.common.Loader

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
// Note that provided Inputs/Outputs' NBT will be IGNORED! If Output must have X NBT, add such in the Clearer Logic.
// Simplest Form, Same Item for Input and Output
if (Loader.isModLoaded(LabsValues.STORAGE_DRAWERS_MODID))
    nbtClearingRecipe(item('storagedrawers:compdrawers'))

// Different Item for Input and Output
nbtClearingRecipe(item('forge:bucketfilled'), item('minecraft:bucket'))

// Same Input/Output Item, Custom NBT Clearer
if (Loader.isModLoaded(LabsValues.STORAGE_DRAWERS_MODID)) {
    nbtClearingRecipe(item('storagedrawers:basicdrawers'), {
        var tag = transferSubTags(it, 'material') // Transfer material, saved generated tag
        tag = transferDrawerUpgradeData(it, tag) // Transfer Upgrades
        it.tagCompound = tag // Remember to Save!
    })
}

// Different Input/Output Item, Custom NBT Clearer, Custom CanClear/Warning Tooltips
nbtClearingRecipe(item('minecraft:water_bucket'), item('minecraft:bucket'),
        {
            it.stackDisplayName = 'Old Water Bucket'
        },
        translatableLiteral('Great Water').addFormat(TextFormatting.AQUA),
        translatableLiteral('Warning: Great Water?').addFormat(TextFormatting.RED)
)

// Examples: Strict Recipes

/**
 * This means, in JEI, the list of 'matching stacks' will be displayed
 * exactly as set, instead of expanding wildcards and removing duplicates.
 */
// Only JEI display is affected! This is only useful with custom IIngredients!

// Custom IIngredient: Returns different NBTs of Stone (NBT's are auto-condensed by JEI)
class TestStone extends SimpleIIngredient {

    ItemStack stone = item('minecraft:stone')

    @Override
    ItemStack[] getMatchingStacks() {
        return [
                item('minecraft:stone'),
                item('minecraft:stone').withNbt(['display': ['Name': 'Test']]),
                item('minecraft:stone').withNbt(['display': ['Name': 'Test 2']]),
                item('minecraft:stone').withNbt(['display': ['Name': 'Test 3']]),
        ].toArray()
    }

    @Override
    boolean test(ItemStack itemStack) {
        return ItemMeta.compare(itemStack, stone)
    }
}

// Without Strict: Shaped
crafting.shapedBuilder()
        .name('shaped-no-strict')
        .output(item('minecraft:dirt'))
        .matrix([[new TestStone()]])
        .register()

// With Strict: Shaped
crafting.shapedBuilder()
        .name('shaped-strict')
        .output(item('minecraft:dirt'))
        .matrix([[new TestStone()]])
        .strictJEIHandling()
        .register()

// Without Strict: Shapeless
crafting.shapelessBuilder()
        .name('shapeless-no-strict')
        .output(item('minecraft:dirt'))
        .input(new TestStone())
        .register()

// With Strict: Shapeless
crafting.shapelessBuilder()
        .name('shapeless-strict')
        .output(item('minecraft:dirt'))
        .input(new TestStone())
        .strictJEIHandling()
        .register()

