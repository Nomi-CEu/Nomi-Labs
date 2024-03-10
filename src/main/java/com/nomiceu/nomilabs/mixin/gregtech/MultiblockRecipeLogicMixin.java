package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleEnergyContainerList;
import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiblockRecipeLogic.class, remap = false)
public abstract class MultiblockRecipeLogicMixin extends AbstractRecipeLogic {
    @Shadow public abstract IEnergyContainer getEnergyContainer();

    /**
     * Default Ignored Constructor
     */
    public MultiblockRecipeLogicMixin(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
    }

    @Inject(method = "getMaxVoltage", at = @At("HEAD"), cancellable = true)
    public void getCorrectMaxVoltage(CallbackInfoReturnable<Long> cir) {
        if (!consumesEnergy()) return;

        IEnergyContainer energyContainer = getEnergyContainer();
        if (energyContainer instanceof EnergyContainerList energyList) {
            long highestVoltage = energyList.getHighestInputVoltage();
            if (energyList.getNumHighestInputContainers() > 1) {
                // allow tier + 1 if there are multiple hatches present at the highest tier
                int tier = GTUtility.getTierByVoltage(highestVoltage);
                cir.setReturnValue(GTValues.V[Math.min(tier + 1, GTValues.MAX)]);
            } else {
                var amp = ((AccessibleEnergyContainerList) energyList).getTotalHighestInputAmperage();
                if (amp >= 4) highestVoltage = highestVoltage * 4;
                cir.setReturnValue(highestVoltage);
            }
        } else {
            var amp = energyContainer.getInputAmperage();
            var voltage = energyContainer.getInputVoltage();
            if (amp >= 4) voltage = voltage * 4;
            cir.setReturnValue(voltage);
        }
    }
}
