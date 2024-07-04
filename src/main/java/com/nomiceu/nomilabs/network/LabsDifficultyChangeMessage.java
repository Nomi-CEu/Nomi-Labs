package com.nomiceu.nomilabs.network;

import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.mixinhelper.DifficultySettableServer;

import io.netty.buffer.ByteBuf;

/**
 * Allows for the Gui Options to properly change and save difficulty changes to all worlds.
 */
public class LabsDifficultyChangeMessage implements IMessage {

    private int difficultyId;

    public LabsDifficultyChangeMessage() {
        difficultyId = EnumDifficulty.NORMAL.getId();
    }

    public LabsDifficultyChangeMessage(int difficultyId) {
        this.difficultyId = difficultyId;
    }

    public EnumDifficulty getDifficulty() {
        return EnumDifficulty.byId(difficultyId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        difficultyId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(difficultyId);
    }

    public static class MessageHandler implements IMessageHandler<LabsDifficultyChangeMessage, IMessage> {

        @Override
        public IMessage onMessage(LabsDifficultyChangeMessage message, MessageContext ctx) {
            if (!ctx.side.isServer()) return null;

            NomiLabs.LOGGER.debug(
                    "[Difficulty Change Handler] Received LabsDifficultyChangeMessage with difficulty {}",
                    message.getDifficulty().getId());

            var server = ctx.getServerHandler().server;
            if (server instanceof DifficultySettableServer diff)
                diff.setDifficultyForAllWorldsAndSave(message.getDifficulty());
            else server.setDifficultyForAllWorlds(message.getDifficulty());

            return null;
        }
    }
}
