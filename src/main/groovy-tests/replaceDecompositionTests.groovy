// Imports all Static Functions from the decomposition section of the groovy helpers

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.ReplaceDecompositionHelpers.*

// Replace Decomposition (changes chemical formula and decomp recipe)
// This only changes DECOMPOSITION recipes, not the recipes that make it, such as ABS, Mixer, Blast Furnace, etc.
// Decomp recipes are either centrifuge or electrolyzer recipes.
// You cannot enable them, if the material already has them disabled.
// Chemical Formula Changes are not recursive.
// However, changes will apply if you first change the inner materials before the outer.

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

// Replace/Add Decomposition by Material
replaceDecomposition(material('pyrite'), [materialstack('nomilabs:pulsating_iron') * 5, fluid('fluorine') * 2000, metaitem('ingotNaquadahAlloy') * 10])

// Replace/Add Decomposition by Material Stack
replaceDecomposition(materialstack('redstone'), [material('nomilabs:lead_metasilicate') * 2, materialstack('pyrite') * 6, materialstack('ruby') * 2, materialstack('mercury') * 4])

// Replace/Add Decomposition by Item Stack
replaceDecomposition(metaitem('ingotEnrichedNaquadahTriniumEuropiumDuranide'), [metaitem('ingotTrinaquadalloy')])

// Replace/Add Decomposition by Fluid Stack
replaceDecomposition(fluid('hexafluorosilicic_acid'), [metaitem('item_collector.hv'), item('minecraft:bucket')])

// Remove Decomposition by Material
removeDecomposition(material('osmiridium'))

// Remove Decomposition by Material Stack
removeDecomposition(materialstack('red_alloy') * 10)

// Remove Decomposition by Item Stack
removeDecomposition(metaitem('ingotTrinaquadalloy'))

// Remove Decomposition by Fluid Stack
removeDecomposition(fluid('dioxygen_difluoride'))
