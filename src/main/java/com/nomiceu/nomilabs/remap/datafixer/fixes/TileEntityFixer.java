package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFix;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

public class TileEntityFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    @NotNull
    public NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        for (var fix : DataFixerHandler.neededFixes.get(LabsFixTypes.FixerTypes.TILE_ENTITY)) {
            if (!(fix instanceof DataFix.TileEntityFix teFix)) continue;
            if (!teFix.validEntry.apply(compound)) continue;
            var oldCompound = compound.copy();
            teFix.transform.accept(compound);
            NomiLabs.LOGGER.debug("[Data Fixer] Changed Block Entity Tag from {} to {}", oldCompound, compound);
            return compound;
        }
        return compound;
    }
}
