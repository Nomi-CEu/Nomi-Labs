package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.InventoryPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.AccessibleGuiMEMonitorable;

import appeng.api.implementations.guiobjects.IPortableCell;
import appeng.client.gui.implementations.GuiMEPortableCell;
import appeng.container.implementations.ContainerMEPortableCell;

/**
 * Applies
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/f1e5a33a6e0f391c3083eb0ef163a3adaea7577f">AE2
 * f1e5a33</a> to v0.56.5.
 */
@Mixin(value = GuiMEPortableCell.class, remap = false)
public class GuiMEPortableCellMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void useProperContainer(InventoryPlayer inventoryPlayer, IPortableCell te, CallbackInfo ci) {
        ((AccessibleGuiMEMonitorable) this).labs$updateContainer(new ContainerMEPortableCell(inventoryPlayer, te));
    }
}
