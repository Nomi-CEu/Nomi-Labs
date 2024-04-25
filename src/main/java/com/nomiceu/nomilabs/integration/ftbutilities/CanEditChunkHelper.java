package com.nomiceu.nomilabs.integration.ftbutilities;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import com.feed_the_beast.ftbutilities.FTBUtilitiesPermissions;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.nomiceu.nomilabs.NomiLabs;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CanEditChunkHelper {

    private static final Map<ChunkPos, Boolean> cannotEditChunks = new Object2ObjectOpenHashMap<>();

    public static void clear() {
        cannotEditChunks.clear();
    }

    public static void addEntries(boolean[][] entries, int startX, int startZ) {
        for (int x = 0; x < entries.length; x++) {
            for (int z = 0; z < entries[x].length; z++) {
                cannotEditChunks.put(new ChunkPos(startX + x, startZ + z), entries[x][z]);
            }
        }
        NomiLabs.LOGGER.debug("[FTB Utils Integration] Chunk Editing Permissions: {}",
                cannotEditChunks.entrySet().stream()
                        .sorted(Comparator
                                .comparingInt((Map.Entry<ChunkPos, Boolean> entry) -> entry.getKey().x)
                                .thenComparingInt(entry -> entry.getKey().z))
                        .map((entry) -> "chunk: x: " + entry.getKey().x + ", z: " +
                                entry.getKey().z + " | " + entry.getValue())
                        .collect(Collectors.toList()));
    }

    public static boolean cannotEditChunk(EntityPlayer player, BlockPos pos, IBlockState state) {
        if (player.world == null) return false;

        var chunkPos = new ChunkPos(pos);
        if ((player instanceof EntityPlayerMP && ClaimedChunks.isActive()) || cannotEditChunks.isEmpty() ||
                !cannotEditChunks.containsKey(chunkPos))
            return ClaimedChunks.blockBlockEditing(player, pos, state);

        if (state == null) {
            state = player.world.getBlockState(pos);
        }

        return !FTBUtilitiesPermissions.hasBlockEditingPermission(player, state.getBlock()) &&
                cannotEditChunks.get(chunkPos);
    }
}
