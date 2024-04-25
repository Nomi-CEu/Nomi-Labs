package com.nomiceu.nomilabs.integration.ftbutilities;

import static com.feed_the_beast.ftbutilities.data.ClaimedChunks.instance;
import static com.nomiceu.nomilabs.integration.ftbutilities.network.CanEditChunkDataMessage.*;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;

import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.ftbutilities.network.CanEditChunkDataMessage;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;

public class CanEditChunkDataMessageHelper {

    public static void sendMessageToAll() {
        if (!ClaimedChunks.isActive()) return;

        var server = getUniverseServer();
        if (server == null) return;

        for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
            sendMessage(player);
        }
    }

    public static void sendMessage(EntityPlayerMP player) {
        // Same as dividing by 16, but faster
        int chunkX = MathHelper.floor(player.posX) >> 4;
        int chunkZ = MathHelper.floor(player.posZ) >> 4;

        int startX = chunkX - STORAGE_WIDTH / 2;
        int startZ = chunkZ - STORAGE_WIDTH / 2;

        int dim = player.world.provider.getDimension();

        boolean[] storage = new boolean[STORAGE_LENGTH];

        for (int x = 0; x < STORAGE_WIDTH; x++) {
            for (int z = 0; z < STORAGE_WIDTH; z++) {
                storage[x * STORAGE_WIDTH + z] = !playerCanEdit(startX + x, startZ + z, dim, player);
            }
        }

        LabsNetworkHandler.NETWORK_HANDLER.sendTo(new CanEditChunkDataMessage(startX, startZ, storage), player);
        NomiLabs.LOGGER.debug("[FTB Utils Integration] Sent CanEditChunkDataMessage to Player {} ({})",
                player.getUniqueID(), player.getName());
    }

    /**
     * Reflection to see whether a chunk is editable by the player.
     * Equal to `getChunk(new ChunkDimPos(posX, posZ, dim)).getTeam().hasStatus(instance.universe.getPlayer(player),
     * chunk.getData().getAttackEntitiesStatus())`
     */
    private static boolean playerCanEdit(int posX, int posZ, int dim, EntityPlayerMP player) {
        var chunk = getChunk(posX, posZ, dim);
        var forgePlayer = getUniversePlayer(player);
        if (chunk == null || forgePlayer == null) return true;

        try {
            var getTeamMethod = chunk.getClass().getDeclaredMethod("getTeam");
            var team = getTeamMethod.invoke(chunk);

            var getDataMethod = chunk.getClass().getDeclaredMethod("getData");
            var data = getDataMethod.invoke(chunk);

            var getStatusMethod = data.getClass().getDeclaredMethod("getEditBlocksStatus");
            var status = getStatusMethod.invoke(data);

            var hasStatusMethod = team.getClass().getDeclaredMethod("hasStatus", forgePlayer.getClass(),
                    status.getClass());
            return (Boolean) hasStatusMethod.invoke(team, forgePlayer, status);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal(
                    "[FTB Utils Integration] Failed to Check whether Player {} can edit at posX {}, posZ {}, dim {}! See Exception Below!",
                    player.getUniqueID(), posX, posZ, dim);
            NomiLabs.LOGGER.throwing(Level.FATAL, e);
            return true;
        }
    }

    /**
     * Reflection to Grab 'ForgePlayer' From Universe. Done to not use FTBLib. (Breaks Compiling)
     * Equal to `instance.universe.getPlayer(player)`
     */
    @Nullable
    private static Object getUniversePlayer(EntityPlayerMP player) {
        var universe = getUniverse();
        if (universe == null) return null;
        try {
            var forgePlayerMethod = universe.getClass().getDeclaredMethod("getPlayer", UUID.class);
            return forgePlayerMethod.invoke(universe, player.getUniqueID());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("[FTB Utils Integration] Failed to get Universe Player! See Exception Below!");
            NomiLabs.LOGGER.throwing(Level.FATAL, e);
            return null;
        }
    }

    /**
     * Reflection to Grab Universe Server. Done to not use FTBLib. (Breaks Compiling)
     * Equal to `instance.universe.server`
     */
    @Nullable
    private static MinecraftServer getUniverseServer() {
        var universe = getUniverse();
        if (universe == null) return null;

        try {
            var serverField = universe.getClass().getDeclaredField("server");
            return (MinecraftServer) serverField.get(universe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("[FTB Utils Integration] Failed to get Universe Server! See Exception Below!");
            NomiLabs.LOGGER.throwing(Level.FATAL, e);
            return null;
        }
    }

    /**
     * Reflection to Grab Universe. Done to not use FTBLib. (Breaks Compiling)
     * Equal to `instance.universe`
     */
    @Nullable
    public static Object getUniverse() {
        try {
            var instance = ClaimedChunks.instance;
            var universeField = instance.getClass().getDeclaredField("universe");
            return universeField.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("[FTB Utils Integration] Failed to get Universe! See Exception Below!");
            NomiLabs.LOGGER.throwing(Level.FATAL, e);
            return null;
        }
    }

    /**
     * Reflection to Grab Claimed Chunk. Done to not use FTBLib. (Breaks Compiling)
     * Equal to `getChunk(new ChunkDimPos(posX, posZ, dim))`
     */
    @Nullable
    private static ClaimedChunk getChunk(int posX, int posZ, int dim) {
        try {
            var chunkDimPosClass = Class.forName("com.feed_the_beast.ftblib.lib.math.ChunkDimPos");
            var constructor = chunkDimPosClass.getConstructor(int.class, int.class, int.class);
            Object obj = constructor.newInstance(posX, posZ, dim);

            var claimedChunksClass = Class.forName("com.feed_the_beast.ftbutilities.data.ClaimedChunks");
            var getChunkMethod = claimedChunksClass.getDeclaredMethod("getChunk", chunkDimPosClass);

            // noinspection JavaReflectionInvocation
            return (ClaimedChunk) getChunkMethod.invoke(instance, chunkDimPosClass.cast(obj));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            NomiLabs.LOGGER.fatal(
                    "[FTB Utils Integration] Failed to get Chunk with posX {}, posZ {}, dim {}! See Exception Below!",
                    posX,
                    posZ, dim);
            NomiLabs.LOGGER.throwing(Level.FATAL, e);
            return null;
        }
    }
}
