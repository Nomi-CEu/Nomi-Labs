package com.nomiceu.nomilabs.gregtech.recipelogic;

import gregtech.api.capability.impl.MultiblockFuelRecipeLogic;
import gregtech.api.metatileentity.multiblock.FuelMultiblockController;

public class NaqRecipeLogic extends MultiblockFuelRecipeLogic {

    public NaqRecipeLogic(FuelMultiblockController tileEntity) {
        super(tileEntity);
    }

    @Override
    public int getParallelLimit() {
        // No Parallel for Naq Reactors
        return 1;
    }

    @Override
    public boolean isAllowOverclocking() {
        return false;
    }
}
