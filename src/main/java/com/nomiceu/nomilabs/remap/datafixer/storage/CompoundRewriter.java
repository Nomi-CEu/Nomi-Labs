package com.nomiceu.nomilabs.remap.datafixer.storage;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import net.minecraft.nbt.NBTTagCompound;

@FunctionalInterface
public interface CompoundRewriter {

    @Nullable
    NBTTagCompound rewrite(NBTTagCompound tag);

}
