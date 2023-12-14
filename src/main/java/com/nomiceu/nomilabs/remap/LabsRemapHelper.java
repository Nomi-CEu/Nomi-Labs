package com.nomiceu.nomilabs.remap;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.storage.CompoundRewriter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.ZipperUtil;
import net.minecraftforge.common.util.Constants;

public class LabsRemapHelper {
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
            var i = childTag.getId();
            i = i;
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
            var j = childTag.getId();
            j = j;
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

    /*
    public static void rewriteBlocks(NBTTagCompound chunkSectionTag, BlockRewriter rewriter) {
        byte[] blockIds = chunkSectionTag.getByteArray("Blocks");
        NibbleArray blockData = new NibbleArray(chunkSectionTag.getByteArray("Data"));
        NibbleArray extendedIds = chunkSectionTag.hasKey("Add", Constants.NBT.TAG_BYTE_ARRAY)
                ? new NibbleArray(chunkSectionTag.getByteArray("Add")) : null;
        for (int i = 0; i < 4096; ++i) {
            int x = i & 0x0F, y = i >> 8 & 0x0F, z = i >> 4 & 0x0F;
            int id = extendedIds == null ? (blockIds[i] & 0xFF)
                    : ((blockIds[i] & 0xFF) | (extendedIds.get(x, y, z) << 8));
            RemappedBlock remapped = rewriter.rewrite(id, (short) blockData.get(x, y, z));
            if (remapped != null) {
                blockIds[i] = (byte) (remapped.id() & 0xFF);
                int idExt = (remapped.id() >> 8) & 0x0F;
                if (idExt != 0) {
                    if (extendedIds == null) {
                        extendedIds = new NibbleArray();
                    }
                    extendedIds.set(x, y, z, idExt);
                }
                blockData.set(x, y, z, remapped.data() & 0x0F);
            }
        }
        if (extendedIds != null) {
            chunkSectionTag.setByteArray("Add", extendedIds.getData());
        }
    }
    TODO
     */

    public static void abort() {
        DataFixerHandler.close();
        StartupQuery.abort();
    }
}
