package com.nomiceu.nomilabs.integration.ae2;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;

import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;

public interface InclNonConsumeButtonDisplay {

    InclNonConsumableButton labs$inclNonConsumeButton();

    default void labs$inclButtonPress(GuiButton btn, InclNonConsumeSettable inventorySlots) {
        if (labs$inclNonConsumeButton() == btn) {
            labs$inclNonConsumeButton().toggle();

            // Client Sync
            inventorySlots
                    .labs$setInclNonConsume(labs$inclNonConsumeButton().isInclNonConsume());

            // Server Sync
            try {
                NetworkHandler.instance().sendToServer(
                        new PacketValueConfig("Labs$NonConsume",
                                labs$inclNonConsumeButton().isInclNonConsume() ? "1" : "0"));
            } catch (IOException e) {
                AELog.error(e);
            }
        }
    }
}
