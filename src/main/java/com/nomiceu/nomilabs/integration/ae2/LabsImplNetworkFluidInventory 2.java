package com.nomiceu.nomilabs.integration.ae2;

import java.util.function.Supplier;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.fluids.util.AEFluidInventory;
import appeng.fluids.util.AEFluidStack;
import appeng.fluids.util.IAEFluidInventory;

public class LabsImplNetworkFluidInventory extends AEFluidInventory {

    private final Supplier<IStorageGrid> supplier;
    private final IActionSource source;

    private final NotifiableFluidInventory handler;

    public LabsImplNetworkFluidInventory(Supplier<IStorageGrid> networkSupplier, IActionSource source,
                                         IAEFluidInventory handler, int slots, int capcity) {
        super(handler, slots, capcity);
        this.supplier = networkSupplier;
        this.source = source;
        this.handler = (NotifiableFluidInventory) handler;
    }

    @Override
    public int fill(FluidStack fluid, boolean doFill) {
        if (fluid == null || fluid.amount <= 0) {
            return 0;
        }
        IStorageGrid storage = supplier.get();
        if (storage != null) {
            int originAmt = fluid.amount;
            IMEInventory<IAEFluidStack> dest = storage
                    .getInventory(AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class));
            IAEFluidStack overflow = dest.injectItems(AEFluidStack.fromFluidStack(fluid),
                    doFill ? Actionable.MODULATE : Actionable.SIMULATE, this.source);
            if (overflow != null && overflow.getStackSize() == originAmt) {
                return super.fill(fluid, doFill);
            } else if (overflow != null) {
                if (doFill) {
                    FluidStack added = fluid.copy();
                    added.amount = (int) (fluid.amount - overflow.getStackSize());
                    handler.labs$onFluidInventoryChanged(this, added, null);
                }
                return (int) (originAmt - overflow.getStackSize());
            } else {
                if (doFill) {
                    handler.labs$onFluidInventoryChanged(this, fluid, null);
                }
                return originAmt;
            }
        } else {
            return super.fill(fluid, doFill);
        }
    }
}
