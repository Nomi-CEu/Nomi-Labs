package com.nomiceu.nomilabs.mixin.ae2;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.client.gui.implementations.GuiCraftConfirm;

/**
 * Sorts the list of craft items to be missing, then to craft, then present.
 */
@Mixin(value = GuiCraftConfirm.class, remap = false)
public class GuiCraftConfirmMixin {

    @Shadow
    @Final
    private List<IAEItemStack> visual;

    @Shadow
    @Final
    private IItemList<IAEItemStack> missing;

    @Shadow
    @Final
    private IItemList<IAEItemStack> pending;

    @Inject(method = "postUpdate", at = @At("RETURN"))
    private void sortVisual(List<IAEItemStack> list, byte ref, CallbackInfo ci) {
        labs$sortVisual();
    }

    @Unique
    private void labs$sortVisual() {
        List<IAEItemStack> currMissing = new ArrayList<>();
        List<IAEItemStack> currToCraft = new ArrayList<>();
        List<IAEItemStack> currPresent = new ArrayList<>();

        for (var stack : visual) {
            IAEItemStack missingStack = missing.findPrecise(stack);
            IAEItemStack pendingStack = pending.findPrecise(stack);

            // Handle Missing
            if (missingStack != null && missingStack.getStackSize() > 0L) {
                currMissing.add(stack);
                continue;
            }

            if (pendingStack != null && pendingStack.getStackSize() > 0L)
                currToCraft.add(stack);
            else
                currPresent.add(stack);
        }

        visual.clear();
        visual.addAll(currMissing);
        visual.addAll(currToCraft);
        visual.addAll(currPresent);
    }
}
