package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.crafttweaker.recipe.CTRecipe;

/**
 * Part of the handling for labs' scripting remove actions.
 */
@Mixin(value = CTRecipe.class, remap = false)
public class CTRecipeMixin {

    @Shadow
    @Final
    private RecipeMap<?> recipeMap;

    @Shadow
    @Final
    private Recipe backingRecipe;

    @Inject(method = "remove", at = @At("RETURN"))
    private void callScriptRemoveAction(CallbackInfoReturnable<Boolean> cir) {
        Consumer<Pair<AccessibleRecipeMap.ScriptType, Recipe>> scriptingAction = ((AccessibleRecipeMap) recipeMap)
                .labs$getScriptRemoveAction();
        if (scriptingAction != null) {
            scriptingAction.accept(Pair.of(AccessibleRecipeMap.ScriptType.CT, backingRecipe));
        }
    }
}
