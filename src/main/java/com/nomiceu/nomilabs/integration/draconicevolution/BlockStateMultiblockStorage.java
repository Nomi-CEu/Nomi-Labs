package com.nomiceu.nomilabs.integration.draconicevolution;

import java.util.function.BiConsumer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.brandon3055.brandonscore.lib.MultiBlockStorage;

public class BlockStateMultiblockStorage extends MultiBlockStorage {

    private final BlockStates[][][] structure;
    private final int size;
    private final BlockStateMultiblockHelper helper;
    private final BlockStateEnergyCoreStructure energyCoreStructure;
    private int xPos = 0;
    private int yPos = 0;

    public BlockStateMultiblockStorage(int size, BlockStateMultiblockHelper helper,
                                       BlockStateEnergyCoreStructure energyCoreStructure) {
        super(size, helper);
        this.helper = helper;
        this.size = size;
        this.structure = new BlockStates[size][size][size];
        this.energyCoreStructure = energyCoreStructure;
    }

    public void addRow(BlockStates... zRow) {
        if (zRow.length == size) {
            if (xPos >= size) {
                throw new RuntimeException("[MultiBlockStorage] Attempt to add too many zRow's to layer");
            } else {
                structure[xPos][yPos] = zRow;
                xPos++;
            }
        } else {
            throw new RuntimeException(
                    "[MultiBlockStorage] Attempt to add zRow larger or smaller then defined structure size");
        }
    }

    /**
     * Mirrors a set of layers.
     * 
     * @param minY Min y layer to mirror. Inclusive.
     * @param maxY Max y layer to mirror. Exclusive.
     */
    public void mirrorLayers(int minY, int maxY) {
        if (yPos < maxY) {
            throw new IllegalArgumentException("[MultiBlockStorage] Cannot mirror from minY " + minY + " to maxY " +
                    maxY + " as have not reached maxY yet!");
        }
        // Loop from last to first (mirror), excluding maxY, including minY
        for (int y = maxY - 1; y >= minY; y--) {
            if (y != minY || xPos != 0)
                newLayer();

            for (int x = 0; x < size; x++)
                addRow(structure[x][y]);
        }
    }

    /**
     * Mirrors half the structure. Rounds down if odd.
     */
    public void mirrorHalf() {
        mirrorLayers(0, size / 2);
    }

    @Override
    public void newLayer() {
        xPos = 0;
        yPos++;
        if (this.yPos >= size) {
            throw new RuntimeException("[MultiBlockStorage] Attempt to add too many layers to structure");
        }
    }

    @Override
    public boolean checkStructure(World world, BlockPos startPos) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (structure[x][y][z].isWildcard())
                        continue;

                    if (!energyCoreStructure.checkBlock(structure[x][y][z], world, pos.add(startPos))) {
                        helper.invalidBlock = startPos.add(pos);
                        helper.expectedBlockState = structure[x][y][z].getDefault();
                        return false;
                    }
                }
            }
        }

        helper.invalidBlock = null;
        return true;
    }

    @Override
    public void placeStructure(World world, BlockPos startPos) {
        forEachBlockStates(startPos, (pos, states) -> energyCoreStructure.setBlock(states, world, pos));
    }

    @Override
    public void forEachInStructure(World world, BlockPos startPos, int flag) {
        forEachBlockStates(startPos, (pos, states) -> energyCoreStructure.forBlock(states, world, pos, startPos, flag));
    }

    public void forEachBlockStates(BlockPos startPos, BiConsumer<BlockPos, BlockStates> consumer) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    if (!structure[x][y][z].isWildcard()) {
                        consumer.accept((new BlockPos(x, y, z)).add(startPos), structure[x][y][z]);
                    }
                }
            }
        }
    }
}
