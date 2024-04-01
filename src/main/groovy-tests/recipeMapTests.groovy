// Import Recipe Search Helpers, used for Chanced Item and Fluid Ingredients


import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.GTRecipeHelpers.*

// Find and Removing GT Recipe Helpers. Goes in Post Init.

// Building Test Recipes
mods.gregtech.arc_furnace.recipeBuilder().inputs(metaitem('nomilabs:dustImpureOsmiridium8020')).outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64).EUt(50).duration(30).buildAndRegister()
mods.gregtech.arc_furnace.recipeBuilder().inputs(item('minecraft:stick')).outputs(item('minecraft:apple') * 64).EUt(50).duration(30).buildAndRegister()
mods.gregtech.arc_furnace.recipeBuilder().inputs(item('minecraft:yellow_flower')).outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64).chancedOutput(item('minecraft:apple') * 64, 50, 1).chancedFluidOutput(fluid('fluorine') * 2000, 50, 1).EUt(50).duration(30).buildAndRegister()
mods.gregtech.arc_furnace.recipeBuilder().inputs(metaitem('nomilabs:dustOsmiridium8020')).outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64).chancedOutput(item('minecraft:apple') * 64, 50, 1).chancedFluidOutput(fluid('fluorine') * 2000, 50, 1).EUt(50).duration(30).buildAndRegister()
mods.gregtech.arc_furnace.recipeBuilder().inputs(metaitem('nomilabs:dustPureOsmiridium8020')).outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64).EUt(50).duration(30).buildAndRegister()

// Find/Remove By Input Extensions (Are Lists of: List<ItemStack> itemInputs, List<FluidStack> fluidInputs)
// mods.gregtech.<RECIPE_MAP>.removeByInput to remove, mods.gregtech.<RECIPE_MAP>.find to find (Returns null if no recipe found)
// Original: [long voltage, Inputs... (see above)] (Matches/Removes any recipe with that input, and that voltage or more, CHECKING AMOUNT)
// ALL FIND/REMOVE BY INPUT EXTENSIONS IGNORE THE AMOUNT!
// Three Extensions:
// [GTRecipeCategory category, Inputs... (see above)] (Matches/Removes any recipe with that input, and that category)
// [Inputs... (see above)] (Matches/Removes any recipe with that input)
// [Predicate<Recipe> predicate, Inputs... (see above)] (Matches/Removes any recipe with that input, and matching that predicate)
mods.gregtech.arc_furnace.removeByInput([item('minecraft:yellow_flower')], null)

// Find/Remove By Output
// Outputs Specification: List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs, List<ChancedItemOutput> chancedItemOutputs, List<ChancedFluidOutput> chancedFluidOutputs
// Chanced Item/Fluid Outputs: chanced(item/fluid, chance, chanceBoost)
// mods.gregtech.<RECIPE_MAP>.removeByOutput to remove, mods.gregtech.<RECIPE_MAP>.findByOutput to find (Returns null if no recipes found)
// ALL FIND/REMOVE BY OUTPUT OPTIONS IGNORE THE AMOUNT!
// Four Options:
// [long voltage, Outputs... (see above)] (Matches/Removes any recipe with that output, and that voltage or more)
// [GTRecipeCategory category, Outputs... (see above)] (Matches/Removes any recipe with that output, and that category)
// [Outputs... (see above)] (Matches/Removes any recipe with that output)
// [Predicate<Recipe> predicate, Outputs... (see above)] (Matches/Removes any recipe with that output, and matching that predicate)
mods.gregtech.arc_furnace.removeByOutput(50, [item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64], null, [chanced(item('minecraft:apple') * 64, 50, 1)], [chanced(fluid('fluorine') * 2000, 50, 1)])

// NBT Helpers for Recipe Builder
// inputNBT version for ItemStack
// wildInputNBT version for ItemStack

mods.gregtech.assembler.recipeBuilder()
    .inputNBT(metaitem('nomilabs:dustPureOsmiridium8020'), NBTMatcher.ANY, NBTCondition.ANY)
    .inputWildNBT(metaitem('nomilabs:dustOsmiridium8020')) // Same as above (Except the ItemStack of course)
    .outputs(item('minecraft:apple') * 64)
    .EUt(30).duration(30)
    .buildAndRegister()