import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TooltipHelpers.*

// (Item) Tooltip Helper, Goes in Post Init.

// Note that tooltips apply to all stacks of that Item and Meta, regardless of NBT Tag.
// If meta is not provided, tooltip only applies to item of meta 0.

// Clearing Tooltips (through `clearTooltip`) always is applied before any `addTooltip` calls!

// Add a tooltip (Uses Translatable, see `jeiTests.groovy`)
addTooltip(item('minecraft:sand'), translatable('item.material.oreprefix.gemPerfect', 'World'))

// Add multiple tooltips (Uses Translatable, see `jeiTests.groovy`)
// Tooltips are joined with `\n`.
addTooltip(item('minecraft:glass'), [translatableLiteral('Testing'), translatable('nomilabs.subtitle.tick.microverse')])

// Remove all of an item's existing tooltip
clearTooltip(metaitem('pump.lv'))
addTooltip(metaitem('pump.lv'), translatableLiteral('Testing'))

// Remove some of an item's existing tooltip
clearSomeTooltip(metaitem('pump.mv'), 1)
addTooltip(metaitem('pump.mv'), translatableLiteral('Testing'))

