package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.PlayerRequest;
import com.projecturanus.betterp2p.network.packet.*;

/**
 * Fixes an edge case CME.
 */
@Mixin(value = ServerUnlinkP2PHandler.class, remap = false)
public class ServerUnlinkP2PHandlerMixin {

    @Inject(method = "onMessage(Lcom/projecturanus/betterp2p/network/packet/C2SUnlinkP2P;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lcom/projecturanus/betterp2p/network/packet/S2COpenGui;",
            at = @At("HEAD"),
            cancellable = true)
    private void newMsgHandling(C2SUnlinkP2P message, MessageContext ctx, CallbackInfoReturnable<S2COpenGui> cir) {
        cir.setReturnValue(null);

        if (!ctx.side.isServer()) return;
        if (message.getP2p() == null) return;

        PlayerRequest state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());
        if (state == null) return;

        ctx.getServerHandler().server.addScheduledTask(() -> {
            state.getGridCache().unlinkP2P(message.getP2p());

            ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
        });
    }
}
