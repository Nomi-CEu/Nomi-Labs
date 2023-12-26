package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFix;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

public class BlockFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        var blockFixes = DataFixerHandler.neededFixes.get(LabsFixTypes.FixerTypes.BLOCK);
        LabsRemapHelper.rewriteBlocks(compound, (state) -> {
            for (var fix : blockFixes) {
                if (!(fix instanceof DataFix.BlockFix blockFix)) continue;
                if (!blockFix.validEntry.apply(state)) continue;
                var oldRl = state.rl;
                var oldMeta = state.meta;
                blockFix.transform.accept(state);
                NomiLabs.LOGGER.debug("[Data Fixer] Changed Block: {} @ {} to {} @ {}", oldRl, oldMeta, state.rl, state.meta);
                return state;
            }
            return state;
        });
        return compound;
    }
}
