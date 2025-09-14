package com.nomiceu.nomilabs.mixin.ae2stuff;

import net.bdew.ae2stuff.top.TOPHandler$;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import mcjty.theoneprobe.api.IProbeInfo;

/**
 * Skips a TOP element.
 */
@Mixin(value = TOPHandler$.class, remap = false)
public class TOPHandlerMixin {

    /**
     * Since the stored energy amount doesn't even show correctly, remove it instead of properly localising.
     */
    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;",
                       ordinal = 0))
    private static IProbeInfo cancelStoredEnergyDisplay(IProbeInfo instance, String s) {
        return instance;
    }
}
