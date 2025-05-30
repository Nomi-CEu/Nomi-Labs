package com.nomiceu.nomilabs.integration.nae2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import com.glodblock.github.loader.FCBlocks;
import com.glodblock.github.loader.FCItems;

import co.neeve.nae2.NAE2;
import co.neeve.nae2.common.registration.definitions.Upgrades;

/**
 * Temporary class to add NAE2 upgrade card support to AE2 FC Interfaces.
 */
public class AE2FCIntegration {

    public static void postInit() {
        Upgrades upgrades = NAE2.definitions().upgrades();

        if (upgrades.autoComplete().isEnabled())
            registerUpgradeFc(Upgrades.UpgradeType.AUTO_COMPLETE);
        if (upgrades.gregtechCircuit().isEnabled())
            registerUpgradeFc(Upgrades.UpgradeType.GREGTECH_CIRCUIT);
    }

    @Optional.Method(modid = "nae2")
    private static void registerUpgradeFc(Upgrades.UpgradeType upgrade) {
        upgrade.registerItem(new ItemStack(FCBlocks.DUAL_INTERFACE), 1);
        upgrade.registerItem(new ItemStack(FCItems.PART_DUAL_INTERFACE), 1);
    }
}
