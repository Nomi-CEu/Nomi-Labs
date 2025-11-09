import gregtech.api.recipes.Recipe

import static gregtech.api.GTValues.*

// Tests the 'scriptingChemReactorImprovements.groovy' config. Goes in Post Init.

// To understand this config, first you must understand the behaviour of CR (chemical reactor) and LCR (large chemical reactor) recipes.
// Whenever a CR recipe is added, a recipe with the same inputs, outputs, duration, eut, etc. is added to the LCR recipe map.
// This does not happen when a LCR recipe is added; in that case it is added to the LCR recipe map only.
// Then, this is displayed in two categories in JEI:
// CR recipes are displayed in a category
// LCR recipes that DO NOT exist in the CR recipe map (LCR-specific recipes) are displayed in a category.

// This config does two things:
// First; when a chemical reactor recipe is removed, then the corresponding recipe in the LCR recipe map is removed
// Second; when recipe searches take place in the LCR recipe map, they are constrained to LCR specific recipes ONLY;
//         making it easier to figure out recipe removal code without having to look through two JEI categories.

// Note: this script uses labs functionality explained in 'recipeMapTests.groovy'.

// Example: changing an chemical reactor recipe, without changing inputs (blaze powder)
// If the config is turned off, that will cause a conflict in the LCR recipe map,
// As the newly created recipe will be added to LCR without the original recipe being removed first.
// This can cause issues. (Test running an LCR with this recipe at MV; it will not run, as the orfiginal recipe is still kept for LCR)
// If the config is turned on, then it should function properly.
mods.gregtech.chemical_reactor.changeByInput([metaitem('dustCarbon'), metaitem('dustSulfur')], null)
    .changeEUt { eu -> (int) (eu / 4) } // Change from HV to MV
    .changeDuration { duration -> (int) (duration / 4) } // Change from 10s to 2.5s
    .replaceAndRegister()

// Example: Changing a chemical reactor recipe, with input changes
// If the config is turned off, the original recipe (fiber-reinforced + annealed copper + sulfuric) will be shown as LCR specific.
// If the config is turned on, then only the new recipe will be displayed (in the CR recipe category)
mods.gregtech.chemical_reactor.recipeBuilder()
    .inputs(metaitem('dustCarbon') * 6)
    .fluidInputs(fluid('water') * 1000)
    .outputs(metaitem('board.fiber_reinforced') * 12)
    .EUt(VA[UEV]).duration(1_000)
    .replace().buildAndRegister()

// Example: Searching LCR recipe map (by input)
// If the config is turned off, then a recipe should be found
// If the config is turned on, then no recipe should be found (as it is not LCR specific)
Recipe recipe = mods.gregtech.large_chemical_reactor.find([metaitem('crushedPurifiedChalcopyrite')], [fluid('nitric_acid') * 100])
log.info "[CR Config Tests] Found recipe: ${recipe} (by input)."
log.info 'Should be null if the `scriptingChemReactorImprovements` is turned on, and a valid recipe returned otherwise.'

// Example: Searching LCR recipe map (by output)
// If the config is turned off, then three recipes should be found
// If the config is turned on, then only one should be found (as the other two are not LCR specific)
List<Recipe> recipes = mods.gregtech.large_chemical_reactor.findByOutput(null, [fluid('sulfuric_acid') * 1000])
log.info "[CR Config Tests] Found recipe amount: ${recipes.size()} (by output)."
log.info 'Should be 1 if the `scriptingChemReactorImprovements` is turned on, and 3 otherwise.'
