package com.nomiceu.nomilabs.network;

import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MainThreadMessageHandler<REQ extends IMessage, REPLY extends IMessage>
                                              implements IMessageHandler<REQ, REPLY> {

    protected abstract REPLY executeClient(REQ message, MessageContext ctx);

    @Override
    public REPLY onMessage(REQ message, MessageContext ctx) {
        if (!ctx.side.isClient()) return null;

        IThreadListener threadListener = FMLCommonHandler.instance().getWorldThread(ctx.getClientHandler());
        if (threadListener.isCallingFromMinecraftThread()) {
            return executeClient(message, ctx);
        } else {
            threadListener.addScheduledTask(() -> executeClient(message, ctx));
        }
        return null;
    }
}
