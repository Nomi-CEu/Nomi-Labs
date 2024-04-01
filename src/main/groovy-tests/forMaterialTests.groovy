import net.minecraftforge.fluids.Fluid

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.MaterialHelpers.*

// Material Removers and Loopers. (Goes in Post Init)

// Actions on Items include all material items and blocks, and buckets containing any material fluid.
// Actions on Fluids include liquids, gasses, solids, and addon fluids (such as molten fluids, added by GCYM)

println("ITEMS:")

// Do an Action for each Material's Item (GregTech's)
forMaterialItem(material('tungsten_steel'), (stack) -> println(getFormattingForStack(stack, "Tungsten Steel")))

// Do an Action for each Material's Item (Nomi Labs')
forMaterialItem(material('nomilabs:dulysite'), (stack) -> println(getFormattingForStack(stack, "Dulysite")))

println("FLUIDS:")

// Do an Action for each Material's Fluid (GregTech's)
forMaterialFluid(material('tungsten_steel'), (fluid) -> println(getFormattingForFluid(fluid, "Tungsten Steel")))

// Do an Action for each Material's Fluid (Nomi Labs')
forMaterialFluid(material('nomilabs:dulysite'), (fluid) -> println(getFormattingForFluid(fluid, "Dulysite")))

println("COMBINED:")

// Do an Action for each Material's Items and Fluids (GregTech's)
forMaterial(material('tungsten_steel'), (stack) -> println(getFormattingForStack(stack, "Tungsten Steel")), (fluid) -> println(getFormattingForFluid(fluid, "Tungsten Steel")))

// Do an Action for each Material's Items and Fluids (Nomi Labs')
forMaterial(material('nomilabs:dulysite'), (stack) -> println(getFormattingForStack(stack, "Dulysite")), (fluid) -> println(getFormattingForFluid(fluid, "Dulysite")))

static String getFormattingForStack(ItemStack stack, String name) {
    if (!stack.hasTagCompound()) return "${name}: Item: ${stack.getItem().getRegistryName()} at ${stack.getMetadata()} with no tag."

    return "${name}: Item: ${stack.getItem().getRegistryName()} at ${stack.getMetadata()} with tag ${stack.getTagCompound()}."
}

static String getFormattingForFluid(Fluid fluid, String name) {
    return "${name}: Fluid: ${fluid.getName()}"
}

// JEI Shortforms
// Hide All Material Items and Fluids
hideMaterial(material('nomilabs:taranium'))

// Remove and Hide All Material Items and Fluids (Removes crafting recipes outputting material items)
removeAndHideMaterial(material('titanium'))

// Same as above, but... yeet. Same as Groovy's
yeetMaterial(material('gcym:watertight_steel'))
