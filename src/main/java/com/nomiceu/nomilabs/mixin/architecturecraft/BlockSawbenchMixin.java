package com.nomiceu.nomilabs.mixin.architecturecraft;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.elytradev.architecture.common.block.BlockArchitecture;
import com.elytradev.architecture.common.block.BlockSawbench;
import com.elytradev.architecture.common.tile.TileSawbench;

/**
 * Fixes the Particle Texture of the Sawbench.
 */
@Mixin(value = BlockSawbench.class, remap = false)
public class BlockSawbenchMixin extends BlockArchitecture<TileSawbench> {

    /**
     * Default Ignored Constructor
     */
    private BlockSawbenchMixin(Material material) {
        super(material);
    }

    @Unique
    @Override
    public IBlockState getParticleState(IBlockAccess world, BlockPos pos) {
        return Blocks.IRON_BLOCK.getDefaultState();
    }
}
