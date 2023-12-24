package com.nomiceu.nomilabs.integration.draconicevolution;

public interface ImprovedTileEnergyCore {
    /**
     * @return True if the core has an active builder, false otherwise.
     */
    boolean hasActiveBuilder();

    /**
     * @return True if the core has an active destructor, false otherwise.
     */
    boolean hasActiveDestructor();
}
