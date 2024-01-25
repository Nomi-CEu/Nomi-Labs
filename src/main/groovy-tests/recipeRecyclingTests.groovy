// Imports all static functions from the recycling section of the groovy helpers
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.RecipeRecyclingHelpers.*

// This should be placed in PostInit!

// All Replace Recipes only works for replacing recipes where:
// output item, ignoring count, including meta, is the same as the old output item, ignoring count, including meta
// Also don't work recursively, each item that depends on these needs to be changed too

// Change a recipe's output & input
replaceRecipeShaped("casing_assembly_control", item('gregtech:multiblock_casing', 3) * 1, [
        [ore('circuitLuv'), metaitem('plate.high_power_integrated_circuit'), ore('circuitLuv')],
        [metaitem('sensor.iv'), metaitem('frameIridium'), metaitem('emitter.iv')],
        [ore('circuitLuv'), metaitem('electric.motor.iv'), ore('circuitLuv')]
])

// Just change a recipe's output
replaceRecipeOutput("assembly_line", metaitem('assembly_line') * 4)

// Just change a recipe's input
replaceRecipeInput("casing_lv", [
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), ore('toolWrench'), metaitem('plateWroughtIron')],
        [metaitem('plateWroughtIron'), metaitem('plateWroughtIron'), metaitem('plateWroughtIron')]
])

replaceRecipeInput("electric_motor_hv", [
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

// Add / Change recycling to a stack
changeStackRecycling(metaitem('battery_buffer.uhv.16'), [metaitem('battery_buffer.uv.16'), metaitem('charger.uv')])