package com.nomiceu.nomilabs.remap.datafixer.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFix;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;

public class ItemFixer implements IFixableData {

    @Override
    public int getFixVersion() {
        return LabsRemapHelper.getReportedVersion();
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        var stack = new ItemStackLike(compound);
        for (var fix : LabsFixes.fixes.get(LabsFixTypes.FixerTypes.ITEM)) {
            if (!(fix instanceof DataFix.ItemFix itemFix)) continue;
            if (!itemFix.validEntry.apply(stack)) continue;
            itemFix.transform.accept(stack);
            var oldCompound = compound.copy();
            NomiLabs.LOGGER.debug("[Data Fixer] Changed Stack: {} to {}", oldCompound, stack.changeCompound(compound));
            // Don't return, allow other item fixes to apply
        }
        return compound;
    }
}
