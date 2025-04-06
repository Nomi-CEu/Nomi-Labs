package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

public interface AccessibleAbstractRecipeLogic {

    boolean labs$isValidForOutputTop();

    List<ItemStack> labs$getOutputs();

    List<FluidStack> labs$getFluidOutputs();

    int labs$getEUt();

    int labs$getNonChancedItemAmt();

    List<Pair<ItemStack, Integer>> labs$getChancedItemOutputs();

    int labs$getNonChancedFluidAmt();

    List<Pair<FluidStack, Integer>> labs$getChancedFluidOutputs();
}
