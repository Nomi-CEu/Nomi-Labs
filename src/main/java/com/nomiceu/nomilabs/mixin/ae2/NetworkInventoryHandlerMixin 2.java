package com.nomiceu.nomilabs.mixin.ae2;

import java.util.*;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.api.config.Actionable;
import appeng.api.config.SecurityPermissions;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.me.storage.NetworkInventoryHandler;

/**
 * Apply <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/533">AE2 #533</a> for v0.56.5.
 */
@Mixin(value = NetworkInventoryHandler.class, remap = false)
public abstract class NetworkInventoryHandlerMixin<T extends IAEStack<T>> {

    @Shadow
    @Final
    private static Comparator<Integer> PRIORITY_SORTER;

    @Shadow
    @Final
    private NavigableMap<Integer, List<IMEInventoryHandler<T>>> priorityInventory;

    @Shadow
    @Final
    private NavigableMap<Integer, List<IMEInventoryHandler<T>>> stickyPriorityInventory;

    @Shadow
    protected abstract boolean diveList(NetworkInventoryHandler<T> networkInventoryHandler, Actionable type);

    @Shadow
    protected abstract boolean testPermission(IActionSource src, SecurityPermissions permission);

    @Shadow
    protected abstract void surface(NetworkInventoryHandler<T> networkInventoryHandler, Actionable type);

    @Shadow
    protected abstract IItemList<T> iterateInventories(IItemList<T> out,
                                                       NavigableMap<Integer, List<IMEInventoryHandler<T>>> map);

    @Unique
    private final NavigableMap<Integer, List<IMEInventoryHandler<T>>> labs$craftingPriorityInventory = new TreeMap<>(
            PRIORITY_SORTER);

    @Inject(method = "addNewStorage", at = @At("HEAD"), cancellable = true)
    private void handleCrafting(IMEInventoryHandler<T> h, CallbackInfo ci) {
        int priority = h.getPriority();

        NavigableMap<Integer, List<IMEInventoryHandler<T>>> map;
        if (h instanceof ICraftingGrid) {
            map = labs$craftingPriorityInventory;
        } else if (h.isSticky()) {
            map = stickyPriorityInventory;
        } else {
            map = priorityInventory;
        }

        map.computeIfAbsent(priority, _priority -> new ArrayList<>()).add(h);
        ci.cancel();
    }

    /**
     * The fix presents some issues to implement neatly, since it modifies a parameter. Just overwrite it.
     */
    @Inject(method = "injectItems", at = @At("HEAD"), cancellable = true)
    private void checkCraftingInv(T input, Actionable type, IActionSource src, CallbackInfoReturnable<T> cir) {
        if (diveList(labs$this(), type)) {
            cir.setReturnValue(input);
            return;
        } else if (testPermission(src, SecurityPermissions.INJECT)) {
            surface(labs$this(), type);
            cir.setReturnValue(input);
            return;
        }

        // First pass. Check if the crafting grid is awaiting the input.
        for (List<IMEInventoryHandler<T>> invList : labs$craftingPriorityInventory.values()) {
            Iterator<IMEInventoryHandler<T>> ii = invList.iterator();
            while (ii.hasNext() && input != null) {
                IMEInventoryHandler<T> inv = ii.next();

                if (inv.canAccept(input) &&
                        (inv.isPrioritized(input) || inv.extractItems(input, Actionable.SIMULATE, src) != null)) {
                    input = inv.injectItems(input, type, src);
                }
            }
        }

        // If everything got stored in the crafting storage, no need to continue.
        if (input == null) {
            surface(labs$this(), type);
            cir.setReturnValue(null);
            return;
        }

        boolean stickyInventoryFound = false;

        // For this pass we do return input if the item is able to go into a sticky inventory. We NEVER want to try and
        // insert the item into a non-sticky inventory if it could already go into a sticky inventory.
        for (List<IMEInventoryHandler<T>> stickyInvList : stickyPriorityInventory.values()) {
            Iterator<IMEInventoryHandler<T>> ii = stickyInvList.iterator();
            while (ii.hasNext() && input != null) {
                IMEInventoryHandler<T> inv = ii.next();
                if (inv.validForPass(1) && inv.canAccept(input) &&
                        (inv.isPrioritized(input) || inv.extractItems(input, Actionable.SIMULATE, src) != null)) {
                    input = inv.injectItems(input, type, src);
                    stickyInventoryFound = true;
                }
            }
        }

        if (stickyInventoryFound) {
            surface(labs$this(), type);
            cir.setReturnValue(input);
            return;
        }

        for (List<IMEInventoryHandler<T>> invList : priorityInventory.values()) {
            Iterator<IMEInventoryHandler<T>> ii = invList.iterator();
            while (ii.hasNext() && input != null) {
                IMEInventoryHandler<T> inv = ii.next();

                if (inv.validForPass(1) && inv
                        .canAccept(input) &&
                        (inv.isPrioritized(input) || inv.extractItems(input, Actionable.SIMULATE, src) != null)) {
                    input = inv.injectItems(input, type, src);
                }
            }

            // We need to ignore prioritized inventories in the second pass. If they were not able to store everything
            // during the first pass, they will do so in the second, but as this is stateless we will just report twice
            // the amount of storable items.
            ii = invList.iterator();
            while (ii.hasNext() && input != null) {
                IMEInventoryHandler<T> inv = ii.next();

                if (inv.validForPass(2) && inv.canAccept(input) && !inv.isPrioritized(input)) {
                    input = inv.injectItems(input, type, src);
                }
            }
        }

        surface(labs$this(), type);

        cir.setReturnValue(input);
    }

    @Inject(method = "getAvailableItems",
            at = @At(value = "INVOKE",
                     target = "Lappeng/me/storage/NetworkInventoryHandler;surface(Lappeng/me/storage/NetworkInventoryHandler;Lappeng/api/config/Actionable;)V"),
            require = 1,
            remap = false)
    private void iterateCrafting(IItemList<T> out, CallbackInfoReturnable<IItemList<T>> cir) {
        iterateInventories(out, labs$craftingPriorityInventory);
    }

    @Unique
    private NetworkInventoryHandler<T> labs$this() {
        // noinspection unchecked
        return (NetworkInventoryHandler<T>) (Object) this;
    }
}
