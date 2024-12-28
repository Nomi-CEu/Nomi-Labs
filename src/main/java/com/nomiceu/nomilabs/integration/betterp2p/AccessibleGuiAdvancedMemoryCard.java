package com.nomiceu.nomilabs.integration.betterp2p;

import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

public interface AccessibleGuiAdvancedMemoryCard {

    BetterMemoryCardModes labs$getMode();

    void labs$setMode(BetterMemoryCardModes mode);

    void labs$syncMemoryInfo();

    void labs$closeTypeSelector();

    void labs$changeSort(boolean forwards);

    SortModes labs$getSortMode();
}
