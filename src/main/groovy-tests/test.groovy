import static com.nomiceu.nomilabs.groovy.GroovyHelpers.RecipeHelpers.*

replaceRecipeShaped("casing_assembly_control", item('gregtech:multiblock_casing', 3) * 1, [
        [ore('circuitLuv'), metaitem('plate.high_power_integrated_circuit'), ore('circuitLuv')],
        [metaitem('sensor.iv'), metaitem('frameIridium'), metaitem('emitter.iv')],
        [ore('circuitLuv'), metaitem('electric.motor.iv'), ore('circuitLuv')]
])

reloadRecyclingRecipes()