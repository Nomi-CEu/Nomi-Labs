package com.nomiceu.nomilabs.integration.betterp2p;

import com.projecturanus.betterp2p.network.data.P2PLocation;

import appeng.parts.p2p.PartP2PTunnel;

public interface AccessibleGridServerCache {

    PartP2PTunnel<?> labs$changeIsInput(P2PLocation key, boolean isInput);

    boolean labs$addInput(P2PLocation toAdd, P2PLocation toBind);

    boolean labs$addOutput(P2PLocation toAdd, P2PLocation toBind);
}
