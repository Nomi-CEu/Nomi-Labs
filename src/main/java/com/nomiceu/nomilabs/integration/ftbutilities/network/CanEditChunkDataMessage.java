package com.nomiceu.nomilabs.integration.ftbutilities.network;

import java.util.Arrays;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.ftbutilities.CanEditChunkHelper;

import io.netty.buffer.ByteBuf;

public class CanEditChunkDataMessage implements IMessage {

    private int startX;
    private int startZ;
    private int storage;

    // Should be a square number. Should be less than or equal to 31.
    public static final int STORAGE_LENGTH = 25;
    // Should be the sqrt of the above. Should not be divisible by 2.
    public static final int STORAGE_WIDTH = 5;

    public CanEditChunkDataMessage() {
        startX = 0;
        startZ = 0;
        storage = 0;
    }

    /**
     * Create a new Can Edit Chunk Data Message.
     * 
     * @param startX    X Loc of the Chunk (Not Block Pos)
     * @param startZ    Z Loc of the Chunk (Not Block Pos)
     * @param chunkInfo Boolean list of chunk info (0, top left, 1, right of that, etc.) true = no edit allowed, false =
     *                  edit allowed
     */
    public CanEditChunkDataMessage(int startX, int startZ, boolean[] chunkInfo) {
        if (chunkInfo.length != STORAGE_LENGTH) throw new RuntimeException(
                "[FTB Utils Integration] Chunk Info cannot have length that is not " + STORAGE_LENGTH + "!");
        this.startX = startX;
        this.startZ = startZ;
        this.storage = 0;

        NomiLabs.LOGGER.debug("[FTB Utils Integration] Serializing Chunk Info {}...", chunkInfo);

        // Encode Boolean List into one integer.
        // Using Bit Manipulation. Each storage is a bit.
        for (int i = 0; i < chunkInfo.length; i++) {
            if (chunkInfo[i]) storage += 1 << i; // 1 Here represents a Boolean Value of True
        }

        NomiLabs.LOGGER.debug(
                "[FTB Utils Integration] Made CanEditChunkDataMessage with Storage {} with start x {} and start z {}",
                storage, startX, startZ);
    }

    public boolean[][] getChunkInfo() {
        NomiLabs.LOGGER.debug("[FTB Utils Integration] Decoding Storage {}...", storage);
        // Decode Storage (More Bit Manipulation)
        boolean[][] chunkInfo = new boolean[STORAGE_WIDTH][STORAGE_WIDTH];
        for (int x = 0; x < STORAGE_WIDTH; x++) {
            for (int z = 0; z < STORAGE_WIDTH; z++) {
                // Shift Storage by position in List, then mask with 1
                chunkInfo[x][z] = ((storage >> x * STORAGE_WIDTH + z) & 1) != 0;
            }
        }
        NomiLabs.LOGGER.debug("[FTB Utils Integration] Decoded Storage into Chunk Info: {}",
                Arrays.deepToString(chunkInfo));
        return chunkInfo;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartZ() {
        return startZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        storage = buf.readInt();
        startX = buf.readInt();
        startZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(storage);
        buf.writeInt(startX);
        buf.writeInt(startZ);
    }

    public static class MessageHandler implements IMessageHandler<CanEditChunkDataMessage, IMessage> {

        @Override
        public IMessage onMessage(CanEditChunkDataMessage message, MessageContext ctx) {
            if (!ctx.side.isClient()) return null;

            NomiLabs.LOGGER.debug(
                    "[FTB Utils Integration] Received CanEditChunkDataMessage with Storage {} with start x {} and start z {}",
                    message.storage, message.startX, message.startZ);
            CanEditChunkHelper.clear();
            CanEditChunkHelper.addEntries(message.getChunkInfo(), message.getStartX(), message.getStartZ());
            return null;
        }
    }
}
