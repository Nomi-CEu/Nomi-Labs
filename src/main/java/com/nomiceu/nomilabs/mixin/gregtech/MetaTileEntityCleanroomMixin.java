package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.common.blocks.BlockCleanroomCasing;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityCleanroom;

/**
 * Fixes forming issues caused by non-casings present in the middle of a top edge.
 */
@Mixin(value = MetaTileEntityCleanroom.class, remap = false)
public abstract class MetaTileEntityCleanroomMixin {

    @Redirect(method = "updateStructureDimensions",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/common/metatileentities/multi/electric/MetaTileEntityCleanroom;isBlockEdge(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;Lnet/minecraft/util/EnumFacing;)Z"))
    private boolean properlyCheckEdge(MetaTileEntityCleanroom instance, World world, BlockPos.MutableBlockPos pos,
                                      EnumFacing direction) {
        // Since the top face is meant to be all filters (except for the edge),
        // we are able to just check if it is not a filter
        IBlockState state = world.getBlockState(pos.move(direction));
        Block block = state.getBlock();

        return !(block instanceof BlockCleanroomCasing casing) ||
                casing.getState(state) == BlockCleanroomCasing.CasingType.PLASCRETE;
    }
}
