package com.nomiceu.nomilabs.gregtech.mixinhelper;

/**
 * This Interface represents a MultiblockPart or MTE that should be refreshed before final recipe validation and input
 * consumption.
 */
public interface IRefreshBeforeConsumption {

    /**
     * Called Server Side Only.
     */
    void labs$refreshBeforeConsumption();
}
