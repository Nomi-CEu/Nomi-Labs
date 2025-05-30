package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.projecturanus.betterp2p.BetterP2P;
import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.PlayerRequest;
import com.projecturanus.betterp2p.network.packet.*;
import com.projecturanus.betterp2p.util.p2p.TunnelInfo;

/**
 * Fixes an edge case CME.
 */
@Mixin(value = ServerTypeChangeHandler.class, remap = false)
public class ServerTypeChangeHandlerMixin {

    @Inject(method = "onMessage(Lcom/projecturanus/betterp2p/network/packet/C2STypeChange;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lcom/projecturanus/betterp2p/network/packet/S2COpenGui;",
            at = @At("HEAD"),
            cancellable = true)
    private void newMsgHandling(C2STypeChange message, MessageContext ctx, CallbackInfoReturnable<S2COpenGui> cir) {
        cir.setReturnValue(null);

        if (!ctx.side.isServer()) return;
        if (message.getP2p() == null) return;

        PlayerRequest state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());
        TunnelInfo type = BetterP2P.proxy.getP2PFromIndex(message.getNewType());
        if (state == null || type == null) return;

        ctx.getServerHandler().server.addScheduledTask(() -> {
            var result = state.getGridCache().changeAllP2Ps(message.getP2p(), type);

            if (result) {
                ModNetwork.INSTANCE.requestP2PList(ctx.getServerHandler().player, type.getIndex());
            }
        });
    }
}
