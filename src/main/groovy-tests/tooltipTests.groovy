import static com.nomiceu.nomilabs.util.LabsTranslate.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TooltipHelpers.*

// (Item) Tooltip Helper, Goes in Post Init.

// Note that tooltips apply to all stacks of that Item/ResourceLocation, regardless of meta or NBT Tag.

// Add a tooltip based on ResourceLocation (Uses Translatable, see `jeiTests.groovy`)
addTooltip(resource('minecraft:dirt'), translatableLiteral('Hello World!'))

// Add a tooltip based on Item/ItemStack (Uses Translatable, see `jeiTests.groovy`)
addTooltip(item('minecraft:sand'), translatable('item.material.oreprefix.gemPerfect', 'World'))

// Add multiple tooltips based on ResourceLocation/Item/ItemStack (Uses Translatable, see `jeiTests.groovy`)
// Tooltips are joined with `\n`.
addTooltip(item('minecraft:glass'), [translatableLiteral('Testing'), translatable('nomilabs.subtitle.tick.microverse')])

