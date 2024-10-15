package com.nomiceu.nomilabs.mixin.storagedrawers;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.capabilities.BasicDrawerAttributes;
import com.nomiceu.nomilabs.integration.storagedrawers.RefreshableDrawer;

@Mixin(targets = "com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers$DrawerAttributes", remap = false)
public class DrawerAttributesMixin extends BasicDrawerAttributes implements RefreshableDrawer {

    @Final
    @Shadow
    TileEntityDrawers this$0;

    @Override
    public void refreshAfterDrawerKey() {
        if (this$0.getGroup() instanceof FractionalDrawerGroup frac) {
            // Custom Handling
            for (int i = 0; i < frac.getDrawerCount(); i++) {
                var drawer = frac.getDrawer(i);
                if (!drawer.isEnabled()) continue;

                if (drawer.isEmpty() || drawer.getStoredItemCount() != 0) return;
            }

            for (int i = 0; i < frac.getDrawerCount(); i++) {
                var drawer = frac.getDrawer(i);
                if (!drawer.isEnabled()) continue;
                drawer.setStoredItem(ItemStack.EMPTY);
            }
            return;
        }

        for (int i = 0; i < this$0.getGroup().getDrawerCount(); i++) {
            var drawer = this$0.getGroup().getDrawer(i);
            if (!drawer.isEnabled()) continue;

            if (!drawer.isEmpty() && drawer.getStoredItemCount() == 0)
                drawer.setStoredItem(ItemStack.EMPTY);
        }
    }
}
