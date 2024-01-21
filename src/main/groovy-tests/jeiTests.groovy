import net.minecraft.util.text.TextFormatting

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.JEIHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*

/* Description Pages. Each entry is seperated by double new lines. */

// Add a description page for a stack
addDescription(item('minecraft:apple'), format("An Ordinary Apple... Not Poisoned.", TextFormatting.DARK_GREEN), "Eat it!")

// Add a translated description page for a stack
addDescription(item('minecraft:iron_ingot'), translate("tooltip.nomilabs.greenhouse.description"), translate("tooltip.nomilabs.dme_sim_chamber.description"))

/* Recipe Output Tooltips. These are tooltips that appear on CRAFTING recipes that output that stack. */

// Add a crafting recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:gold_ingot'), format("A Very Low Carrot Gold Ingot.", TextFormatting.GOLD), "Oops! Meant Carat!")

// Add a translated crafting recipe output tooltip for a stack
addRecipeOutputTooltip(item('minecraft:iron_ingot'), translate("tooltip.nomilabs.greenhouse.description"), translate("tooltip.nomilabs.dme_sim_chamber.description"))

// Add a crafting recipe output tooltip for a specific recipe for a stack (Higher Priority than wild)
addRecipeOutputTooltip(item('minecraft:gold_ingot'), resource('minecraft:gold_ingot_from_block'), format("A Very High Carat Gold Ingot.", TextFormatting.GOLD), "Precious!")

// Add a translated crafting recipe output tooltip for a specific recipe for a stack (Higher Priority than wild)
addRecipeOutputTooltip(item('minecraft:iron_ingot'), resource('minecraft:iron_ingot_from_nuggets'), translate("tooltip.nomilabs.universalnavigator.description"), translate("tooltip.nomilabs.greenhouse.description"), translate("tooltip.nomilabs.dme_sim_chamber.description"))
