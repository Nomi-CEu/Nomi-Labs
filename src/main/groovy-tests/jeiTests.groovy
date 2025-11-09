import gregtech.client.utils.TooltipHelper
import net.minecraft.util.text.TextFormatting

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.JEIHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*

// JEI and Translation Helpers. (Goes in Post Init)

// JEI hiding, recipe tooltips, descriptions, etc. can be marked as side only: client to save computation.
// Remove and hide operations SHOULD NOT, as they also remove recipes!

// There are two types of translations: translate and translatable.
// Translate translates the input NOW, whilst translatable translates the input when it is needed.
// All JEI Pages use Translatable, so that the language can be changed on-the-fly.

// Translate: Instantly Translate Something, on Server or Client, with Parameters
// Use TranslateFormat to Translate with Parameters and a GT Format Code wrapped around the string!
// (Note, normal format codes should be included in the lang itself, and thus are not available with `translateFormat`)
// (Note, normal format codes are still available with `format`)
log.info('Translated Hand Framing Tool Side: ' + translate('tooltip.nomilabs.hand_framing_tool.side', format('Hi', TextFormatting.AQUA)))
log.info('Translated Hand Framing Tool Main: ' + translateFormat('tooltip.nomilabs.hand_framing_tool.not_set', TooltipHelper.RAINBOW))

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
log.info('Translatable Object: ' + translatableObj.translate())

/*
 * Recipe Output Tooltips. These are tooltips that appear on CRAFTING TABLE recipes, on a specific registry name.
 *
 * They appear BEFORE `Recipe By <MODID>` Tooltips, and AFTER any item tooltips.
 *
 * You MUST provide a Resource Location of the Recipe Name.
 */

// Add a crafting recipe output tooltip
addRecipeOutputTooltip('minecraft:gold_ingot_from_block',
        translatableLiteral('A Very High Carrot Gold Ingot.').addFormat(TextFormatting.GOLD))

// Use in a crafting shaped/shapeless builder
crafting.shapedBuilder()
    .output(item('minecraft:apple'))
    .matrix('AAA', 'AAA', 'ABA')
    .key('A', item('minecraft:diamond'))
    .key('B', item('minecraft:apple'))
    .setOutputTooltip(translatableLiteral('A Very Low Carot Gold Ingot. Oops, I meant Carrot!').addFormat(TextFormatting.GOLD),
            translatable('tooltip.nomilabs.growth_chamber.description'))
    .register()

/**
 * Recipe Input Tooltips. These are tooltips that appear on CRAFTING TABLE recipes, on a specific registry name and index.
 *
 * They appear AFTER any item tooltips.
 *
 * You MUST provide a Resource Location of the Recipe Name. You MAY provide an index of the input.
 * If you do not provide an index, it will be applied to ALL INPUTS!
 */

// Outside of builder
// Specify an index between 0 and 8! This represents the slot number. It goes from left to right, top to bottom.
// E.g. slot 4 = 2nd row, 2nd column
addRecipeInputTooltip('gregtech:block_decompress_aluminium', 4,
        translatableLiteral("This is consumed!!!").addFormat(TooltipHelper.BLINKING_CYAN))

// Outside of builder, no index (applied to all inputs)
addRecipeInputTooltip('gregtech:block_compress_aluminium',
        translatableLiteral("This is consumed???").addFormat(TooltipHelper.BLINKING_CYAN))

// In a crafting shaped/shapeless builder
crafting.shapelessBuilder()
    .output(item('minecraft:apple'))
    .input(item('minecraft:diamond'), item('minecraft:apple'))
    // No index, applied to all inputs
    .setInputTooltip(translatableLiteral('Is a Gold Ingot. 100% Pure Quality.').addFormat(TextFormatting.GOLD),
            translatable('tooltip.nomilabs.growth_chamber.description'))
    .register()

crafting.shapedBuilder()
    .output(item('minecraft:apple'))
    .matrix('AAA', 'ABA', 'ABA')
    .key('A', item('minecraft:diamond'))
    .key('B', item('minecraft:apple'))
    // Specify an index between 0 and 8! This represents the slot number. It goes from left to right, top to bottom.
    // E.g. slot 1 = 1st row, 2nd column
    .setInputTooltip(1, translatableLiteral('Is a Gold Ingot?? No.').addFormat(TextFormatting.GOLD),
            translatable('tooltip.nomilabs.growth_chamber.description'))
    // Specify an index between 0 and 8! This represents the slot number. It goes from left to right, top to bottom.
    // E.g. slot 4 = 2nd row, 2nd column
    .setInputTooltip(4, translatableLiteral('Is a Gold Ingot? Perhaps.').addFormat(TextFormatting.GOLD),
            translatable('tooltip.nomilabs.growth_chamber.description'))
    .register()

