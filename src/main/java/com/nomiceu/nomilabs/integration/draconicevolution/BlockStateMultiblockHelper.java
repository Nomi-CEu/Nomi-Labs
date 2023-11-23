package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.brandonscore.utils.MultiBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateMultiblockHelper extends MultiBlockHelper {
    public IBlockState expectedBlockState = null;

    public boolean checkBlock(BlockStates states, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return DraconicHelpers.validState(states, state, false);
    }

    public void setBlock(BlockStates states, World world, BlockPos pos) {
        if (states.isWildcard()) {
            world.setBlockToAir(pos);
        } else {
            world.setBlockState(pos, states.getDefault());
        }

    }

    @SuppressWarnings("unused")
    public void forBlock(BlockStates state, World world, BlockPos pos, BlockPos startPos, int flag) {
    }
}
