package com.nomiceu.nomilabs.remap.datafixer.walker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;

public class ItemStackWalker implements IDataWalker {

    @Override
    @NotNull
    public NBTTagCompound process(@NotNull IDataFixer fixer, @NotNull NBTTagCompound compound, int versionIn) {
        if (DataFixerHandler.fixNotAvailable() ||
                !DataFixerHandler.neededFixes.containsKey(LabsFixTypes.FixerTypes.ITEM))
            return compound;

        LabsRemapHelper.rewriteCompoundTags(compound, tag -> {
            if (LabsRemapHelper.tagHasItemInfo(tag)) {
                return fixer.process(LabsFixTypes.FixerTypes.ITEM, tag, versionIn);
            }
            return null;
        });
        return compound;
    }
}
