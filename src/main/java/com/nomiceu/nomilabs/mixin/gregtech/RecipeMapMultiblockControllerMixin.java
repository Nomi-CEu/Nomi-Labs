package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.CustomWarningBuilder;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;

@Mixin(value = RecipeMapMultiblockController.class, remap = false)
public abstract class RecipeMapMultiblockControllerMixin extends MultiblockWithDisplayBase {

    @Shadow
    protected MultiblockRecipeLogic recipeMapWorkable;

    /**
     * Mandatory Ignored Constructor
     */
    public RecipeMapMultiblockControllerMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Inject(method = "addWarningText", at = @At(value = "RETURN"))
    private void addMore(List<ITextComponent> textList, CallbackInfo ci) {
        new CustomWarningBuilder(textList, isStructureFormed(), recipeMapWorkable)
                .addOutputFull().addRecipeVoltageHigh().addCleanroomWarnings();
    }
}
