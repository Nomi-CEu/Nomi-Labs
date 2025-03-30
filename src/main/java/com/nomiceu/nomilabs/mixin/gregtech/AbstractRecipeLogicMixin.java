package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleAbstractRecipeLogic;

import gregtech.api.capability.impl.AbstractRecipeLogic;

/**
 * Allows accessing outputs/fluid outputs.
 */
@Mixin(value = AbstractRecipeLogic.class, remap = false)
public class AbstractRecipeLogicMixin implements AccessibleAbstractRecipeLogic {

    @Shadow
    protected NonNullList<ItemStack> itemOutputs;

    @Shadow
    protected List<FluidStack> fluidOutputs;

    @Shadow
    protected int recipeEUt;

    @Unique
    @Override
    public List<ItemStack> labs$getOutputs() {
        return itemOutputs;
    }

    @Unique
    @Override
    public List<FluidStack> labs$getFluidOutputs() {
        return fluidOutputs;
    }

    @Unique
    @Override
    public int labs$getEUt() {
        return recipeEUt;
    }
}
