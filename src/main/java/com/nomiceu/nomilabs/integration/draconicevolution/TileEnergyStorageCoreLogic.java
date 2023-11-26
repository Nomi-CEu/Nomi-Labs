package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class TileEnergyStorageCoreLogic {
    public static void activateCore(TileEnergyStorageCore tile, long capacity) {
        if (!tile.getWorld().isRemote && tile.validateStructure()) {
            if (tile.energy.value > capacity) {
                tile.energy.value = capacity;
            }

            tile.buildGuide.value = false;
            tile.coreStructure.formTier(tile.tier.value);
            tile.active.value = true;
            // Stabilizers activated in mixin (private method)
        }
    }

    public static void deactivateCore(TileEnergyStorageCore tile) {
        if (!tile.getWorld().isRemote) {
            tile.coreStructure.revertTier(tile.tier.value);
            tile.active.value = false;
            // Stabilizers activated in mixin (private method)
        }
    }

    public static boolean validateStructure(TileEnergyStorageCore tile) {
        boolean valid = tile.checkStabilizers();
        var helper = ((BlockStateEnergyCoreStructure) tile.coreStructure).getHelper();
        if (!(tile.coreValid.value = tile.coreStructure.checkTier(tile.tier.value))) {
            BlockPos pos = helper.invalidBlock;
            String expectedString = helper.expectedBlockState == null
                    ? "null"
                    : new ItemStack(helper.expectedBlockState.getBlock(), 1,
                    helper.expectedBlockState.getBlock().getMetaFromState(helper.expectedBlockState)).getDisplayName();
            tile.invalidMessage.value = "Error At: x:" + pos.getX() + ", y:" + pos.getY() + ", z:" + pos.getZ() + " Expected: " + expectedString;
            valid = false;
        }

        if (!valid && tile.active.value) {
            tile.active.value = false;
            tile.deactivateCore();
        }

        tile.structureValid.value = valid;
        if (valid) {
            tile.invalidMessage.value = "";
        }

        return valid;
    }
}
