package com.nomiceu.nomilabs.mixin.enderio;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crazypants.enderio.api.farm.IFarmingTool;
import crazypants.enderio.base.farming.FarmingTool;
import crazypants.enderio.base.integration.tic.TicProxy;
import crazypants.enderio.machines.machine.farm.FarmSlots;
import crazypants.enderio.machines.machine.farm.TileFarmStation;
import crazypants.enderio.util.Prep;

/**
 * Improves slot checking for farming station:
 * - Fixes not being able to quickswap tools (e.g. holding a golden axe, clicking on a slot containing diamond axe)
 * - Fixes dark backhoe durability resetting glitch
 */
@Mixin(value = TileFarmStation.class, remap = false)
public abstract class TileFarmStationMixin {

    @Shadow
    protected abstract FarmSlots getSlotForTool(@NotNull IFarmingTool tool);

    @Inject(method = "isMachineItemValidForSlot", at = @At("HEAD"), cancellable = true)
    private void betterSlotChecking(int i, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Prep.isInvalid(stack) && i <= 2) {
            IFarmingTool toolType = FarmingTool.getToolType(stack);
            if (toolType != FarmingTool.NONE && !TicProxy.isBroken(stack) && !FarmingTool.isDryRfTool(stack)) {
                var slot = getSlotForTool(toolType);
                cir.setReturnValue(slot == null || slot.ordinal() == i);
            }
        }
    }
}
