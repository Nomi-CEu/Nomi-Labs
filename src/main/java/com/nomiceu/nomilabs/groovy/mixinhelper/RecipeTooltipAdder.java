package com.nomiceu.nomilabs.groovy.mixinhelper;

import net.minecraft.util.ResourceLocation;

import com.nomiceu.nomilabs.integration.jei.LabsJEIPlugin;
import com.nomiceu.nomilabs.util.LabsTranslate;

public class RecipeTooltipAdder {

    public static void addTooltips(ResourceLocation rl,
                                   LabsTranslate.Translatable[][] inputTooltip,
                                   LabsTranslate.Translatable[] outputTooltip) {
        if (inputTooltip != null) {
            for (int i = 0; i < inputTooltip.length; i++) {
                var tooltip = inputTooltip[i];
                if (tooltip != null)
                    LabsJEIPlugin.addGroovyRecipeInputTooltip(rl, i, tooltip);
            }
        }
        if (outputTooltip != null)
            LabsJEIPlugin.addGroovyRecipeOutputTooltip(rl, outputTooltip);
    }
}
