package com.nomiceu.nomilabs.mixin.ae2;

import javax.annotation.Nullable;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.integration.ae2.LabsImplNetworkInventory;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.data.IAEItemStack;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.helpers.UnlockCraftingEvent;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/448">AE2 #448</a> and its later hotfix PRs,
 * as well as <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/507">AE2 #507</a>
 * to v0.56.5.
 */
@Mixin(value = DualityInterface.class, remap = false)
public class DualityInterfaceMixin {

    @Shadow
    @Final
    @Mutable
    private AppEngInternalInventory storage;

    @Shadow
    @Final
    private AENetworkProxy gridProxy;

    @Shadow
    @Final
    private IActionSource mySource;

    @Shadow
    @Final
    public static int NUMBER_OF_STORAGE_SLOTS;

    @Shadow
    @Nullable
    private UnlockCraftingEvent unlockEvent;

    @Shadow
    @Nullable
    private IAEItemStack unlockStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectNetworkInv(AENetworkProxy networkProxy, IInterfaceHost ih, CallbackInfo ci) {
        storage = new LabsImplNetworkInventory(this::labs$getStorageGrid, mySource, (IAEAppEngInventory) this,
                NUMBER_OF_STORAGE_SLOTS, 512);
    }

    @WrapOperation(method = "getAdaptor",
                   at = @At(value = "NEW",
                            target = "(Lnet/minecraftforge/items/IItemHandlerModifiable;II)Lnet/minecraftforge/items/wrapper/RangedWrapper;"),
                   require = 1)
    private RangedWrapper returnNewRangedWrapper(IItemHandlerModifiable compose, int minSlot, int maxSlotExclusive,
                                                 Operation<RangedWrapper> original) {
        return ((LabsImplNetworkInventory) compose).getBufferWrapper(minSlot);
    }

    @Redirect(method = "onPushPatternSuccess",
              at = @At(value = "INVOKE",
                       target = "Lappeng/api/networking/crafting/ICraftingPatternDetails;getPrimaryOutput()Lappeng/api/storage/data/IAEItemStack;"),
              require = 1)
    private IAEItemStack copyStack(ICraftingPatternDetails instance) {
        return instance.getPrimaryOutput().copy();
    }

    @Inject(method = "onStackReturnedToNetwork", at = @At("HEAD"), cancellable = true)
    private void properlyCheckMetaAndNBT(IAEItemStack stack, CallbackInfo ci) {
        if (unlockEvent != UnlockCraftingEvent.RESULT || unlockStack == null) return;

        ci.cancel();

        if (unlockStack.isSameType(stack)) {
            long remainingAmount = unlockStack.getStackSize() - stack.getStackSize();
            if (remainingAmount <= 0L) {
                unlockEvent = null;
                unlockStack = null;
            } else {
                unlockStack.setStackSize(remainingAmount);
            }
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
