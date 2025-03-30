package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.PlayerRequest;
import com.projecturanus.betterp2p.network.packet.C2SLinkP2P;
import com.projecturanus.betterp2p.network.packet.ServerLinkP2PHandler;

/**
 * Fixes an edge case CME.
 */
@Mixin(value = ServerLinkP2PHandler.class, remap = false)
public class ServerLinkP2PHandlerMixin {

    @Inject(method = "onMessage(Lcom/projecturanus/betterp2p/network/packet/C2SLinkP2P;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            at = @At("HEAD"),
            cancellable = true)
    private void newMsgHandling(C2SLinkP2P message, MessageContext ctx, CallbackInfoReturnable<IMessage> cir) {
        cir.setReturnValue(null);

        if (!ctx.side.isServer()) return;
        if (message.getInput() == null || message.getOutput() == null) return;

        PlayerRequest state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());
        if (state == null) return;

        ctx.getServerHandler().server.addScheduledTask(() -> {
            var result = state.getGridCache().linkP2P(message.getInput(), message.getOutput());

            if (result != null) {
                ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
            }
        });
    }
}
