package com.nomiceu.nomilabs.integration.ae2;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.RangedWrapper;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.tile.inventory.AppEngInternalOversizedInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.item.AEItemStack;

public class LabsImplNetworkInventory extends AppEngInternalOversizedInventory {

    private final Supplier<IStorageGrid> supplier;
    private final IActionSource source;

    public LabsImplNetworkInventory(Supplier<IStorageGrid> networkSupplier, IActionSource source,
                                    IAEAppEngInventory inventory, int size, int maxStack) {
        super(inventory, size, maxStack);
        this.supplier = networkSupplier;
        this.source = source;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        IStorageGrid storage = supplier.get();
        if (storage != null) {
            int originAmt = stack.getCount();
            IMEInventory<IAEItemStack> dest = storage
                    .getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));
            IAEItemStack overflow = dest.injectItems(AEItemStack.fromItemStack(stack),
                    simulate ? Actionable.SIMULATE : Actionable.MODULATE, this.source);
            if (overflow != null && overflow.getStackSize() == originAmt) {
                return super.insertItem(slot, stack, simulate);
            } else if (overflow != null) {
                if (!simulate) {
                    ItemStack added = stack.copy();
                    added.setCount((int) (stack.getCount() - overflow.getStackSize()));
                    getTileEntity().onChangeInventory(this, slot, InvOperation.INSERT, ItemStack.EMPTY, added);
                }
                return overflow.createItemStack();
            } else {
                if (!simulate) {
                    getTileEntity().onChangeInventory(this, slot, InvOperation.INSERT, ItemStack.EMPTY, stack);
                }
                return ItemStack.EMPTY;
            }
        } else {
            return super.insertItem(slot, stack, simulate);
        }
    }

    @Nonnull
    private ItemStack insertToBuffer(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public RangedWrapper getBufferWrapper(int selectSlot) {
        return new RangedWrapper(this, selectSlot, selectSlot + 1) {

            @Override
            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 0) {
                    return insertToBuffer(selectSlot, stack, simulate);
                }
                return stack;
            }
        };
    }
}
