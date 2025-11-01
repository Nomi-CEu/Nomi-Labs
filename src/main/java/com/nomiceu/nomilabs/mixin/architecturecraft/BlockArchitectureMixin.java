package com.nomiceu.nomilabs.mixin.architecturecraft;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.elytradev.architecture.common.block.BlockArchitecture;

/**
 * Fixes running particles.
 */
@Mixin(value = BlockArchitecture.class, remap = false)
public abstract class BlockArchitectureMixin extends BlockContainer {

    @Shadow
    public abstract IBlockState getParticleState(IBlockAccess world, BlockPos pos);

    /**
     * Mandatory Ignored Constructor
     */
    private BlockArchitectureMixin(Material materialIn) {
        super(materialIn);
    }

    @Unique
    @Override
    @ParametersAreNonnullByDefault
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        IBlockState particleState = getParticleState(world, pos);

        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                pos.getX() + ((double) RANDOM.nextFloat() - 0.5D) * (double) entity.width,
                entity.getEntityBoundingBox().minY + 0.1D,
                pos.getZ() + ((double) RANDOM.nextFloat() - 0.5D) * (double) entity.width,
                -entity.motionX * 4.0D,
                1.5D,
                entity.motionZ * 4.0D,
                Block.getStateId(particleState));
        return true;
    }
}
