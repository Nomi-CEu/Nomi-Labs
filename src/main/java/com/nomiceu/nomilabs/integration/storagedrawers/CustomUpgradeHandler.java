package com.nomiceu.nomilabs.integration.storagedrawers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.Nullable;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

public class CustomUpgradeHandler {

    public static final String CUSTOM_UPGRADES = "labs_saved_upgrades";

    public static void addCustomUpgradesToTile(TileEntityDrawers tile, @Nullable NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(CUSTOM_UPGRADES, Constants.NBT.TAG_COMPOUND) || nbt.hasKey("tile")) return;

        tile.upgrades().readFromNBT(nbt.getCompoundTag(CUSTOM_UPGRADES));
    }
}
