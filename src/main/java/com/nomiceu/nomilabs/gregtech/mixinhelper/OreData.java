package com.nomiceu.nomilabs.gregtech.mixinhelper;

public class OreData {

    private byte minY;
    private byte maxY;
    private byte avgY;

    public OreData(byte startingY) {
        minY = startingY;
        maxY = startingY;
        avgY = startingY;
    }

    public byte minY() {
        return minY;
    }

    public byte maxY() {
        return maxY;
    }

    public byte avgY() {
        return avgY;
    }

    public OreData update(byte y) {
        minY = y < minY ? y : minY;
        maxY = y > maxY ? y : maxY;

        // Whilst this isn't a perfect average, it puts waypoints in middle of vein, which is good
        // Note that this implementation is different to the one in GT #2726
        avgY = (byte) ((minY + maxY) / 2);
        return this;
    }
}
