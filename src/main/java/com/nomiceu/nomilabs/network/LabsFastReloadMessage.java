package com.nomiceu.nomilabs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.core.mixin.jei.JeiProxyAccessor;
import com.cleanroommc.groovyscript.sandbox.LoadStage;

import io.netty.buffer.ByteBuf;
import mezz.jei.Internal;
import mezz.jei.JustEnoughItems;
import sonar.core.integration.jei.JEISonarPlugin;

public class LabsFastReloadMessage implements IMessage {

    public LabsFastReloadMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler extends MainThreadMessageHandler<LabsFastReloadMessage, IMessage> {

        @Override
        protected IMessage executeClient(LabsFastReloadMessage message, MessageContext ctx) {
            // noinspection UnstableApiUsage
            long timeReload = GroovyScript.runGroovyScriptsInLoader(LoadStage.POST_INIT);

            // Copied from GrS Reload Code, but with the change of `start` to `load` (with recipeOnly true)
            JeiProxyAccessor jeiProxy = (JeiProxyAccessor) JustEnoughItems.getProxy();
            long time = System.currentTimeMillis();

            // Sonar Core adds its categories to JEISonarPlugin#providers every time JeiStarter#start() is called
            // So, to prevent duplicate categories, we need to clear the List before running.
            if (Loader.isModLoaded("sonarcore")) {
                jeiProxy.getPlugins().forEach(plugin -> {
                    if (plugin instanceof JEISonarPlugin) ((JEISonarPlugin) plugin).providers.clear();
                });
            }

            jeiProxy.getStarter().load(jeiProxy.getPlugins(), jeiProxy.getTextures(), true);
            time = System.currentTimeMillis() - time;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Reloading JEI took " + time + "ms"));

            Internal.getIngredientFilter().block();

            GroovyScript.postScriptRunResult(Minecraft.getMinecraft().player, true, true, true, timeReload);
            return null;
        }
    }
}
