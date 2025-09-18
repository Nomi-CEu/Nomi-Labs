// Import Recipe Search Helpers, used for Chanced Item and Fluid Ingredients

import com.nomiceu.nomilabs.groovy.ChangeRecipeBuilder

import gregtech.api.recipes.RecipeBuilder
import gregtech.api.recipes.RecipeMaps
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher
import gregtech.api.recipes.recipeproperties.CleanroomProperty
import gregtech.api.recipes.recipeproperties.ResearchProperty
import gregtech.api.recipes.recipeproperties.TemperatureProperty

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.GTRecipeHelpers.*
import static gregtech.api.GTValues.*

// Find and Removing GT Recipe Helpers. Goes in Post Init.

// For running this on servers, when using recipe properties (in 2.8.10), you will need to add client-side methods
// via labs config.
// Example for Temperature Property:
// gregtech/api/recipes/recipeproperties/TemperatureProperty@drawInfo@(Lnet/minecraft/client/Minecraft;IIILjava/lang/Object;)V
// All GT Properties, in 2.8.10, require this change.

// Building Test Recipes
mods.gregtech.sifter.recipeBuilder()
    .inputs(metaitem('nomilabs:dustImpureOsmiridium8020'))
    .outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

mods.gregtech.cyclotron.recipeBuilder()
        .inputs(item('gregtech:meta_foil', 64))
        .fluidInputs(fluid('hydrogen') * 1000)
        .outputs(item('nomilabs:meta_foil', 20))
        .EUt(VA[UHV]).duration(600)
        .buildAndRegister()

mods.gregtech.sifter.recipeBuilder()
    .inputs(item('minecraft:stick'))
    .outputs(item('minecraft:apple') * 64)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

mods.gregtech.sifter.recipeBuilder()
    .inputs(item('minecraft:yellow_flower'))
    .outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64)
    .chancedOutput(item('minecraft:apple') * 64, 50, 1)
    .chancedFluidOutput(fluid('fluorine') * 2000, 50, 1)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

mods.gregtech.sifter.recipeBuilder()
    .inputs(metaitem('nomilabs:dustOsmiridium8020'))
    .outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64)
    .chancedOutput(item('minecraft:apple') * 64, 50, 1)
    .chancedFluidOutput(fluid('fluorine') * 2000, 50, 1)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

mods.gregtech.sifter.recipeBuilder()
    .inputs(metaitem('nomilabs:dustPureOsmiridium8020'))
    .outputs(item('minecraft:apple') * 64, item('minecraft:apple') * 64)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

// Find/Remove By Input Extensions (Are Lists of: List<ItemStack> itemInputs, List<FluidStack> fluidInputs)
// mods.gregtech.<RECIPE_MAP>.removeByInput to remove, mods.gregtech.<RECIPE_MAP>.find to find (Returns null if no recipe found)
// Original: [long voltage, Inputs... (see above)] (Matches/Removes any recipe with that input, and that voltage or more, CHECKING AMOUNT)
// ALL FIND/REMOVE BY INPUT EXTENSIONS IGNORE THE AMOUNT!
// Three Extensions:
// [GTRecipeCategory category, Inputs... (see above)] (Matches/Removes any recipe with that input, and that category)
// [Inputs... (see above)] (Matches/Removes any recipe with that input)
// [Predicate<Recipe> predicate, Inputs... (see above)] (Matches/Removes any recipe with that input, and matching that predicate)
mods.gregtech.sifter.removeByInput([item('minecraft:yellow_flower')], null)

// Find/Remove By Output
// Outputs Specification: List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs, List<ChancedItemOutput> chancedItemOutputs, List<ChancedFluidOutput> chancedFluidOutputs
// (You can also exclude the Chanced Items and Fluids, e.g. List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs)
// Chanced Item/Fluid Outputs: chanced(item/fluid, chance, chanceBoost)
// mods.gregtech.<RECIPE_MAP>.removeByOutput to remove, mods.gregtech.<RECIPE_MAP>.findByOutput to find (Returns null if no recipes found)
// ALL FIND/REMOVE BY OUTPUT OPTIONS IGNORE THE AMOUNT!
// Four Options:
// [long voltage, Outputs... (see above)] (Matches/Removes any recipe with that output, and that voltage or more)
// [GTRecipeCategory category, Outputs... (see above)] (Matches/Removes any recipe with that output, and that category)
// [Outputs... (see above)] (Matches/Removes any recipe with that output)
// [Predicate<Recipe> predicate, Outputs... (see above)] (Matches/Removes any recipe with that output, and matching that predicate)
mods.gregtech.sifter.removeByOutput(50, [item('minecraft:apple') * 64, item('minecraft:apple') * 64, item('minecraft:apple') * 64], null, [chanced(item('minecraft:apple') * 64, 50, 1)], [chanced(fluid('fluorine') * 2000, 50, 1)])

