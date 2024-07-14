package com.nomiceu.nomilabs.mixin.gregtech;

import static com.nomiceu.nomilabs.groovy.LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.util.ItemMeta;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.loaders.recipe.RecyclingRecipes;

/**
 * This mixin allows us to define custom NBT handling in recycling.
 */
@Mixin(value = RecyclingRecipes.class, remap = false)
public class RecyclingRecipesMixin {

    @Inject(method = "cleanInputNBT", at = @At("HEAD"))
    private static void handleCustomNBT(ItemStack input, RecipeBuilder<?> builder, CallbackInfo ci) {
        // Only initiate custom handling if we have nbt conditions
        if (REPLACE_RECYCLING_MANAGER.nbtConditions == null || REPLACE_RECYCLING_MANAGER.nbtConditions.isEmpty())
            return;

        var handling = REPLACE_RECYCLING_MANAGER.nbtConditions.get(new ItemMeta(input));
        if (handling == null) return;

        builder.clearInputs();
        builder.inputNBT(new GTRecipeItemInput(input), handling.getLeft(), handling.getRight());
    }
}
