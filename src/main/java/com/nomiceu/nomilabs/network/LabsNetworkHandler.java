package com.nomiceu.nomilabs.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.ftbutilities.network.CanEditChunkDataMessage;

public class LabsNetworkHandler {

    public static SimpleNetworkWrapper NETWORK_HANDLER;
    private static int CURRENT_ID;

    public static void onConstruction() {
        NETWORK_HANDLER = new SimpleNetworkWrapper(LabsValues.LABS_MODID);
    }

    public static void preInit() {
        CURRENT_ID = 0;
        registerMessage(CanEditChunkDataMessage.MessageHandler.class, CanEditChunkDataMessage.class);
        registerMessage(LabsDifficultyChangeMessage.MessageHandler.class, LabsDifficultyChangeMessage.class);
    }

    @SuppressWarnings("SameParameterValue")
    private static <REQ extends IMessage,
            REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
                                                         Class<REQ> requestMessageType) {
        NETWORK_HANDLER.registerMessage(messageHandler, requestMessageType, CURRENT_ID++, Side.CLIENT);
        NETWORK_HANDLER.registerMessage(messageHandler, requestMessageType, CURRENT_ID++, Side.SERVER);
    }
}
