package com.nomiceu.nomilabs.mixin.ae2;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.util.LabsSide;

import appeng.api.AEApi;
import appeng.api.definitions.IItemDefinition;
import appeng.client.me.SlotME;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerInterface;
import appeng.container.slot.*;
import appeng.util.Platform;

/**
 * Applies
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/f1e5a33a6e0f391c3083eb0ef163a3adaea7577f">AE2
 * f1e5a33</a> and its later fix commit, as well as
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/7baf538a7684cadffc012c2dc580ee52c84d552a">AE2
 * 7baf538</a>, for v0.56.5.
 */
@Mixin(value = AEBaseContainer.class, remap = false)
public abstract class AEBaseContainerMixin extends Container {

    @Shadow
    protected abstract void updateSlot(Slot clickSlot);

    @Shadow
    protected abstract ItemStack transferStackToContainer(ItemStack input);

    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true, remap = true)
    private void checkDisabled(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player,
                               CallbackInfoReturnable<ItemStack> cir) {
        if (slotId >= 0) {
            var slot = getSlot(slotId);
            if (slot instanceof SlotDisabled) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }

    /**
     * The fix
     * (<a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/7baf538a7684cadffc012c2dc580ee52c84d552a">AE2
     * 7baf538</a>) is very inconvenient, because it takes place in a loop. Just override the whole thing...
     */
    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true, remap = true)
    public void transferStackInSlotNew(EntityPlayer p, int idx, CallbackInfoReturnable<ItemStack> cir) {
        if (LabsSide.isClient()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        AppEngSlot clickSlot = (AppEngSlot) inventorySlots.get(idx); // require AE SLots!

        if (clickSlot instanceof SlotDisabled || clickSlot instanceof SlotInaccessible) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        if (clickSlot == null || !clickSlot.getHasStack()) {
            updateSlot(clickSlot);
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        ItemStack tis = clickSlot.getStack();

        if (tis.isEmpty()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        IItemDefinition expansionCard = AEApi.instance().definitions().materials().cardPatternExpansion();
        ContainerInterface casted;

        List<Slot> selectedSlots = new ArrayList<>();

        /*
         * Gather a list of valid destinations.
         */
        if (clickSlot.isPlayerSide()) {
            tis = transferStackToContainer(tis);

            if (!tis.isEmpty()) {
                // noinspection ConstantValue
                if ((Object) this instanceof ContainerInterface && expansionCard.isSameAs(tis) &&
                        (casted = (ContainerInterface) (Object) this).getPatternUpgrades() ==
                                casted.availableUpgrades() - 1) {
                    cir.setReturnValue(ItemStack.EMPTY); // Don't insert more pattern expansions than maximum useful
                    return;
                }

                // target slots in the container...
                for (Object inventorySlot : inventorySlots) {
                    AppEngSlot cs = (AppEngSlot) inventorySlot;

                    if (!(cs.isPlayerSide()) && !(cs instanceof SlotFake) && !(cs instanceof SlotCraftingMatrix)) {
                        if (cs.isItemValid(tis)) {
                            selectedSlots.add(cs);
                        }
                    }
                }
            }
        } else {
            tis = tis.copy();

            // target slots in the container...
            for (Object inventorySlot : inventorySlots) {
                AppEngSlot cs = (AppEngSlot) inventorySlot;

                if ((cs.isPlayerSide()) && !(cs instanceof SlotFake) && !(cs instanceof SlotCraftingMatrix)) {
                    if (cs.isItemValid(tis)) {
                        selectedSlots.add(cs);
                    }
                }
            }
        }

        /*
         * Handle Fake Slot Shift clicking.
         */
        if (selectedSlots.isEmpty() && clickSlot.isPlayerSide()) {
            if (!tis.isEmpty()) {
                // target slots in the container...
                for (Object inventorySlot : inventorySlots) {
                    AppEngSlot cs = (AppEngSlot) inventorySlot;
                    ItemStack destination = cs.getStack();

                    if (!(cs.isPlayerSide()) && cs instanceof SlotFake) {
                        if (Platform.itemComparisons().isSameItem(destination, tis)) {
                            break;
                        } else if (destination.isEmpty()) {
                            cs.putStack(tis.copy());
                            updateSlot(cs);
                            break;
                        }
                    }
                }
            }
        }

        if (!tis.isEmpty()) {
            // find partials..
            for (Slot d : selectedSlots) {
                if (d instanceof SlotDisabled || d instanceof SlotME) {
                    continue;
                }

                if (d.isItemValid(tis)) {
                    if (d.getHasStack()) {
                        ItemStack t = d.getStack().copy();

                        if (Platform.itemComparisons().isSameItem(tis, t)) // t.isItemEqual(tis))
                        {
                            if (d instanceof SlotRestrictedInput && ((SlotRestrictedInput) d).getPlaceableItemType() ==
                                    SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN) {
                                cir.setReturnValue(ItemStack.EMPTY); // don't insert duplicate encoded patterns to
                                                                     // interfaces
                                return;
                            }

                            int maxSize;
                            if (d instanceof SlotOversized slotOversized) {
                                maxSize = slotOversized.getSlotStackLimit();
                            } else {
                                maxSize = Math.min(tis.getMaxStackSize(), d.getSlotStackLimit());
                            }

                            int placeAble = maxSize - t.getCount();
                            if (placeAble <= 0) {
                                continue;
                            }

                            if (tis.getCount() < placeAble) {
                                placeAble = tis.getCount();
                            }

                            t.setCount(t.getCount() + placeAble);
                            tis.setCount(tis.getCount() - placeAble);

                            d.putStack(t);

                            if (tis.getCount() <= 0) {
                                clickSlot.putStack(ItemStack.EMPTY);
                                d.onSlotChanged();

                                updateSlot(clickSlot);
                                updateSlot(d);
                                cir.setReturnValue(ItemStack.EMPTY);
                                return;
                            } else {
                                updateSlot(d);
                            }
                        }
                    }
                }
            }

            // any match..
            for (Slot d : selectedSlots) {
                if (d instanceof SlotDisabled || d instanceof SlotME) {
                    continue;
                }

                if (d.isItemValid(tis)) {
                    if (!d.getHasStack()) {
                        int maxSize;
                        if (d instanceof SlotOversized slotOversized) {
                            maxSize = slotOversized.getSlotStackLimit();
                        } else {
                            maxSize = Math.min(tis.getMaxStackSize(), d.getSlotStackLimit());
                        }

                        ItemStack tmp = tis.copy();
                        if (tmp.getCount() > maxSize) {
                            tmp.setCount(maxSize);
                        }

                        tis.setCount(tis.getCount() - tmp.getCount());
                        d.putStack(tmp);

                        if (tis.getCount() <= 0) {
                            clickSlot.putStack(ItemStack.EMPTY);
                            d.onSlotChanged();

                            updateSlot(clickSlot);
                            updateSlot(d);
                            cir.setReturnValue(ItemStack.EMPTY);
                            return;
                        } else {
                            updateSlot(d);

                            // noinspection ConstantValue
                            if ((d instanceof SlotRestrictedInput && ((SlotRestrictedInput) d).getPlaceableItemType() ==
                                    SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN) ||
                                    ((Object) this instanceof ContainerInterface && expansionCard.isSameAs(tis) &&
                                            (casted = (ContainerInterface) (Object) this).getPatternUpgrades() ==
                                                    casted.availableUpgrades() - 1)) {
                                break; // Only insert one pattern when shift-clicking into interfaces, and don't insert
                                       // more pattern expansions than maximum useful
                            }
                        }
                    }
                }
            }
        }

        clickSlot.putStack(!tis.isEmpty() ? tis : ItemStack.EMPTY);

        updateSlot(clickSlot);
        cir.setReturnValue(ItemStack.EMPTY);
    }
}
