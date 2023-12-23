package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
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
        if (DataFixerHandler.fixNotAvailable()) return compound;

        var stack = new ItemStackLike(compound);
        for (var shouldFix : LabsFixes.itemFixes.keySet()) {
            if (!shouldFix.apply(stack)) continue;
            var fix = LabsFixes.itemFixes.get(shouldFix);
            fix.accept(stack);
            var oldCompound = compound.copy();
            NomiLabs.LOGGER.info("[Data Fixer] Changed Stack: {} to {}", oldCompound, stack.changeCompound(compound));
            return compound;
        }
        return compound;
    }
}
