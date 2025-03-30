package com.nomiceu.nomilabs.mixin.storagedrawers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributesModifiable;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawerKey;
import com.nomiceu.nomilabs.integration.storagedrawers.RefreshableDrawer;

/**
 * Calls Refresh Drawer After Usage.
 */
@Mixin(value = ItemDrawerKey.class, remap = false)
public class ItemDrawerKeyMixin {

    @Inject(method = "handleDrawerAttributes", at = @At("RETURN"))
    private void refreshDrawer(IDrawerAttributesModifiable attrs, CallbackInfo ci) {
        ((RefreshableDrawer) attrs).refreshAfterDrawerKey();
    }
}
