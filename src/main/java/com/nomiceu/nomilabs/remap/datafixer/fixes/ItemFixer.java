package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFix;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

public class ItemFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        var stack = new ItemStackLike(compound);
        for (var fix : DataFixerHandler.neededFixes.get(LabsFixTypes.FixerTypes.ITEM)) {
            if (!(fix instanceof DataFix.ItemFix itemFix)) continue;
            if (!itemFix.validEntry.apply(stack)) continue;
            itemFix.transform.accept(stack);
            var oldCompound = compound.copy();
            NomiLabs.LOGGER.debug("[Data Fixer] Changed Stack: {} to {}", oldCompound, stack.changeCompound(compound));
            return compound;
        }
        return compound;
    }
}
