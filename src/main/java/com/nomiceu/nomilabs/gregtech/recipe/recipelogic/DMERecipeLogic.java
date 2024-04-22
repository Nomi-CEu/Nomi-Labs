package com.nomiceu.nomilabs.gregtech.recipe.recipelogic;

import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.MetaTileEntityDMESimChamber;
import com.nomiceu.nomilabs.gregtech.recipe.DMEDataProperty;
import com.nomiceu.nomilabs.mixin.deepmoblearning.DataModelHelperAccessor;

import gregicality.multiblocks.api.capability.impl.GCYMMultiblockRecipeLogic;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.GTUtility;
import mustapelto.deepmoblearning.common.util.DataModelHelper;

public class DMERecipeLogic extends GCYMMultiblockRecipeLogic {

    public DMERecipeLogic(MetaTileEntityDMESimChamber tileEntity) {
        super(tileEntity);
    }

    @Override
    protected boolean setupAndConsumeRecipeInputs(@NotNull Recipe recipe,
                                                  @NotNull IItemHandlerModifiable importInventory,
                                                  @NotNull IMultipleTankHandler importFluids) {
        if (!super.setupAndConsumeRecipeInputs(recipe, importInventory, importFluids)) return false;
        var list = GTUtility.itemHandlerToList(importInventory);
        var property = recipe.getProperty(DMEDataProperty.getInstance(), null);
        if (property == null) return false;
        var searchItem = property.dataItem;
        var searchTier = property.tier;
        for (var stack : list) {
            if (stack.getItem() == searchItem && DataModelHelper.getTier(stack) == searchTier) {
                DataModelHelper.setCurrentTierDataCount(stack,
                        Math.min(Integer.MAX_VALUE, DataModelHelper.getCurrentTierDataCount(stack) + property.getAddition()));
                DataModelHelper.setTotalSimulationCount(stack,
                        Math.min(Integer.MAX_VALUE, DataModelHelper.getTotalSimulationCount(stack) + property.getAddition()));
                var increase = true;
                while (increase) {
                    increase = DataModelHelperAccessor.tryIncreaseTier(stack);
                }
                return true;
            }
        }
        return false;
    }
}
