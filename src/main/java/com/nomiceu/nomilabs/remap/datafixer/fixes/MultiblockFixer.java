package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

public class MultiblockFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    @NotNull
    public NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        NomiLabs.LOGGER.debug("Block Entity: {}", compound);
        if (compound.hasKey("MetaId", Constants.NBT.TAG_STRING) && compound.hasKey("id", Constants.NBT.TAG_STRING) && compound.getString("id").equals("gregtech:machine")) {
            var metaId = compound.getString("MetaId");
            for (var shouldFix : LabsFixes.multiblockFixes.keySet()) {
                if (!shouldFix.apply(metaId)) continue;
                var fix = LabsFixes.multiblockFixes.get(shouldFix);
                var oldCompound = compound.copy();
                compound.setString("MetaId", fix.get());
                NomiLabs.LOGGER.info("Changed Block Entity: {} to {}", oldCompound, compound);
                return compound;
            }
            return compound;
        }
        return compound;
    }
}
