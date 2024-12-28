package com.nomiceu.nomilabs.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGridServerCache;
import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.PlayerRequest;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.network.data.P2PLocationKt;

import appeng.parts.p2p.PartP2PTunnel;
import io.netty.buffer.ByteBuf;

public class LabsP2PChangeTypeMessage implements IMessage {

    private P2PLocation location;
    private boolean isInput;

    public LabsP2PChangeTypeMessage() {
        location = null;
        isInput = true;
    }

    public LabsP2PChangeTypeMessage(P2PLocation location, boolean isInput) {
        this.location = location;
        this.isInput = isInput;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = P2PLocationKt.readP2PLocation(buf);
        isInput = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        P2PLocationKt.writeP2PLocation(buf, location);
        buf.writeBoolean(isInput);
    }

    public static class MessageHandler implements IMessageHandler<LabsP2PChangeTypeMessage, IMessage> {

        @Override
        public IMessage onMessage(LabsP2PChangeTypeMessage message, MessageContext ctx) {
            if (!ctx.side.isServer()) return null;

            PlayerRequest state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());
            if (state == null) return null;

            ctx.getServerHandler().server.addScheduledTask(() -> {
                PartP2PTunnel<?> result = ((AccessibleGridServerCache) (Object) state.getGridCache())
                        .labs$changeIsInput(message.location, message.isInput);
                if (result != null) {
                    ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
                }
            });
            return null;
        }
    }
}
