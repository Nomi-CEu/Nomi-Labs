package com.nomiceu.nomilabs.mixin.bogosorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.UnmodifiableView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cleanroommc.bogosorter.api.ISlot;
import com.cleanroommc.bogosorter.common.sort.SlotGroup;
import com.cleanroommc.bogosorter.common.sort.SortHandler;
import com.google.common.collect.ImmutableList;

/**
 * Prevents Modification of 'Locked' Slots.
 */
@Mixin(value = SortHandler.class, remap = false)
public class SortHandlerMixin {

    @Shadow
    @Final
    private EntityPlayer player;

    @Redirect(method = "sortHorizontal",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;getSlots()Ljava/util/List;"))
    private @UnmodifiableView List<ISlot> checkLockedSlots1(SlotGroup instance) {
        return labs$getSlotsLogic(instance);
    }

    @Redirect(method = "gatherItems",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;getSlots()Ljava/util/List;"))
    private @UnmodifiableView List<ISlot> checkLockedSlots2(SlotGroup instance) {
        return labs$getSlotsLogic(instance);
    }

    @Redirect(method = "clearAllItems",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;getSlots()Ljava/util/List;"))
    private @UnmodifiableView List<ISlot> checkLockedSlots3(SlotGroup instance) {
        return labs$getSlotsLogic(instance);
    }

    @Redirect(method = "randomizeItems",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;getSlots()Ljava/util/List;"))
    private @UnmodifiableView List<ISlot> checkLockedSlots4(SlotGroup instance) {
        return labs$getSlotsLogic(instance);
    }

    /**
     * Sort Bogo is static, so we need to redirect calls of it
     */
    @Redirect(method = "sort(Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;Z)V",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/bogosorter/common/sort/SortHandler;sortBogo(Lcom/cleanroommc/bogosorter/common/sort/SlotGroup;)V"))
    private void newSortBogo(SlotGroup instance) {
        List<ItemStack> items = new ArrayList<>();

        for (var slot : labs$getSlotsLogic(instance)) {
            ItemStack stack = slot.bogo$getStack();
            items.add(stack);
        }

        Collections.shuffle(items);
        List<ISlot> slots = labs$getSlotsLogic(instance);

        for (int i = 0; i < slots.size(); i++) {
            ISlot slot = slots.get(i);
            slot.bogo$putStack(items.get(i));
        }
    }

    @Unique
    private @UnmodifiableView List<ISlot> labs$getSlotsLogic(SlotGroup instance) {
        var slots = instance.getSlots();
        List<ISlot> result = new ArrayList<>();

        for (var slot : slots) {
            /*
             * Logic being used to check if we cannot access the slot:
             * 1. Can the player take the stack?
             * This usually returns true, but some slot implementations return false if the slot is empty.
             *
             * 2. Can we insert the current stack into the slot?
             * This may seem roundabout, but this means that if it returns false, then most likely, the slot is
             * always returning false.
             *
             * 3. Is the stack in the slot empty?
             * If it is empty, some implementations return false for both above methods.
             * Although this might risk changing actually inaccessible slots, most likely, those slots would not be
             * empty.
             * 
             * The slot should only be marked as inaccessible if all three conditions return false.
             */
            boolean canTake = slot.bogo$canTakeStack(player);
            boolean canInsert = slot.bogo$isItemValid(slot.bogo$getStack().copy());
            boolean isEmpty = slot.bogo$getStack().isEmpty();
            if (canTake || canInsert || isEmpty) result.add(slot);
        }
        return ImmutableList.copyOf(result);
    }
}
