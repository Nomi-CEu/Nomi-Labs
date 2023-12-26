package com.nomiceu.nomilabs.remap.datafixer.storage;

import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import net.minecraft.util.ResourceLocation;

/**
 * Essentially an IBlockState, but contains ResourceLocation instead of Block. Contains meta, but no other info.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BlockStateLike {
    public ResourceLocation rl;
    public short meta;

    public boolean invalid;

    public BlockStateLike(int id, short meta) {
        this.rl = DataFixerHandler.blockIdToRlMap.getOrDefault(id, null);
        this.invalid = this.rl == null;
        this.meta = (short) Math.max(0, meta);
    }

    public BlockStateLike setRl(ResourceLocation newRl) {
        rl = newRl;
        return this;
    }

    public BlockStateLike setMeta(short newMeta) {
        meta = newMeta;
        return this;
    }

    public int getId() {
        return rl == null ? -1 : LabsRemapHelper.getBlockRegistry().getID(rl);
    }
}
