package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsLangKeyWithArgsElement;

import appeng.integration.modules.theoneprobe.TheOneProbeText;
import appeng.integration.modules.theoneprobe.part.ChannelInfoProvider;
import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Properly localizes a TOP string.
 */
@Mixin(value = ChannelInfoProvider.class, remap = false)
public class ChannelInfoProviderMixin {

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;"),
              require = 1)
    private IProbeInfo properlyAddText(IProbeInfo instance, String s, @Local(ordinal = 0) int usedChannels,
                                       @Local(ordinal = 1) int maxChannels) {
        return instance.element(new LabsLangKeyWithArgsElement(TheOneProbeText.CHANNELS.getUnlocalized(),
                Integer.toString(usedChannels), Integer.toString(maxChannels)));
    }
}
