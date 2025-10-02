package com.nomiceu.nomilabs.mixin.groovyscript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.compat.mods.jei.Ingredient;
import com.nomiceu.nomilabs.integration.jei.LabsJEIPlugin;

import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;

/**
 * Apply Ignore NBT Item Removals
 */
@Mixin(value = Ingredient.class, remap = false)
public class IngredientMixin {

    @Shadow
    @Final
    private Map<IIngredientType<?>, List<Object>> hiddenIngredients;

    @Inject(method = "applyChanges", at = @At("HEAD"))
    private void applyIgnoreNBTRemovals(IIngredientRegistry ingredientRegistry, CallbackInfo ci) {
        var allIng = ingredientRegistry.getAllIngredients(VanillaTypes.ITEM);
        var list = hiddenIngredients.computeIfAbsent(VanillaTypes.ITEM, k -> new ArrayList<>());

        for (var entry : LabsJEIPlugin.getIgnoreNbtHide()) {
            allIng.stream()
                    .filter((stack) -> entry.getKey().getItem() == stack.getItem() &&
                            entry.getKey().getMetadata() == stack.getMetadata() &&
                            entry.getValue().apply(stack.getTagCompound()))
                    .forEach(list::add);
        }
    }
}
