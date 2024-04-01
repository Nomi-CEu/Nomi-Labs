// Imports Helpers (Only Needed to use Groovy Helper Calls,
// useful when you only have an ItemStack or FluidStack of a Material
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.ChangeCompositionHelpers.*

// Imports Mixer Specification, needed to specify Mixer Specification if specifying how to change Mixer
import static com.nomiceu.nomilabs.groovy.CompositionBuilder.MixerSpecification

// Replace Composition (changes specified). Goes in Post Init.

// THESE DO NOT APPLY UNTIL AFTER ALL POST INIT SCRIPTS ARE RUN!

// Decomp recipes are either centrifuge or electrolyzer recipes, and require the material not to have the DISABLE_DECOMPOSITION flag, and either have a dust or fluid.
// You cannot enable them, if the material already has them disabled.
// Chemical Formula Changes are not recursive.
// However, changes will apply if you first change the inner materials before the outer.
// ABS Recipe requires the material to have a Molten Fluid or a Liquid, and Alloy Blast and Blast Properties, and not have the Disable ABS Recipe Flag.
// Mixer Recipe requires the material to have an existing mixer recipe, and have a Dust Property.

// You can get all material components of a material by holding an ItemStack of that material, then doing /gs hand!

// Having the target material be a Material Stack changes nothing.
// Having the component list be an item stack will grab the material, then use the stack's amount as the material amount.
// Having the component list be an fluid stack will grab the material, then use the fluid amount / 1000.
// You cannot have fractional components.
// You can have materials with only liquid forms (fluorine, mercury, etc.), as both inputs and outputs.

// Don't have recursive components! This will throw a Stack Overflow Error.

// How to specify a Material Stack
println("Material Stack of 1 Pyrite | " + materialstack('pyrite')) // Material Stack of 1 Pyrite
println("Material Stack of 6 Pyrite | " + materialstack('pyrite') * 6) // Material Stack of 6 Pyrite

println("Material Stack of 1 Pyrite | " + material('pyrite') * 1) // Material Stack of 1 Pyrite
println("Material Stack of 6 Pyrite | " + material('pyrite') * 6) // Material Stack of 6 Pyrite

// How not to specify a Material Stack
println("Material: Pyrite | " + material('pyrite')) // MATERIAL, not Material Stack! This will throw an error!

// Remove Decomposition by Material

// Call Change Composition On a FluidStack or ItemStack or MaterialStack or Material, with Groovy Helper Call
// Change Composition Selectively
changeComposition(material('osmiridium'))
        // Remove Components
        .removeComponents()
        // Remove Decomposition (No Components, so is removed)
        .changeDecompositionRecipes()
        // Remove Mixer (No Components, so is removed)
        .changeMixer()
        // Save and Apply Change **IMPORTANT**
        .change()

changeComposition(material('osmiridium'))
        // Set Components, using a mixture of MaterialStacks, ItemStacks and FluidStacks
        // This is 4 fluorine, not 4000, because fluids are divided by 1000
        .setComponents([fluid('fluorine') * 4000, materialstack('rhodium'), materialstack('nomilabs:naquadah_oxide') * 4, materialstack('magnesium') * 3, metaitem('dustCarbon') * 2])
        // Change ABS Recipe with new specified components
        .changeABS()
        // Change Chemical Formula (Also Saves the Change, so GS Hand shows new components instead of original)
        .changeChemicalFormula()
        // Save and Apply Change **IMPORTANT**
        .change()

// Call Change Composition on a Material Object
material('ruthenium_trinium_americium_neutronate')
        // Start the builder
        .changeComposition()
        // Set Components, using a mixture of MaterialStacks, ItemStacks and FluidStacks
        .setComponents([materialstack('pyrite') * 4, materialstack('rhodium') * 2, materialstack('nomilabs:naquadah_oxide'), materialstack('magnesium'), materialstack('carbon')])
        // Change ABS Recipes
        .changeABS()
        // Change Chemical Formula (Also Saves the Change, so GS Hand shows new components instead of original)
        .changeChemicalFormula()
        // Change Decomp Recipes
        .changeDecompositionRecipes()
        // Change Mixer, using all mixer defaults (see below for description)
        .changeMixer()
        // Save and Apply Change **IMPORTANT**
        .change()

material('rhodium_plated_palladium')
        // Start the builder
        .changeComposition()
        // Set Components, using a mixture of MaterialStacks, ItemStacks and FluidStacks
        .setComponents([materialstack('pyrite') * 4, materialstack('rhodium') * 2, materialstack('nomilabs:naquadah_oxide'), materialstack('magnesium'), materialstack('carbon')])
        // Change Chemical Formula (Also Saves the Change, so GS Hand shows new components instead of original)
        .changeChemicalFormula()
        // Change Decomp Recipes
        .changeDecompositionRecipes()
        // Override All, Some or None Mixer Specifications
        // If none, can provide no params
        // Call new MixerSpecification() to start the builder
        // Builder Params and Defaults:
        // EUt: How Much EU per Tick the Mixer Recipe uses. Default to EUt in original recipe.
        // Output Amount: How much the Mixer Recipe Outputs. Defaults to Amount of Components (E.g. If 5 Pyrite and 2 Iron, outputs 7)
        // Circuit: The circuit of the recipe, use 0 for none. Defaults to circuit in original mixer recipe.
        .changeMixer(new MixerSpecification().overrideEUt(200).overrideOutputAmount(1).overrideDuration(250).overrideCircuit(0))
        // Change ABS Recipes
        .changeABS()
        .change()

// Example of 'Recursive' Chemical Formula Change
// Redstone has Ruby as a Component
material('ruby')
        .changeComposition()
        // Originally 1 Chrome, 2 Aluminium, 3 Oxygen
        .setComponents([materialstack('chrome') * 2, materialstack('aluminium') * 2, materialstack('oxygen') * 6])
        .changeChemicalFormula() // Must call this, so the chemical formula is reloaded, and so that changes are saved
        .change()

material('redstone')
        .changeComposition()
        // Marks this as a 'reload', meaning that components are the original ones from GT or the addon
        .reloadComponents()
        .changeChemicalFormula() // Must call this, so the chemical formula is reloaded
        .change()