// NBT Helpers for Recipe Builder
// inputNBT version with IIngredient
// wildInputNBT (parameter of IIngredient)
mods.gregtech.assembler.recipeBuilder()
    .inputNBT(ore('dyeBlue'), NBTMatcher.ANY, NBTCondition.ANY)
    .inputWildNBT(ore('dyeRed')) // Same as above (Except the OreDict of course)
    .outputs(item('minecraft:apple') * 64)
    .EUt(VA[LV]).duration(30)
    .buildAndRegister()

// Replace Recipes
// A ReplaceByOutput on the recipe builder's recipe map, with the outputs (and other info) specified up to that point, will be conducted.
// Remember that ReplaceByOutput ignores amount!

// Variations: `replace(RecipeMap<?>... otherMaps), `replace(Predicate<Recipe> condition, RecipeMap<?>... otherMaps)`,
// `replaceInCategory(RecipeMap<?>... otherMaps)`, `replaceWithVoltage(RecipeMap<?>... otherMaps)`,
// `replaceWithExactVoltage(RecipeMap<?>... otherMaps)`

// Other Maps also have the recipe removed from them. Useful when a recipe is auto-registered to multiple recipe maps (e.g. Chemical Reactor)
// Note that the current recipe map will ALSO have the recipe removed from it, no matter what variation is used.

// It is also important to note that the removal is conducted AS SOON AS the function is called, NOT on build.
// This behaviour may be useful in ensuring that 'replacements'/'removals' are only conducted once when multiple recipes are being
// added to replace one (e.g. adding assembler recipes for three different rubbers, or all types of capacitors, etc.)

// Example: Changing the Recipe for Multi-Layer Fiber Reinforced Circuit Board (with Recycling)
mods.gregtech.chemical_reactor.recipeBuilder()
    .inputs(metaitem('board.wetware') * 6, metaitem('circuit_assembler.iv') * 5)
    .fluidInputs(fluid('rhodium') * 144)
    .outputs(metaitem('board.multilayer.fiber_reinforced') * 12)
    .EUt(VA[OpV]).duration(1_000_000)
    .replace(RecipeMaps.LARGE_CHEMICAL_RECIPES) // Chem Reactor recipes are also registered to Large Chemical Reactor's Recipe Map
    .buildAndRegister()

// Change Recipes
// NOTE THAT PROPERTIES ARE NOT TRANSFERRED! (CLEANROOM, ASSEMBLY RESEARCH, ETC.)
// THIS IS BECAUSE BUILDERS CAN APPLY THESE THEMSELVES (e.g. Primitive), CAUSING DUPE PROPERTIES!

// Can use Any of the Find by Input or Output Methods (`changeByInput` & `changeByOutput`)
// Simply the normal find method parameters, and returning a consumer of a ChangeRecipeBuilder (for Input)
// and a consumer of a Stream of ChangeRecipeBuilders (for Output)

// If error occurs (could not find recipes), a dummy recipe builder is returned for input, and an empty stream is returned for output.
// Can also call `changeAllRecipes` to change all recipes, filtering by predicate, category, both, or none.

// Note: Calling `replaceAndRegister` removes the original recipe **AND** adds the new one.
// calling `buildAndRegister` just adds the new one.

// When copying properties, it is important to check that the property is 'safe', e.g. its `drawInfo` method has
// @SideOnly(Side.CLIENT) annotation. Else, this method must have SideOnly added (e.g. via PR + Nomi Labs config)

// IMPORTANT: changeEach<TYPE> and changeAll<TYPE> functions use the list from the ORIGINAL RECIPE; hence changes DO NOT stack.
// change<TYPE> and remove<TYPE> functions use the list from the CURRENT BUILDER, hence changes DO stack.
// Also, since remove<TYPE> uses indices from start of operation, it is best to condense all remove operations to one call,
// to remove changing indices due to removals from middle of lists.

// Example 1: Changing All PBF recipes to be half duration
// Using Change All Recipes
mods.gregtech.primitive_blast_furnace.changeAllRecipes()
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeDuration(duration -> (int) (duration / 2))
                .replaceAndRegister()
    }

