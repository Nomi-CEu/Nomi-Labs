package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.LabsImplNetworkFluidInventory;
import com.nomiceu.nomilabs.integration.ae2.NotifiableFluidInventory;

import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.fluids.helper.DualityFluidInterface;
import appeng.fluids.helper.IFluidInterfaceHost;
import appeng.fluids.util.AEFluidInventory;
import appeng.fluids.util.AEFluidStack;
import appeng.fluids.util.IAEFluidInventory;
import appeng.fluids.util.IAEFluidTank;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/448">AE2 #448</a> and its later hotfix PRs
 * to v0.56.5.
 */
@Mixin(value = DualityFluidInterface.class, remap = false)
public abstract class DualityFluidInterfaceMixin implements NotifiableFluidInventory {

    @Shadow
    @Final
    @Mutable
    private AEFluidInventory tanks;

    @Shadow
    public abstract void saveChanges();

    @Shadow
    @Final
    private IFluidInterfaceHost iHost;

    @Shadow
    @Final
    private AENetworkProxy gridProxy;

    @Shadow
    @Final
    private IActionSource mySource;

    @Shadow
    @Final
    public static int NUMBER_OF_TANKS;

    @Shadow
    @Final
    public static int TANK_CAPACITY;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectNetworkInv(AENetworkProxy networkProxy, IFluidInterfaceHost ih, CallbackInfo ci) {
        tanks = new LabsImplNetworkFluidInventory(this::labs$getStorageGrid, mySource, (IAEFluidInventory) this,
                NUMBER_OF_TANKS, TANK_CAPACITY);
    }

    @Unique
    @Override
    public void labs$onFluidInventoryChanged(IAEFluidTank inventory, FluidStack added, FluidStack removed) {
        if (inventory == tanks) {
            if (added != null) {
                iHost.onStackReturnNetwork(AEFluidStack.fromFluidStack(added));
            }
            saveChanges();
        }
    }

    @Unique
    private IStorageGrid labs$getStorageGrid() {
        try {
            return gridProxy.getStorage();
        } catch (GridAccessException e) {
            return null;
        }
    }
}
