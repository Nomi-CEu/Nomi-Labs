import net.minecraft.util.text.TextFormatting

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.JEIHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*

/* Description Pages. Each entry is seperated by double new lines. */

// Add a description page for a stack
addDescription(item('minecraft:apple'), format("An Ordinary Apple... Not Poisoned.", TextFormatting.DARK_GREEN), "Eat it!")

// Add a translated description page for a stack
addDescription(item('minecraft:iron_ingot'), translate("tooltip.nomilabs.greenhouse.description"), translate("tooltip.nomilabs.dme_sim_chamber.description"))

/* Recipe Output Tooltips. These are tooltips that appear on recipes that output that stack. */

// Add a recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:golden_apple'), format("An Ordinary Apple... Not Poisoned.", TextFormatting.GREEN), "Eat it!")

// Add a translated recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:iron_ingot'), translate("tooltip.nomilabs.greenhouse.description"), translate("tooltip.nomilabs.dme_sim_chamber.description"))
