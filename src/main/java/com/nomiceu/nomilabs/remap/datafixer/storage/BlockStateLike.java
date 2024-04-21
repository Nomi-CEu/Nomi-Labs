package com.nomiceu.nomilabs.remap.datafixer.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;

/**
 * Essentially an IBlockState, but contains ResourceLocation instead of Block. Contains meta, but no other info.
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class BlockStateLike {

    public ResourceLocation rl;
    public short meta;
    public boolean invalid;
    public final BlockPos pos;
    @Nullable
    public NBTTagCompound tileEntityTag;

    public BlockStateLike(int id, short meta, BlockPos pos) {
        this.pos = pos;
        this.rl = DataFixerHandler.getBlockHelperMap().getOrDefault(id, null);
        this.invalid = this.rl == null;
        this.meta = (short) Math.max(0, meta);
    }

    private BlockStateLike(ResourceLocation rl, short meta, boolean invalid, BlockPos pos,
                           @Nullable NBTTagCompound tileEntityTag) {
        this.rl = rl;
        this.meta = meta;
        this.invalid = invalid;
        this.pos = pos;
        this.tileEntityTag = tileEntityTag;
    }

    public BlockStateLike setRl(ResourceLocation newRl) {
        rl = newRl;
        return this;
    }

    public BlockStateLike setMeta(short newMeta) {
        meta = newMeta;
        return this;
    }

    public BlockStateLike setTileEntityTag(@Nonnull NBTTagCompound tag) {
        tileEntityTag = tag;
        return this;
    }

    public int getId() {
        return rl == null ? -1 : DataFixerHandler.getBlockHelperMap().inverse().getOrDefault(rl, 0);
    }

    public BlockStateLike copy() {
        return new BlockStateLike(rl, meta, invalid, pos, tileEntityTag != null ? tileEntityTag.copy() : null);
    }
}
