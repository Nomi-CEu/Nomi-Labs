package com.nomiceu.nomilabs.mixin.solarflux;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.solarflux.AccessibleInventoryDummy;

import tk.zeitheron.solarflux.utils.InventoryDummy;

@Mixin(value = InventoryDummy.class, remap = false)
public class InventoryDummyMixin implements AccessibleInventoryDummy {

    @Unique
    private boolean labs$stateChanged = true;

    @Unique
    @Override
    public boolean labs$stateChanged() {
        var old = labs$stateChanged;
        labs$stateChanged = false;
        return old;
    }

    @Inject(method = "markDirty", at = @At("RETURN"), remap = true)
    private void handleMarkDirty(CallbackInfo ci) {
        labs$stateChanged = true;
    }

    @Inject(method = "setInventorySlotContents", at = @At("RETURN"), remap = true)
    private void handleInventoryChanged(int index, ItemStack stack, CallbackInfo ci) {
        labs$stateChanged = true;
    }
}
