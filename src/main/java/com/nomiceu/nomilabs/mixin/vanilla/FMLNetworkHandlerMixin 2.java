package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixinhelper.AccessibleModListMessage;
import com.nomiceu.nomilabs.util.LabsModeHelper;

/**
 * Checks Mode Data in Mod List Handshake.
 */
@Mixin(value = FMLNetworkHandler.class, remap = false)
public class FMLNetworkHandlerMixin {

    @Inject(method = "checkModList(Lnet/minecraftforge/fml/common/network/handshake/FMLHandshakeMessage$ModList;Lnet/minecraftforge/fml/relauncher/Side;)Ljava/lang/String;",
            at = @At("RETURN"),
            cancellable = true)
    private static void checkModeInfo(FMLHandshakeMessage.ModList modListPacket, Side side,
                                      CallbackInfoReturnable<String> cir) {
        if (cir.getReturnValue() != null) {
            NomiLabs.LOGGER.error("[Labs Mode Validation] Ignoring Mode Checks due to Invalid Mod List!");
            return;
        }

        String mode = ((AccessibleModListMessage) modListPacket).labs$getMode();
        if (mode.isEmpty()) {
            NomiLabs.LOGGER.error("[Labs Mode Validation] No Mode Data!");
            cir.setReturnValue(
                    "§cMode Validation Failed!§r\n\nCheck your client and server, and make sure they are on the same version!");
            return;
        }

        if (mode.equals(LabsModeHelper.getMode())) {
            NomiLabs.LOGGER.info("[Labs Mode Validation] Mode Match.");
            return;
        }

        NomiLabs.LOGGER.error("[Labs Mode Validation] Mode Mismatch! Received: {}, Expected: {}", mode,
                LabsModeHelper.getMode());

        String modeMismatchText;
        // Data coming from server
        if (side.isServer())
            modeMismatchText = String.format("Server expects mode §a%s§r, but you are on mode §e%s§r!",
                    LabsModeHelper.formatMode(mode),
                    LabsModeHelper.getFormattedMode());
        else
            modeMismatchText = String.format("Server expects mode §a%s§r, but you are on mode §e%s§r!",
                    LabsModeHelper.getFormattedMode(), LabsModeHelper.formatMode(mode));

        String linkText = "";
        if (LabsConfig.advanced.modeCheckNomiCeuLink)
            linkText = "\n\nSee §bhttps://github.com/Nomi-CEu/Nomi-CEu/blob/main/overrides/README.md§r for information on switching modes!";

        cir.setReturnValue("§cServer Mode Rejection:§r" + "\n\n" + modeMismatchText + linkText);
    }
}
