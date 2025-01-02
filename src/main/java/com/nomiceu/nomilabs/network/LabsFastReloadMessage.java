package com.nomiceu.nomilabs.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.nomiceu.nomilabs.integration.jei.SavedJEIValues;

import io.netty.buffer.ByteBuf;
import mezz.jei.Internal;

public class LabsFastReloadMessage implements IMessage {

    public LabsFastReloadMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler extends MainThreadMessageHandler<LabsFastReloadMessage, IMessage> {

        @Override
        protected IMessage executeClient(LabsFastReloadMessage message, MessageContext ctx) {
            // Save existing JEI values
            SavedJEIValues.savedFilter = Internal.getIngredientFilter();

            // noinspection UnstableApiUsage
            ReloadableRegistryManager.reloadJei(true);

            SavedJEIValues.savedFilter = null;

            GroovyScript.postScriptRunResult(Minecraft.getMinecraft().player, true, true, true, 0);
            return null;
        }
    }
}
