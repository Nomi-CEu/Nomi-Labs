package com.nomiceu.nomilabs.remap.datafixer.walker;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

public class ItemStackWalker implements IDataWalker {
    private final String type;
    public ItemStackWalker(String type) {
        this.type = type;
    }
    @Override
    @NotNull
    public NBTTagCompound process(@NotNull IDataFixer fixer, @NotNull NBTTagCompound compound, int versionIn) {
        if (DataFixerHandler.fixNotAvailable()) return compound;

        LabsRemapHelper.rewriteCompoundTags(compound, tag -> {
            if (tag.hasKey("id", Constants.NBT.TAG_STRING) && tag.hasKey("Count", Constants.NBT.TAG_ANY_NUMERIC)
                    && tag.hasKey("Damage", Constants.NBT.TAG_ANY_NUMERIC)) {
                NomiLabs.LOGGER.debug("[ItemStack Walker] {}, walked to {}", type, tag);
                return fixer.process(LabsFixTypes.FixerTypes.ITEM, tag, versionIn);
            }
            return null;
        });
        return compound;
    }
}
