package com.nomiceu.nomilabs.remap.datafixer.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFix;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;

public class BlockFixer implements IFixableData {

    @Override
    public int getFixVersion() {
        return LabsFixes.CURRENT;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        var blockFixes = DataFixerHandler.neededFixes.get(LabsFixTypes.FixerTypes.CHUNK);
        LabsRemapHelper.rewriteBlocks(compound, (state) -> {
            for (var fix : blockFixes) {
                if (!(fix instanceof DataFix.BlockFix blockFix)) continue;
                if (!blockFix.validEntry.apply(state)) continue;
                if (blockFix.teNeeded)
                    state.setTileEntityTag(LabsRemapHelper.getPosToTileEntityMap(compound).get(state.pos));
                if (blockFix.secondaryValidEntry != null && !blockFix.secondaryValidEntry.apply(state)) continue;
                var oldState = state.copy();
                blockFix.transform.accept(state);
                NomiLabs.LOGGER.debug("[Data Fixer] Changed Block: {} @ {} to {} @ {} at Pos: {}. (ID: {} to {})",
                        oldState.rl, oldState.meta, state.rl, state.meta, state.pos, state.getOldId(), state.getId());
                if (blockFix.teNeeded)
                    NomiLabs.LOGGER.debug("[Data Fixer] Changed Tile Entity With Above Block: {} to {}.",
                            oldState.tileEntityTag, state.tileEntityTag);
                return state;
            }
            return state;
        });
        return compound;
    }
}
