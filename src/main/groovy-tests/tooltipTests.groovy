import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TooltipHelpers.*
import static com.nomiceu.nomilabs.tooltip.LabsTooltipHelper.*

// (Item) Tooltip Helper, Goes in Post Init.

// You MAY put side only, client, on tooltip scripts to save computation; but it is NOT required.

// Note that tooltips apply to all stacks of that Item and Meta, regardless of NBT Tag.
// If meta is not provided, tooltip only applies to item of meta 0.

// Tooltip operations are all applied sequentially.

// Add a tooltip (Uses Translatable, see `jeiTests.groovy`)
addTooltip(item('minecraft:sand'), translatable('item.material.oreprefix.gemPerfect', 'World'))

// Add multiple tooltips (Uses Translatable, see `jeiTests.groovy`)
// Tooltips are joined with `\n`.
addTooltip(item('minecraft:glass'), [translatableLiteral('Testing'), translatable('nomilabs.subtitle.tick.microverse')])

// Remove all of an item's existing tooltip
clearTooltip(metaitem('pump.lv'))
addTooltip(metaitem('pump.lv'), translatableLiteral('Testing'))

// Remove some of an item's existing tooltip
customHandleTooltip(metaitem('pump.mv')) {
    tryRemove(it, 0, 1) // Remove first two
    tryRemove(it, it.size() - 1, it.size()) // Remove last
}
addTooltip(metaitem('pump.mv'), translatableLiteral('Testing'))

