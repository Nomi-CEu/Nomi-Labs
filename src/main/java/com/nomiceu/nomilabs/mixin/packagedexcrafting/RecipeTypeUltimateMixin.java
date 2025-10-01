package com.nomiceu.nomilabs.mixin.packagedexcrafting;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thelm.packagedexcrafting.recipe.RecipeTypeUltimate;

/**
 * Only allow importing from 9x9.
 */
@Mixin(value = RecipeTypeUltimate.class, remap = false)
public class RecipeTypeUltimateMixin {

    @Unique
    private final List<String> labs$category = Collections.singletonList("extendedcrafting:table_crafting_9x9");

    @Inject(method = "getJEICategories", at = @At("HEAD"), cancellable = true)
    private void getJEICategories(CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(labs$category);
    }
}
