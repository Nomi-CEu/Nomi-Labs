package com.nomiceu.nomilabs.remap.datafixer.storage;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;

@FunctionalInterface
public interface BlockRewriter {

    @Nullable
    BlockLike rewrite(int id, short data);
}