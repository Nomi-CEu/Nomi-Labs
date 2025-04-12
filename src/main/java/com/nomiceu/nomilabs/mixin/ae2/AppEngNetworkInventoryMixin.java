package com.nomiceu.nomilabs.mixin.ae2;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.RangedWrapper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.integration.ae2.AccessibleAppEngNetworkInventory;

import appeng.tile.inventory.AppEngInternalOversizedInventory;
import appeng.tile.inventory.AppEngNetworkInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.filter.IAEItemFilter;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/454">AE2 #454</a> to v0.56.6.
 */
@Mixin(value = AppEngNetworkInventory.class, remap = false)
public class AppEngNetworkInventoryMixin extends AppEngInternalOversizedInventory
                                         implements AccessibleAppEngNetworkInventory {

    /**
     * Mandatory Ignored Constructor
     */
    private AppEngNetworkInventoryMixin(IAEAppEngInventory inventory, int size, int maxStack, IAEItemFilter filter) {
        super(inventory, size, maxStack, filter);
    }

    @Unique
    @Nonnull
    private ItemStack labs$insertToBuffer(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @Unique
    @Override
    public RangedWrapper labs$getBufferWrapper(int selectSlot) {
        return new RangedWrapper(this, selectSlot, selectSlot + 1) {

            @Override
            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 0) {
                    return labs$insertToBuffer(selectSlot, stack, simulate);
                }
                return stack;
            }
        };
    }
}
