package com.nomiceu.nomilabs.remap;

import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.ZipperUtil;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsVersionConfig;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.BlockRewriter;
import com.nomiceu.nomilabs.remap.datafixer.storage.BlockStateLike;
import com.nomiceu.nomilabs.remap.datafixer.storage.CompoundRewriter;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

public class LabsRemapHelper {

    public static final Pattern META_ITEM_MATCHER = Pattern.compile("meta_.+");
    public static final Pattern META_BLOCK_MATCHER = Pattern.compile("meta_block_.+_\\d+");
    public static final int MIN_META_ITEM_BASE_ID = 32000; // The Base ID where the Old Meta Items/Materials started
                                                           // from
    public static final int MIN_META_BLOCK_BASE_ID = 2000; // The Base ID where the Old Meta Blocks started from

    private static Map<BlockPos, NBTTagCompound> posToTileEntityCache;

    public static void createWorldBackup() {
        try {
            NomiLabs.LOGGER.info("Creating world backup...");
            ZipperUtil.backupWorld();
        } catch (Exception e) {
            abort();
            NomiLabs.LOGGER.error("Error creating backup. Closing...");
            NomiLabs.LOGGER.throwing(e);
        }
    }

    public static void rewriteCompoundTags(NBTTagCompound tag, CompoundRewriter rewriter) {
        for (String key : tag.getKeySet()) {
            NBTBase childTag = tag.getTag(key);
            switch (childTag.getId()) {
                case Constants.NBT.TAG_LIST -> rewriteCompoundTags((NBTTagList) childTag, rewriter);
                case Constants.NBT.TAG_COMPOUND -> {
                    NBTTagCompound childTagCompound = (NBTTagCompound) childTag;
                    rewriteCompoundTags(childTagCompound, rewriter);
                    childTagCompound = rewriter.rewrite(childTagCompound);
                    if (childTagCompound != null) {
                        tag.setTag(key, childTagCompound);
                    }
                }
            }
        }
    }

    public static void rewriteCompoundTags(NBTTagList tag, CompoundRewriter rewriter) {
        for (int i = 0; i < tag.tagCount(); i++) {
            NBTBase childTag = tag.get(i);
            switch (childTag.getId()) {
                case Constants.NBT.TAG_LIST -> rewriteCompoundTags((NBTTagList) childTag, rewriter);
                case Constants.NBT.TAG_COMPOUND -> {
                    NBTTagCompound childTagCompound = (NBTTagCompound) childTag;
                    rewriteCompoundTags(childTagCompound, rewriter);
                    childTagCompound = rewriter.rewrite(childTagCompound);
                    if (childTagCompound != null) {
                        tag.set(i, childTagCompound);
                    }
                }
            }
        }
    }

    public static void rewriteBlocks(NBTTagCompound compound, BlockRewriter rewriter) {
        clearTECache();
        NBTTagCompound levelTag = compound.getCompoundTag("Level");
        int x = levelTag.getInteger("xPos");
        int z = levelTag.getInteger("zPos");
        NBTTagList sectionListTag = levelTag.getTagList("Sections", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < sectionListTag.tagCount(); i++) {
            sectionListTag.set(i, rewriteBlocksInSection(sectionListTag.getCompoundTagAt(i), x, z, rewriter));
        }
        if (posToTileEntityCache != null) {
            var tagList = new NBTTagList();
            posToTileEntityCache.values().forEach(tagList::appendTag);
            levelTag.setTag("TileEntities", tagList);
        }
    }

    private static NBTTagCompound rewriteBlocksInSection(NBTTagCompound chunkSectionTag, int chunkX, int chunkZ,
                                                         BlockRewriter rewriter) {
        byte[] blockIds = chunkSectionTag.getByteArray("Blocks");
        int chunkY = chunkSectionTag.getInteger("Y");
        NibbleArray blockMetadata = new NibbleArray(chunkSectionTag.getByteArray("Data"));
        NibbleArray extendedIds = chunkSectionTag.hasKey("Add", Constants.NBT.TAG_BYTE_ARRAY) ?
                new NibbleArray(chunkSectionTag.getByteArray("Add")) : null;
        for (int i = 0; i < 4096; ++i) {
            int x = i & 0x0F, y = i >> 8 & 0x0F, z = i >> 4 & 0x0F;

            // This is based off BlockStateContainer's setDataFromNBT
            // There, the block id is shifted by 4, and extended is shifted by 12
            // However, that is to allow the accommodation of 4 bits of metadata info
            // Thus, here, the block id is not shifted, and extended is only shifted by 8.
            int id = extendedIds == null ? (blockIds[i] & 0xFF) :
                    ((blockIds[i] & 0xFF) | (extendedIds.get(x, y, z) << 8));
            var state = new BlockStateLike(id, (short) blockMetadata.get(x, y, z),
                    new BlockPos(chunkX * 16 + x, chunkY * 16 + y, chunkZ * 16 + z));
            if (state.invalid) continue;
            BlockStateLike remapped = rewriter.rewrite(state);
            if (remapped != null) {
                blockIds[i] = (byte) (remapped.getId() & 0xFF);
                int idExt = (remapped.getId() >> 8) & 0x0F;
                if (idExt != 0) {
                    if (extendedIds == null) {
                        extendedIds = new NibbleArray();
                    }
                    extendedIds.set(x, y, z, idExt);
                }
                blockMetadata.set(x, y, z, remapped.meta & 0x0F);

                if (posToTileEntityCache != null && remapped.tileEntityTag != null)
                    posToTileEntityCache.put(remapped.pos, remapped.tileEntityTag);
            }
        }
        if (extendedIds != null) {
            chunkSectionTag.setByteArray("Add", extendedIds.getData());
        }
        return chunkSectionTag;
    }

    public static boolean tagHasItemInfo(NBTTagCompound tag) {
        return tag.hasKey("id", Constants.NBT.TAG_STRING) && tag.hasKey("Count", Constants.NBT.TAG_ANY_NUMERIC) &&
                tag.hasKey("Damage", Constants.NBT.TAG_ANY_NUMERIC);
    }

    public static void clearTECache() {
        posToTileEntityCache = null;
    }

    /**
     * Call Clear TE Cache every time doing this on a new section!
     * <br>
     * {@link LabsRemapHelper#rewriteBlocks(NBTTagCompound, BlockRewriter)} does this for you!
     */
    public static Map<BlockPos, NBTTagCompound> getPosToTileEntityMap(NBTTagCompound chunkTag) {
        if (posToTileEntityCache != null) return posToTileEntityCache;
        posToTileEntityCache = new Object2ObjectLinkedOpenHashMap<>();
        NBTTagCompound levelTag = chunkTag.getCompoundTag("Level");
        NBTTagList tileEntities = levelTag.getTagList("TileEntities", Constants.NBT.TAG_COMPOUND); // Returns empty if
                                                                                                   // doesn't exist
        for (var te : tileEntities) {
            var tag = (NBTTagCompound) te;
            posToTileEntityCache.put(
                    new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")), tag);
        }
        return posToTileEntityCache;
    }

    /**
     * This method gets the version which should be reported by all Labs Data Fixers.
     * <p>
     * This combines the internal version with the config version, allowing for forced re-application.
     */
    public static int getReportedVersion() {
        return LabsFixes.CURRENT + LabsVersionConfig.manualFixVersion;
    }

    public static void abort() {
        DataFixerHandler.close();
        StartupQuery.abort();
    }
}
