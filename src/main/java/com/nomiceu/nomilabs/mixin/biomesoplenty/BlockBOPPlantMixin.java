package com.nomiceu.nomilabs.mixin.biomesoplenty;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biomesoplenty.common.block.BlockBOPPlant;

/**
 * Fixes a crash with BOP and FTB Utils.
 */
@Mixin(value = BlockBOPPlant.class, remap = false)
public class BlockBOPPlantMixin {

    @Shadow
    public IProperty<?> variantProperty;

    @Inject(method = "isReplaceable", at = @At("HEAD"), cancellable = true, remap = true)
    private void checkForInstance(IBlockAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        IBlockState state = world.getBlockState(pos);

        if (!state.getPropertyKeys().contains(variantProperty))
            cir.setReturnValue(state.getMaterial().isReplaceable());
    }
}
