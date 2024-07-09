import gregtech.client.utils.TooltipHelper
import net.minecraft.util.text.TextFormatting

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.JEIHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*

// JEI and Translation Helpers. (Goes in Post Init)

// There are two types of translations: translate and translatable.
// Translate translates the input NOW, whilst translatable translates the input when it is needed.
// All JEI Pages use Translatable, so that the language can be changed on-the-fly.

// Translate: Instantly Translate Something, on Server or Client, with Parameters
// Use TranslateFormat to Translate with Parameters and a GT Format Code wrapped around the string!
// (Note, normal format codes should be included in the lang itself, and thus are not available with `translateFormat`)
// (Note, normal format codes are still available with `format`)
println('Translated Hand Framing Tool Side: ' + translate('tooltip.nomilabs.hand_framing_tool.side', format('Hi', TextFormatting.AQUA)))
println('Translated Hand Framing Tool Main: ' + translateFormat('tooltip.nomilabs.hand_framing_tool.not_set', TooltipHelper.RAINBOW))

// Translatable: Save something to be translated later
var translatableObj = translatable('tooltip.nomilabs.excitationcoil.description')

// You can add formatting (TextFormatting, GTFormatCode, or Format)
translatableObj.addFormat(TextFormatting.AQUA)

// You can append other translatable objects
// Note that appended objects will be concatenated with no seperator, and in this order:
// If translatable1 has translated2 and translated3 appended in that order, and translated2 has p1 and p2 appended in that order:
// transtable1 + translated2 + p1 + p2 + translated3
translatableObj.append(translatable('tooltip.nomilabs.hand_framing_tool.front', format('Hello World!', TextFormatting.RED)))

// Very flexible, you can even append literal strings
translatableObj.append(translatableLiteral('Hello World!'))

// Call `toString` or `translate` to retrieve the translated and concatenated string
println('Translatable Object: ' + translatableObj.translate())

/* JEI Pages. Each Method requires Translatable Objects. */

/* Description Pages. Each entry is seperated by double new lines. */

// Add a description page for a stack
addDescription(item('minecraft:apple'), translatableLiteral('An Ordinary Apple... Not Poisoned.').addFormat(TextFormatting.DARK_GREEN), translatableLiteral('Eat it!'))

// Add a translated description page for a stack
addDescription(item('minecraft:iron_ingot'), translatable('tooltip.nomilabs.greenhouse.description'), translatable('tooltip.nomilabs.dme_sim_chamber.description'))

/* Recipe Output Tooltips. These are tooltips that appear on CRAFTING recipes that output that stack. */

// Add a crafting recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:gold_ingot'), translatableLiteral('A Very Low Carrot Gold Ingot.').addFormat(TextFormatting.GOLD))

// Add a translated crafting recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:iron_ingot'), translatable('tooltip.nomilabs.greenhouse.description'), translatable('tooltip.nomilabs.dme_sim_chamber.description'))

// Add a crafting recipe output tooltip for a specific recipe for a stack (Higher Priority than wild)
addRecipeOutputTooltip(item('minecraft:gold_ingot'), resource('minecraft:gold_ingot_from_block'), translatableLiteral('A Very High Carrot Gold Ingot.').addFormat(TextFormatting.GOLD))

// Add a translated crafting recipe output tooltip for a specific recipe for a stack (Higher Priority than wild)
addRecipeOutputTooltip(item('minecraft:iron_ingot'), resource('minecraft:iron_ingot_from_nuggets'), translatable('tooltip.nomilabs.universalnavigator.description'), translatable('tooltip.nomilabs.greenhouse.description'), translatable('tooltip.nomilabs.dme_sim_chamber.description'))
