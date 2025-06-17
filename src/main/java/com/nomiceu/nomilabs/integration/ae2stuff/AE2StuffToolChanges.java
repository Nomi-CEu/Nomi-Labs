package com.nomiceu.nomilabs.integration.ae2stuff;

import net.bdew.ae2stuff.machines.grower.BlockGrower;
import net.bdew.ae2stuff.machines.inscriber.BlockInscriber;
import net.bdew.ae2stuff.machines.wireless.BlockWireless;

/**
 * Applies <a href="https://github.com/AE2-UEL/ae2stuff/pull/5">AE2Stuff #5</a>.
 */
public class AE2StuffToolChanges {

    public static void apply() {
        BlockGrower.setHarvestLevel("pickaxe", 2);
        BlockInscriber.setHarvestLevel("pickaxe", 2);
        BlockWireless.setHarvestLevel("pickaxe", 2);
    }
}
