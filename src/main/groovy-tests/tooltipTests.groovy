import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TooltipHelpers.*

// (Item) Tooltip Helper, Goes in Post Init.

// Note that tooltips apply to all stacks of that Item and Meta, regardless of NBT Tag.
// If meta is not provided, tooltip only applies to item of meta 0.

// Add a tooltip (Uses Translatable, see `jeiTests.groovy`)
addTooltip(item('minecraft:sand'), translatable('item.material.oreprefix.gemPerfect', 'World'))

// Add multiple tooltips (Uses Translatable, see `jeiTests.groovy`)
// Tooltips are joined with `\n`.
addTooltip(item('minecraft:glass'), [translatableLiteral('Testing'), translatable('nomilabs.subtitle.tick.microverse')])

