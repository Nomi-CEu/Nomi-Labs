package com.nomiceu.nomilabs.integration.betterp2p;

import net.minecraft.util.math.Vec3d;

import com.projecturanus.betterp2p.client.gui.widget.WidgetScrollBar;

public interface AccessibleInfoList {

    void labs$setPlayerPos(Vec3d pos);

    void labs$properlyResetScrollbar(WidgetScrollBar scrollBar, int numEntries);

    void labs$setSortMode(SortModes mode);

    SortModes labs$getSortMode();

    void labs$changeSortMode(boolean forwards);

    boolean labs$getSortReversed();

    void labs$setSortReversed(boolean reversed);
}
