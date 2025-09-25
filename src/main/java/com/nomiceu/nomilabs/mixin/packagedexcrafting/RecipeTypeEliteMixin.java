package com.nomiceu.nomilabs.mixin.packagedexcrafting;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thelm.packagedexcrafting.recipe.RecipeTypeElite;

/**
 * Only allow importing from 7x7.
 */
@Mixin(value = RecipeTypeElite.class, remap = false)
public class RecipeTypeEliteMixin {

    @Unique
    private final List<String> labs$category = Collections.singletonList("extendedcrafting:table_crafting_7x7");

    @Inject(method = "getJEICategories", at = @At("HEAD"), cancellable = true)
    public void getJEICategories(CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(labs$category);
    }
}
