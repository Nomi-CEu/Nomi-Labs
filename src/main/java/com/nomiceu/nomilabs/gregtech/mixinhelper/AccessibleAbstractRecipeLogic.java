package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface AccessibleAbstractRecipeLogic {

    List<ItemStack> labs$getOutputs();

    List<FluidStack> labs$getFluidOutputs();

    int labs$getEUt();
}
