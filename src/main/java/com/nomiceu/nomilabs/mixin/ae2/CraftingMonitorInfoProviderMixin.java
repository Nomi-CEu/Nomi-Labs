package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsItemNameElement;

import appeng.integration.modules.theoneprobe.TheOneProbeText;
import appeng.integration.modules.theoneprobe.tile.CraftingMonitorInfoProvider;
import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Properly localizes a TOP string.
 */
@Mixin(value = CraftingMonitorInfoProvider.class, remap = false)
public class CraftingMonitorInfoProviderMixin {

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;"),
              require = 1)
    private IProbeInfo properlyAddText(IProbeInfo instance, String s, @Local ItemStack stack) {
        return instance.element(new LabsItemNameElement(stack, TheOneProbeText.CRAFTING.getUnlocalized()));
    }
}
