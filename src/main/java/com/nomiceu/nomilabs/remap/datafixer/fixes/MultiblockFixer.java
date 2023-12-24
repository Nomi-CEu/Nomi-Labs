package com.nomiceu.nomilabs.remap.datafixer.fixes;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
        if (DataFixerHandler.fixNotAvailable()) return compound;

        NomiLabs.LOGGER.debug("Block Entity: {}", compound);
        if (compound.hasKey("MetaId", Constants.NBT.TAG_STRING) && compound.hasKey("id", Constants.NBT.TAG_STRING) &&
                compound.getString("id").equals(new ResourceLocation(LabsValues.GREGTECH_MODID, LabsValues.GT_MACHINE_PATH).toString())) {
            var metaId = new ResourceLocation(compound.getString("MetaId"));
            for (var shouldFix : LabsFixes.multiblockFixes.keySet()) {
                if (!shouldFix.apply(metaId)) continue;
                var newMetaId = LabsFixes.multiblockFixes.get(shouldFix).get().toString();
                compound.setString("MetaId", newMetaId);
                NomiLabs.LOGGER.info("[Data Fixer] Changed Block Entity MetaId: {} to {}", metaId, newMetaId);
                return compound;
            }
            return compound;
        }
        return compound;
    }
}
