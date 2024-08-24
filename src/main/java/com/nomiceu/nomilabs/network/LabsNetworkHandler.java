package com.nomiceu.nomilabs.network;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.ftbutilities.network.CanEditChunkDataMessage;

public class LabsNetworkHandler {

    public static SimpleNetworkWrapper NETWORK_HANDLER;

    public static void onConstruction() {
        NETWORK_HANDLER = new SimpleNetworkWrapper(LabsValues.LABS_MODID);
    }

    public static void preInit() {
        registerMessage(0, CanEditChunkDataMessage.MessageHandler.class, CanEditChunkDataMessage.class);
        registerMessage(1, LabsDifficultyChangeMessage.MessageHandler.class, LabsDifficultyChangeMessage.class);

        if (Loader.isModLoaded(LabsValues.AE2_MODID))
            registerMessage(2, LabsP2PCycleMessage.MessageHandler.class, LabsP2PCycleMessage.class);

        if (Loader.isModLoaded(LabsValues.BETTER_P2P_MODID)) {
            registerMessage(3, LabsP2PChangeTypeMessage.MessageHandler.class, LabsP2PChangeTypeMessage.class);
            registerMessage(4, LabsP2PAddAsInputMessage.MessageHandler.class, LabsP2PAddAsInputMessage.class);
            registerMessage(5, LabsP2PAddAsOutputMessage.MessageHandler.class, LabsP2PAddAsOutputMessage.class);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static <REQ extends IMessage,
            REPLY extends IMessage> void registerMessage(int id,
                                                         Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
                                                         Class<REQ> requestMessageType) {
        NETWORK_HANDLER.registerMessage(messageHandler, requestMessageType, id, Side.CLIENT);
        NETWORK_HANDLER.registerMessage(messageHandler, requestMessageType, id, Side.SERVER);
    }
}
