package com.nomiceu.nomilabs.mixin.ae2fc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.glodblock.github.client.container.ContainerUltimateEncoder;
import com.glodblock.github.integration.jei.RecipeTransferBuilder;
import com.glodblock.github.integration.jei.UltimateEncoderRecipeTransferHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.integration.ae2fc.RecipeTransferBuilderState;

import mezz.jei.api.gui.IRecipeLayout;

/**
 * Allows detection of container for RecipeTransferBuilder.
 */
@Mixin(value = UltimateEncoderRecipeTransferHandler.class, remap = false)
public class UltimateEncoderRTHMixin {

    @WrapOperation(method = "transferRecipe(Lcom/glodblock/github/client/container/ContainerUltimateEncoder;Lmezz/jei/api/gui/IRecipeLayout;Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
                   at = @At(value = "NEW",
                            target = "(Lmezz/jei/api/gui/IRecipeLayout;)Lcom/glodblock/github/integration/jei/RecipeTransferBuilder;"),
                   require = 1)
    private RecipeTransferBuilder setContainer(IRecipeLayout recipe, Operation<RecipeTransferBuilder> original,
                                               @Local(argsOnly = true) ContainerUltimateEncoder container) {
        RecipeTransferBuilderState.container = (InclNonConsumeSettable) container;
        RecipeTransferBuilder builder = original.call(recipe);
        RecipeTransferBuilderState.container = null;

        return builder;
    }
}
