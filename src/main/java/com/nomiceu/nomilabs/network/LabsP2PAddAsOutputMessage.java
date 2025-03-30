package com.nomiceu.nomilabs.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGridServerCache;
import com.projecturanus.betterp2p.network.ModNetwork;
import com.projecturanus.betterp2p.network.PlayerRequest;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.network.data.P2PLocationKt;

import io.netty.buffer.ByteBuf;

public class LabsP2PAddAsOutputMessage implements IMessage {

    private P2PLocation location;
    private short sourceFrequency;

    public LabsP2PAddAsOutputMessage() {
        location = null;
        sourceFrequency = 0;
    }

    public LabsP2PAddAsOutputMessage(P2PLocation location, short sourceFrequency) {
        this.location = location;
        this.sourceFrequency = sourceFrequency;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = P2PLocationKt.readP2PLocation(buf);
        sourceFrequency = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        P2PLocationKt.writeP2PLocation(buf, location);
        buf.writeShort(sourceFrequency);
    }

    public static class MessageHandler implements IMessageHandler<LabsP2PAddAsOutputMessage, IMessage> {

        @Override
        public IMessage onMessage(LabsP2PAddAsOutputMessage message, MessageContext ctx) {
            if (!ctx.side.isServer()) return null;

            PlayerRequest state = ModNetwork.INSTANCE.getPlayerState().get(ctx.getServerHandler().player.getUniqueID());
            if (state == null) return null;

            ctx.getServerHandler().server.addScheduledTask(() -> {
                boolean result = ((AccessibleGridServerCache) (Object) state.getGridCache())
                        .labs$addOutput(message.location, message.sourceFrequency);

                if (result) {
                    ModNetwork.INSTANCE.requestP2PUpdate(ctx.getServerHandler().player);
                }
            });
            return null;
        }
    }
}
