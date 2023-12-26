package com.nomiceu.nomilabs.remap.datafixer.storage;

@FunctionalInterface
public interface BlockRewriter {
    BlockStateLike rewrite(BlockStateLike state);
}
