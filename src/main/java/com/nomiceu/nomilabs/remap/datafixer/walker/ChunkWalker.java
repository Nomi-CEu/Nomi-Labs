package com.nomiceu.nomilabs.remap.datafixer.walker;

import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

public class ChunkWalker implements IDataWalker {
    @Override
    public @NotNull NBTTagCompound process(@NotNull IDataFixer fixer, @NotNull NBTTagCompound compound, int versionIn) {
        if (DataFixerHandler.fixNotAvailable() || !DataFixerHandler.neededFixes.containsKey(LabsFixTypes.FixerTypes.BLOCK)) return compound;

        if (compound.hasKey("Level", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound levelTag = compound.getCompoundTag("Level");
            if (levelTag.hasKey("Sections", Constants.NBT.TAG_LIST)) {
                NBTTagList sectionListTag = levelTag.getTagList("Sections", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < sectionListTag.tagCount(); i++) {
                    sectionListTag.set(i, fixer.process(
                            LabsFixTypes.FixerTypes.BLOCK, sectionListTag.getCompoundTagAt(i), versionIn));
                }
            }
        }
        return compound;
    }
}
