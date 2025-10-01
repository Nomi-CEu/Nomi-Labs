package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.function.BooleanSupplier;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.gui.GuiTextures;
import gregtech.api.recipes.Recipe;
import gregtech.integration.RecipeCompatUtil;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import gregtech.integration.jei.utils.AdvancedRecipeWrapper;
import gregtech.integration.jei.utils.JeiButton;

/**
 * If the recipe is from CT or GS, shows an info logo, saying that the recipe is from CT/GS,
 * instead of showing the button to copy a script to remove the recipe.
 */
@Mixin(value = GTRecipeWrapper.class, remap = false)
public abstract class GTRecipeWrapperMixin extends AdvancedRecipeWrapper {

    @Shadow
    @Final
    private Recipe recipe;

    @Inject(method = "initExtras", at = @At("TAIL"))
    private void showIsCustomRecipe(CallbackInfo ci) {
        if (!RecipeCompatUtil.isTweakerLoaded()) return;

        BooleanSupplier creativePlayerPredicate = () -> Minecraft.getMinecraft().player != null &&
                Minecraft.getMinecraft().player.isCreative();
        BooleanSupplier creativeTweaker = () -> creativePlayerPredicate.getAsBoolean() &&
                (recipe.getIsCTRecipe() || recipe.isGroovyRecipe());
        BooleanSupplier creativeDefault = () -> creativePlayerPredicate.getAsBoolean() && !recipe.getIsCTRecipe() &&
                !recipe.isGroovyRecipe();

        if (buttons.isEmpty()) return;
        // Get Newest Button, which should be the default button
        buttons.get(buttons.size() - 1).setActiveSupplier(creativeDefault);

        buttons.add(new JeiButton(166, 2, 10, 10)
                .setTextures(GuiTextures.INFO_ICON)
                .setTooltipBuilder(lines -> lines.add(recipe.isGroovyRecipe() ?
                        LabsTranslate.translate(
                                "nomilabs.gui.recipes.tooltip.gs_recipe") :
                        LabsTranslate.translate(
                                "nomilabs.gui.recipes.tooltip.ct_recipe")))
                .setClickAction((mc, x, y, button) -> false)
                .setActiveSupplier(creativeTweaker));
    }
}
