package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.data.P2PLocationKt;
import com.projecturanus.betterp2p.network.packet.C2SRenameP2P;
import com.projecturanus.betterp2p.network.packet.ServerRenameP2PTunnel;

import appeng.api.parts.IPart;
import appeng.me.GridAccessException;
import appeng.parts.p2p.PartP2PTunnel;

/**
 * Properly handles inputs and unbound P2Ps in renaming.
 */
@Mixin(value = ServerRenameP2PTunnel.class, remap = false)
public class ServerRenameP2PTunnelMixin {

    @Inject(method = "onMessage(Lcom/projecturanus/betterp2p/network/packet/C2SRenameP2P;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            at = @At(value = "INVOKE",
                     target = "Lappeng/parts/p2p/PartP2PTunnel;setCustomName(Ljava/lang/String;)V",
                     shift = At.Shift.AFTER),
            require = 1,
            cancellable = true)
    private void properlyHandleInputs(C2SRenameP2P message, MessageContext ctx, CallbackInfoReturnable<IMessage> cir,
                                      @Local(ordinal = 0) IPart partTunnel) {
        var tunnel = (PartP2PTunnel<?>) partTunnel;
        var state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());

        try {
            if (tunnel.getInputs().isEmpty() && tunnel.getOutputs().isEmpty()) {
                state.getGridCache().markDirty(P2PLocationKt.toLoc(tunnel), tunnel);
                ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
                cir.setReturnValue(null);
                return;
            }

            for (var input : tunnel.getInputs()) {
                // Mark all dirty
                for (var output : input.getOutputs()) {
                    var outputTunnel = (PartP2PTunnel<?>) output;
                    state.getGridCache().markDirty(P2PLocationKt.toLoc(outputTunnel), outputTunnel);
                }
                state.getGridCache().markDirty(P2PLocationKt.toLoc(input), input);
            }

            ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
            cir.setReturnValue(null);
        } catch (GridAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
