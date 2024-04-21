package com.nomiceu.nomilabs.integration.draconicevolution;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;

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
        var improvedTile = (ImprovedTileEnergyCore) tile;
        if (!(tile.coreValid.value = tile.coreStructure.checkTier(tile.tier.value))) {
            BlockPos pos = helper.invalidBlock;

            improvedTile.setExpectedBlockString(helper.expectedBlockState == null ? "null" :
                    new ItemStack(helper.expectedBlockState.getBlock(), 1,
                            helper.expectedBlockState.getBlock().getMetaFromState(helper.expectedBlockState))
                                    .getDisplayName());

            improvedTile.setExpectedBlockPos(pos);
            valid = false;
        }

        if (!valid && tile.active.value) {
            tile.active.value = false;
            tile.deactivateCore();
        }

        tile.structureValid.value = valid;
        if (valid) {
            improvedTile.setExpectedBlockString("");
        }

        return valid;
    }
}
