package com.nomiceu.nomilabs.integration.draconicevolution;

public interface TileInvisECoreBlockState {

    boolean labs$getDefault();

    /**
     * This should also set metadata to 0.
     */
    void labs$setIsDefault();

    int labs$getMetadata();

    /**
     * This should also set default to false, if meta set is not 0 (legacy compat)
     */
    void labs$setMetadata(int metadata);
}
