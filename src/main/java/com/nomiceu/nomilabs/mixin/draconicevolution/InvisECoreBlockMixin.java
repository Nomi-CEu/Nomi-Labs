package com.nomiceu.nomilabs.mixin.draconicevolution;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.brandon3055.draconicevolution.blocks.InvisECoreBlock;
import com.nomiceu.nomilabs.integration.draconicevolution.InvisECoreBlockLogic;

@Mixin(value = InvisECoreBlock.class, remap = false)
public class InvisECoreBlockMixin {

    @Inject(method = "onBlockHarvested(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = true)
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player, CallbackInfo ci) {
        InvisECoreBlockLogic.onBlockHarvested(world, pos, player);
        ci.cancel();
    }

    @Inject(method = "getPickBlock", at = @At("HEAD"), cancellable = true)
    public void getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player,
                             CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(InvisECoreBlockLogic.getPickBlock(world, pos));
    }
}
