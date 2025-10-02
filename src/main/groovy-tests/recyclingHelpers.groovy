// Imports all static functions from the recycling section of the groovy helpers


import gregtech.api.recipes.RecipeMaps
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.RecyclingHelpers.*

// Recycling Helpers. Goes in Post Init.

// All Replace Recipes only works for replacing recipes where:
// output item, ignoring count, including meta, is the same as the old output item, ignoring count, including meta
// Also don't work recursively, each item that depends on these needs to be changed too

// All crafting recipe 'replacements' or 'creations' in this script also changes/adds recycling of the output!

// Change a recipe's output & input, replacing by name
replaceRecipeShaped("casing_assembly_control", item('gregtech:multiblock_casing', 3) * 1, [
        [ore('circuitLuv'), metaitem('plate.high_power_integrated_circuit'), ore('circuitLuv')],
        [metaitem('sensor.iv'), metaitem('frameIridium'), metaitem('emitter.iv')],
        [ore('circuitLuv'), metaitem('electric.motor.iv'), ore('circuitLuv')]
])

// Change a recipe's output & input, replacing by output
replaceRecipeShaped(item('gregtech:multiblock_casing', 4) * 2, item('gregtech:multiblock_casing', 4) * 1, [
        [ore('circuitLuv'), metaitem('plate.high_power_integrated_circuit'), ore('circuitLuv')],
        [metaitem('sensor.iv'), metaitem('frameIridium'), metaitem('emitter.iv')],
        [ore('circuitLuv'), metaitem('electric.motor.iv'), ore('circuitLuv')]
])

// Just change a recipe's output, replacing by name
replaceRecipeOutput("assembly_line", metaitem('assembly_line') * 4)

// Just change a recipe's output, replacing by output
replaceRecipeOutput(metaitem('circuit_assembler.uv'), metaitem('circuit_assembler.uv') * 4)

// Just change a recipe's input, replacing by name
replaceRecipeInput("casing_lv", [
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), ore('toolWrench'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')]
])

// Just change a recipe's input, replacing by output
replaceRecipeInput(metaitem('electric.motor.hv'), [
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), ore('toolWrench'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')]
])

// Add a recipe with recycling, with a defined name
createRecipe("uhv_batbuf_4x", metaitem('battery_buffer.uhv.4'), [
        [null, null, null],
        [null, metaitem('battery_buffer.uv.4'), null],
        [null, null, null]])

// Add a recipe with recycling, without a defined name
createRecipe(metaitem('battery_buffer.uhv.8'), [
        [null, null, null],
        [null, metaitem('battery_buffer.uv.8'), null],
        [null, null, null]])

/* Note that all versions of changeStackRecycling have a corresponding changeStackRecyclingNBT, which allows specification of nbt matcher and condition. */

// Add / Change recycling to a stack
changeStackRecycling(metaitem('battery_buffer.uhv.16'), [metaitem('battery_buffer.uv.16'), metaitem('charger.uv')])

// Add / Change recycling to a stack (with NBT)
changeStackRecyclingNBT(metaitem('tool.datastick'), [metaitem('battery_buffer.uv.16'), metaitem('charger.uv')], NBTMatcher.ANY, NBTCondition.ANY)

// Remove recycling to a stack
removeStackRecycling(metaitem('item_collector.hv'))

// Add / Change recycling to a stack, with items from a GT Recipe
changeStackRecyclingNBT(mods.gregtech.circuit_assembler.findByOutput([item('gregtech:meta_item_1', 262)], null, null, null)[0], NBTMatcher.ANY, NBTCondition.ANY)

// Add / Change recycling to a stack, with items sourced from the recipe with that output in that RecipeMap
// You can either use the actual map itself (RecipeMaps.CIRCUIT_ASSEMBLER) or the GrS reference (mods.gregtech.circuit_assembler)
// If more than one recipe is found, and the recipes have different item inputs, or no recipe is found, will error.
// Also accepts appending a Predicate<Recipe> at the end.
changeStackRecycling(metaitem('cover.screen'), RecipeMaps.ASSEMBLER_RECIPES)

// Replace Recycling In a Recipe Builder
mods.gregtech.assembler.recipeBuilder()
    .inputs(metaitem('cableGtSingleEuropium') * 2, metaitem('circuit.wetware_mainframe'), metaitem('wireGtQuadrupleEuropium') * 4, item('minecraft:chest') * 1, metaitem('hull.uhv'))
    .outputs(metaitem('charger.uhv'))
    .changeRecycling()
    .duration(500).EUt(120)
    .buildAndRegister()
