package com.nomiceu.nomilabs.gregtech.mixinhelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.capability.impl.GTFluidHandlerItemStack;

/**
 * Handles locked fluids appropriately.
 */
public class QuantumFluidHandlerItemStack extends GTFluidHandlerItemStack {

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public QuantumFluidHandlerItemStack(@NotNull ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public @Nullable FluidStack getFluid() {
        FluidStack result = super.getFluid();
        if (result != null) return result;

        NBTTagCompound tag = container.getTagCompound();
        if (tag == null || !tag.hasKey("LockedFluid", Constants.NBT.TAG_COMPOUND))
            return null;

        result = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("LockedFluid"));
        if (result == null) return null;

        result.amount = 0;
        return result;
    }
}
