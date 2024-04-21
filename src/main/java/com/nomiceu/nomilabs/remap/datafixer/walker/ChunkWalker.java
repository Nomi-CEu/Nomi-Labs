package com.nomiceu.nomilabs.remap.datafixer.walker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;

public class ChunkWalker implements IDataWalker {

    @Override
    public @NotNull NBTTagCompound process(@NotNull IDataFixer fixer, @NotNull NBTTagCompound compound, int versionIn) {
        if (DataFixerHandler.fixNotAvailable() ||
                !DataFixerHandler.neededFixes.containsKey(LabsFixTypes.FixerTypes.CHUNK))
            return compound;

        return fixer.process(LabsFixTypes.FixerTypes.CHUNK, compound, versionIn);
    }
}
