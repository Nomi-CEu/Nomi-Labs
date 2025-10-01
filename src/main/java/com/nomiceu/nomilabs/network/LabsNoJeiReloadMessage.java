package com.nomiceu.nomilabs.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.sandbox.LoadStage;

import io.netty.buffer.ByteBuf;

public class LabsNoJeiReloadMessage implements IMessage {

    public LabsNoJeiReloadMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler extends MainThreadMessageHandler<LabsNoJeiReloadMessage, IMessage> {

        @Override
        protected IMessage executeClient(LabsNoJeiReloadMessage message, MessageContext ctx) {
            // noinspection UnstableApiUsage
            long time = GroovyScript.runGroovyScriptsInLoader(LoadStage.POST_INIT);
            GroovyScript.postScriptRunResult(Minecraft.getMinecraft().player, true, true, true, time);
            return null;
        }
    }
}
