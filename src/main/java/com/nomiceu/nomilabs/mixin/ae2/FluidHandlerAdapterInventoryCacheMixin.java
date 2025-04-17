package com.nomiceu.nomilabs.mixin.ae2;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.api.AEApi;
import appeng.api.config.StorageFilter;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import appeng.fluids.util.AEFluidStack;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/483">AE2 #483</a> for v0.56.5.
 */
@Mixin(targets = "appeng.fluids.parts.FluidHandlerAdapter$InventoryCache", remap = false)
public class FluidHandlerAdapterInventoryCacheMixin {

    @Shadow
    @Final
    private IFluidHandler fluidHandler;

    @Shadow
    IItemList<IAEFluidStack> currentlyCached;

    @Shadow
    @Final
    private StorageFilter mode;

    @Inject(method = "update", at = @At(value = "HEAD"), cancellable = true)
    private void useTankContents(CallbackInfoReturnable<List<IAEFluidStack>> cir) {
        List<IAEFluidStack> changes = new ArrayList<>();
        IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
        IItemList<IAEFluidStack> currentlyOnStorage = AEApi.instance().storage()
                .getStorageChannel(IFluidStorageChannel.class).createList();

        for (IFluidTankProperties tankProperty : tankProperties) {
            var contents = tankProperty.getContents();
            if (mode != StorageFilter.EXTRACTABLE_ONLY || this.fluidHandler.drain(contents, false) != null) {
                currentlyOnStorage.add(AEFluidStack.fromFluidStack(contents));
            }
        }

        for (IAEFluidStack is : currentlyCached) {
            is.setStackSize(-is.getStackSize());
        }

        for (IAEFluidStack is : currentlyOnStorage) {
            currentlyCached.add(is);
        }

        for (IAEFluidStack is : currentlyCached) {
            if (is.getStackSize() != 0L) {
                changes.add(is);
            }
        }

        currentlyCached = currentlyOnStorage;
        cir.setReturnValue(changes);
    }
}
