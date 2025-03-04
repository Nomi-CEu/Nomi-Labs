package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.IRefreshBeforeConsumption;

import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;

/**
 * Part of <a href="https://github.com/GregTechCEu/GregTech/pull/2646">GTCEu #2646</a> impl.
 */
@Mixin(value = MultiblockRecipeLogic.class, remap = false)
public abstract class MultiblockRecipeLogicMixin extends AbstractRecipeLogic {

    /**
     * Mandatory Ignored Constructor
     */
    public MultiblockRecipeLogicMixin(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
    }

    @Inject(method = "prepareRecipeDistinct", at = @At("HEAD"))
    private void refresh(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        ((IRefreshBeforeConsumption) metaTileEntity).labs$refreshBeforeConsumption();
    }

    @Override
    public boolean prepareRecipe(Recipe recipe) {
        ((IRefreshBeforeConsumption) metaTileEntity).labs$refreshBeforeConsumption();
        return super.prepareRecipe(recipe);
    }

    /**
     * Remove Muffler Logic
     */
    @Redirect(
            method = "completeRecipe()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lgregtech/api/capability/impl/MultiblockRecipeLogic;performMufflerOperations()V"
            )
    )
    private void removeMufflerMechanic(MultiblockRecipeLogic instance) {
        // Do nothing
    }
}


