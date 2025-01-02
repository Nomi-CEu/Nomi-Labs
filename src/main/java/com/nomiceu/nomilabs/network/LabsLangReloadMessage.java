package com.nomiceu.nomilabs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;

import io.netty.buffer.ByteBuf;

public class LabsLangReloadMessage implements IMessage {

    public LabsLangReloadMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler extends MainThreadMessageHandler<LabsLangReloadMessage, IMessage> {

        @Override
        protected IMessage executeClient(LabsLangReloadMessage message, MessageContext ctx) {
            // Reload Textures, but ONLY Lang
            long time = System.currentTimeMillis();
            FMLClientHandler.instance().refreshResources(VanillaResourceType.LANGUAGES);
            time = System.currentTimeMillis() - time;
            Minecraft.getMinecraft().player.sendMessage(
                    new TextComponentString("Reloading Language Resources took " + time + "ms"));

            // noinspection UnstableApiUsage
            ReloadableRegistryManager.reloadJei(true);

            GroovyScript.postScriptRunResult(Minecraft.getMinecraft().player, true, true, true, 0);
            return null;
        }
    }
}
