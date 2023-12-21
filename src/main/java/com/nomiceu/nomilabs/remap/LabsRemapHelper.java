package com.nomiceu.nomilabs.remap;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.storage.CompoundRewriter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

    public static void abort() {
        DataFixerHandler.close();
        StartupQuery.abort();
    }
}
