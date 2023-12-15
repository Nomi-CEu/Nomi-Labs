package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.BlockLike;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

public class BlockFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    @NotNull
    public NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        LabsRemapHelper.rewriteBlocks(compound, (id, data) -> {
            var blockLike = new BlockLike(id, data);
            for (var shouldFix : LabsFixes.blockFixes.keySet()) {
                if (shouldFix.apply(blockLike)) {
                    var fix = LabsFixes.blockFixes.get(shouldFix);
                    fix.accept(blockLike);
                    return blockLike;
                }
            }
            return null;
        });
        return compound;
    }
}
