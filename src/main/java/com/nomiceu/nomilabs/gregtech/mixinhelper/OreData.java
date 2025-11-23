package com.nomiceu.nomilabs.gregtech.mixinhelper;

import com.google.common.primitives.UnsignedBytes;

public class OreData {

    private int minY;
    private int maxY;

    public OreData(byte startingY) {
        minY = UnsignedBytes.toInt(startingY);
        maxY = minY;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    public int avgY() {
        // Whilst this isn't a perfect average, it puts waypoints in middle of vein, which is good
        // Note that this implementation is different to the one in GT #2726
        return (minY + maxY) / 2;
    }

    public OreData update(byte y) {
        int yPos = UnsignedBytes.toInt(y);

        minY = Math.min(yPos, minY);
        maxY = Math.max(yPos, maxY);
        return this;
    }
}