// Example 2: Making All Electronic Circuit Recipes Output Double and require an Apple, whilst Changing (Adding) Recycling
mods.gregtech.circuit_assembler.changeByOutput([metaitem('circuit.electronic') * 2], null) // Excluding Chanced Output Specification
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeEachOutput { stack ->
            stack.count *= 2
            return stack
        }.builder { RecipeBuilder recipe ->
            recipe.inputs(item('minecraft:apple'))
                    .changeRecycling()
        }.replaceAndRegister()
    }

// Example 3: Changing a Macerator Recipe to Double the Chance of the Final Chanced Output
mods.gregtech.macerator.changeByInput([metaitem('plant_ball') * 2], null)
    .changeChancedOutput(-1) { // -1 = Last (we can use negative indices to count from end of list)
        chanced(it.ingredient, it.chance * 2, it.chanceBoost)
    }
    .replaceAndRegister()

// Example 4: Changing the Circuit Meta of a Recipe
mods.gregtech.assembler.changeByOutput([item('minecraft:iron_bars') * 4], null)
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeCircuitMeta { meta -> meta * 2 }
            .builder { RecipeBuilder recipe ->
                recipe.notConsumable(fluid('water') * 1000)
            }
            .replaceAndRegister()
    }

// Example 5: Adding Alternative Chemical Reactor Recipes
// Alternative = `buildAndRegister` not `replaceAndRegister`
mods.gregtech.chemical_reactor.changeByOutput(null, [fluid('polytetrafluoroethylene')]) // Change By Output ignores Amount
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeCircuitMeta { meta -> meta + 10 }
            .builder { RecipeBuilder recipe ->
                recipe.fluidInputs(fluid('rhodium') * 144)
            }
            .changeDuration { duration -> (int) (duration / 2) }
            .changeEachFluidOutput { output ->
                output.amount *= 2
                return output
            }
            .buildAndRegister()
    }

// Example 6: Doubling the Output of the Crystal Processor Assembly Circuit Recipes
mods.gregtech.circuit_assembler.changeByOutput([metaitem('circuit.crystal_assembly')], null)
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeEachOutput { stack ->
            stack.count *= 2
            return stack
        }.copyProperties(CleanroomProperty.instance) // Copy the CLEANROOM Property!
        .replaceAndRegister()
    }

// Example 7: Adding an Alternative Blast Furnace Recipe for Red Steel, with Double Output but Double Temperature
// Alternative = `buildAndRegister` not `replaceAndRegister`
mods.gregtech.electric_blast_furnace.changeByOutput([metaitem('ingotRedSteel')], null)
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeEachOutput { stack ->
                stack.count *= 2
                return stack
            }.changeCircuitMeta { meta -> meta + 10 }
            /*
             * It is important that you, somehow, make a copy of the input property, and not modify the property itself!
             * Otherwise, reloading may not work correctly, and can cause modifications to be applied on top of each other!
             *
             * Note that the `changeProperty` method does not allow for property string keys, you must input an object.
             *
             * Note that this is not needed for the TemperatureProperty, as it simply stores an integer.
             * (Also note that you would do `copyProperties(TemperatureProperty.instance)` to copy the temperature property.
             */
            .changeProperty(TemperatureProperty.instance, temp -> temp * 2)
            .buildAndRegister()
    }

// Example 8: Doubling the Output of the Fusion MK I Controller Assembly Line Recipe (With Recycling)
mods.gregtech.assembly_line.changeByOutput([metaitem('fusion_reactor.luv')], null)
    .forEach { ChangeRecipeBuilder builder ->
        builder.changeEachOutput { stack ->
                stack.count *= 2
                return stack
            }.builder { RecipeBuilder recipe -> recipe.changeRecycling() }
            .copyProperties(ResearchProperty.instance)
            .replaceAndRegister()
    }

// Example 9: Removing Last Two Inputs of Good Integrated Circuit Recipe, Replace with Apple + Log
mods.gregtech.circuit_assembler.changeByOutput([metaitem('circuit.good_integrated')], null) // Ignores count, so even though recipe outputs 2, we can stick with 1
    .forEach { ChangeRecipeBuilder builder ->
        builder.removeInputs(-2, -1) // off list state at beginning of operation, so we use -1 and -2. Indices can be in any order. Note: Circuits & Non Consumables are included in the list.
            .builder { RecipeBuilder recipe ->
                recipe.inputs(item('minecraft:apple'), item('minecraft:log'))
            }.replaceAndRegister()
    }

// See {@link com.nomiceu.nomilabs.groovy.ChangeRecipeBuilder} for more functions!
