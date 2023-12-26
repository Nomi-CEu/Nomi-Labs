package com.nomiceu.nomilabs.remap.datafixer.walker;

import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import org.jetbrains.annotations.NotNull;

public class BlockEntityWalker implements IDataWalker {
    private final ItemStackWalker itemWalker;
    public BlockEntityWalker(ItemStackWalker itemWalker) {
        this.itemWalker = itemWalker;
    }

    @Override
    @NotNull
    public NBTTagCompound process(@NotNull IDataFixer fixer, @NotNull NBTTagCompound compound, int versionIn) {
        if (DataFixerHandler.fixNotAvailable()) return compound;

        itemWalker.process(fixer, compound, versionIn);
        if (!DataFixerHandler.neededFixes.containsKey(LabsFixTypes.FixerTypes.TILE_ENTITY)) return compound;
        return fixer.process(LabsFixTypes.FixerTypes.TILE_ENTITY, compound, versionIn);
    }
}
