package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleAbstractRecipeLogic;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.MultiMapMultiblockController;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.RecipeMap;

/**
 * Fixes Recipes not being Rechecked when Mode Changed in GUI.
 * <p>
 * Allows Changing Modes with Screwdriver when Machine Active.
 */
@Mixin(value = MultiMapMultiblockController.class, remap = false)
public abstract class MultiMapMultiblockControllerMixin extends RecipeMapMultiblockController {

    /**
     * Mandatory Ignored Constructor
     */
    public MultiMapMultiblockControllerMixin(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap);
    }

    @Redirect(method = "onScrewdriverClick",
              at = @At(value = "INVOKE", target = "Lgregtech/api/capability/impl/MultiblockRecipeLogic;isActive()Z"),
              require = 1)
    private boolean allowModeChangeWhenActive(MultiblockRecipeLogic instance) {
        return false;
    }

    /**
     * Cancel original recipe recheck in on screwdriver click, prevent double recheck
     */
    @Redirect(method = "onScrewdriverClick",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/capability/impl/MultiblockRecipeLogic;forceRecipeRecheck()V"),
              require = 1)
    private void cancelOriginalRecheck(MultiblockRecipeLogic instance) {}

    @Inject(method = "setRecipeMapIndex", at = @At("RETURN"))
    private void forceRecheck(int index, CallbackInfo ci) {
        if (!getWorld().isRemote) {
            // We only want to do this if we aren't running a recipe,
            // Otherwise recipe voiding could happen.
            if (recipeMapWorkable.getProgress() <= 0)
                recipeMapWorkable.forceRecipeRecheck();
            else
                // Remove recipe cache to prevent re-running of curr recipe
                ((AccessibleAbstractRecipeLogic) recipeMapWorkable).labs$clearRecipeCache();
        }
    }
}
