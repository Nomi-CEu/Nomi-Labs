package com.nomiceu.nomilabs.mixin.ae2fc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.glodblock.github.integration.jei.FluidPatternEncoderRecipeTransferHandler;
import com.glodblock.github.integration.jei.RecipeTransferBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.integration.ae2fc.DummyInclNonConsumableSettings;
import com.nomiceu.nomilabs.integration.ae2fc.RecipeTransferBuilderState;

import mezz.jei.api.gui.IRecipeLayout;

/**
 * Disables Incl Non Consume for Fluid Pattern Encoder.
 */
@Mixin(value = FluidPatternEncoderRecipeTransferHandler.class, remap = false)
public class FluidPatternEncoderRTHMixin {

    @Unique
    private static final DummyInclNonConsumableSettings labs$dummy = new DummyInclNonConsumableSettings();

    @WrapOperation(method = "transferRecipe(Lcom/glodblock/github/client/container/ContainerFluidPatternEncoder;Lmezz/jei/api/gui/IRecipeLayout;Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
                   at = @At(value = "NEW",
                            target = "(Lmezz/jei/api/gui/IRecipeLayout;)Lcom/glodblock/github/integration/jei/RecipeTransferBuilder;"),
                   require = 1)
    private RecipeTransferBuilder setContainer(IRecipeLayout recipe, Operation<RecipeTransferBuilder> original) {
        RecipeTransferBuilderState.container = labs$dummy;
        RecipeTransferBuilder builder = original.call(recipe);
        RecipeTransferBuilderState.container = null;

        return builder;
    }
}
