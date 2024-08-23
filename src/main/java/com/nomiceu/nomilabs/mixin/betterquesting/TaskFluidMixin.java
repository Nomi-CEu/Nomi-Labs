package com.nomiceu.nomilabs.mixin.betterquesting;

import java.util.*;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.ParticipantInfo;
import betterquesting.questing.tasks.TaskFluid;

/**
 * Fixes issues with detecting fluids in stacks with count > 1.
 */
@Mixin(value = TaskFluid.class, remap = false)
public abstract class TaskFluidMixin {

    @Shadow
    public abstract boolean isComplete(UUID uuid);

    @Shadow
    protected abstract List<Tuple<UUID, int[]>> getBulkProgress(@NotNull List<UUID> uuids);

    @Shadow
    public boolean consume;

    @Shadow
    public boolean groupDetect;

    @Shadow
    @Final
    public NonNullList<FluidStack> requiredFluids;

    @Shadow
    public boolean ignoreNbt;

    @Shadow
    protected abstract void setBulkProgress(@NotNull List<Tuple<UUID, int[]>> list);

    @Shadow
    protected abstract void checkAndComplete(ParticipantInfo pInfo, DBEntry<IQuest> quest, boolean resync);

    @Inject(method = "detect", at = @At("HEAD"), cancellable = true)
    private void replaceDetect(ParticipantInfo pInfo, DBEntry<IQuest> quest, CallbackInfo ci) {
        if (!LabsConfig.modIntegration.enableBQuFluidTaskFixes) return;

        ci.cancel();

        if (isComplete(pInfo.UUID)) return;

        // Removing the consume check here would make the task cheaper on groups and for that reason sharing is
        // restricted to detect only
        List<Tuple<UUID, int[]>> progress = getBulkProgress(
                consume ? Collections.singletonList(pInfo.UUID) : pInfo.ALL_UUIDS);
        boolean updated = false;

        if (!consume) {
            if (groupDetect) // Reset all detect progress
                progress.forEach((value) -> Arrays.fill(value.getSecond(), 0));
            else {
                for (int i = 0; i < requiredFluids.size(); i++) {
                    final int r = requiredFluids.get(i).amount;
                    for (Tuple<UUID, int[]> value : progress) {
                        int n = value.getSecond()[i];
                        if (n != 0 && n < r) {
                            value.getSecond()[i] = 0;
                            updated = true;
                        }
                    }
                }
            }
        }

        List<InventoryPlayer> invoList;
        if (consume) {
            // We do not support consuming resources from other member's inventories.
            // This could otherwise be abused to siphon items/fluids unknowingly
            invoList = Collections.singletonList(pInfo.PLAYER.inventory);
        } else {
            invoList = new ArrayList<>();
            pInfo.ACTIVE_PLAYERS.forEach((p) -> invoList.add(p.inventory));
        }

        for (InventoryPlayer invo : invoList) {
            for (int i = 0; i < invo.getSizeInventory(); i++) {
                ItemStack stack = invo.getStackInSlot(i);

                if (stack.isEmpty()) continue;

                // Make a copy of the stack to retrieve the fluid amount & info.
                // Set count to 1, otherwise fluid handlers may not allow draining
                var toRetrieveInfo = stack.copy();
                toRetrieveInfo.setCount(1);

                var handler = FluidUtil.getFluidHandler(toRetrieveInfo);
                if (handler == null) continue;

                for (int j = 0; j < requiredFluids.size(); j++) {
                    FluidStack rStack = requiredFluids.get(j);

                    boolean hasDrained = false;
                    boolean requiresFullDrain = false;
                    int fullDrainAmt = 0;

                    // Initial Check
                    FluidStack rStackOg = rStack.copy();
                    rStackOg.amount = (int) Math.ceil((double) rStack.amount / (double) stack.getCount());
                    FluidStack sample = handler.drain(rStackOg, false);
                    if (sample == null || sample.amount <= 0) {
                        // Check if we can drain the entire container instead (Simple Fluid Handler)
                        if (handler.getTankProperties().length < 1) continue;

                        fullDrainAmt = handler.getTankProperties()[0].getCapacity();
                        if (fullDrainAmt <= 0) continue;
                        rStackOg.amount = fullDrainAmt;

                        sample = handler.drain(rStackOg, false);
                        if (sample == null || sample.amount <= 0) continue;

                        requiresFullDrain = true;
                    }

                    // Theoretically this could work in consume mode for parties but the priority order and manual
                    // submission code would need changing
                    for (Tuple<UUID, int[]> value : progress) {
                        if (value.getSecond()[j] >= rStack.amount) continue;
                        int remaining = rStack.amount - value.getSecond()[j];

                        FluidStack drain = rStack.copy();

                        // Take the max, so we are not removing less than required
                        if (requiresFullDrain)
                            drain.amount = fullDrainAmt;
                        else
                            drain.amount = (int) Math.ceil((double) remaining / (double) stack.getCount());
                        if (ignoreNbt) drain.tag = null;
                        if (drain.amount <= 0) continue;

                        FluidStack fluid = handler.drain(drain, consume);
                        if (fluid == null || fluid.amount <= 0) continue;

                        value.getSecond()[j] += Math.min(fluid.amount * stack.getCount(), remaining);
                        hasDrained = true;
                        updated = true;
                    }

                    if (!hasDrained) continue;

                    if (consume) {
                        // Restore Stack Count
                        var result = handler.getContainer();
                        result.setCount(stack.getCount());

                        // Set Contents
                        invo.setInventorySlotContents(i, result);
                    }

                    break;
                }
            }

            if (updated) setBulkProgress(progress);
            checkAndComplete(pInfo, quest, updated);
        }
    }
}
