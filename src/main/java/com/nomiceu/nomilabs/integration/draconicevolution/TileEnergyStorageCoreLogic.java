package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public static void destructCore(TileEnergyStorageCore tile, EntityPlayer player) {
        var coreStructure = (BlockStateEnergyCoreStructure) tile.coreStructure;
        World world = tile.getWorld();
        BlockStateMultiblockStorage storage = coreStructure.getStorageForTier(tile.tier.value);
        BlockPos start = tile.getPos().add(coreStructure.getCoreOffset(tile.tier.value));
        storage.forEachBlockStates(start, (pos, states) -> {
            if (states == null || states.isWildcard() || states.equals(coreStructure.X)) return;

            if (!player.capabilities.isCreativeMode){
                ItemStack stack = new ItemStack(states.getDefault().getBlock(), 1, states.getDefault().getBlock().getMetaFromState(states.getDefault()));
                if (!DraconicHelpers.insertItem(stack, player)){
                    Block.spawnAsEntity(world, pos, stack);
                }
            }

            world.setBlockToAir(pos);
            SoundType soundtype = states.getDefault().getBlock().getSoundType(states.getDefault(), world, pos, player);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        });
    }
}
