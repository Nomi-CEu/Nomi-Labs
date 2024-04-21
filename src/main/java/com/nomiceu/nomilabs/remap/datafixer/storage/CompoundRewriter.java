package com.nomiceu.nomilabs.remap.datafixer.storage;

import net.minecraft.nbt.NBTTagCompound;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;

@FunctionalInterface
public interface CompoundRewriter {

    @Nullable
    NBTTagCompound rewrite(NBTTagCompound tag);
}
