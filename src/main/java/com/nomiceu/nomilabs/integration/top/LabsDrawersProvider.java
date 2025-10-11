package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.mixin.theoneprobe.storagedrawers.DefaultProbeInfoProviderMixin;
import com.nomiceu.nomilabs.util.LabsTranslate;

import mcjty.theoneprobe.api.*;

/**
 * Heavily inspired by, and replaces, {@link io.github.drmanganese.topaddons.addons.AddonStorageDrawers}.
 * Replacement of the chest view occurs in
 * {@link DefaultProbeInfoProviderMixin}.
 */
public class LabsDrawersProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":drawers_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        if (!(world.getTileEntity(data.getPos()) instanceof TileEntityDrawers drawer)) return;

        // Drawer state checks
        if (drawer.isSealed()) {
            info.text(TextStyleClass.LABEL + LabsTranslate.topTranslate("nomilabs.top.storage_drawers.sealed"));
            return;
        }

        if (drawer.getOwner() != null && drawer.getOwner().compareTo(player.getUniqueID()) != 0) {
            info.text(TextStyleClass.ERROR + LabsTranslate.topTranslate("storagedrawers.waila.protected"));
            return;
        }

        var attr = drawer.getDrawerAttributes();

        // Max storage info
        String max;
        if (attr.isUnlimitedStorage()) {
            max = "∞";
        } else {
            max = drawer.getEffectiveDrawerCapacity() * drawer.upgrades().getStorageMultiplier() +
                    " (x" + drawer.upgrades().getStorageMultiplier() + ")";
        }

        info.text(
                TextStyleClass.LABEL + LabsTranslate.topTranslate("storagedrawers.waila.config.displayStackLimit") +
                        ": " + TextStyleClass.INFO + max);

        // Properties
        var group = drawer.getGroup();
        boolean hasItems = false;
        for (int i = 0; i < group.getDrawerCount(); i++) {
            if (!group.getDrawer(i).isEmpty()) {
                hasItems = true;
                break;
            }
        }
        LabsTOPUtils.addProperties(mode, info, hasItems && attr.isItemLocked(LockAttribute.LOCK_POPULATED),
                attr.isVoid());
    }
}
