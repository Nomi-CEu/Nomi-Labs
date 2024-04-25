package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.ftbutilities.CanEditChunkHelper;

import nl.requios.effortlessbuilding.helper.SurvivalHelper;

@Mixin(value = SurvivalHelper.class, remap = false)
public class SurvivalHelperMixin {

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private static void checkFTBUtilsCondition(World world, Block blockIn, IBlockState newBlockState, BlockPos pos,
                                               boolean skipCollisionCheck, EnumFacing sidePlacedOn, Entity placer,
                                               CallbackInfoReturnable<Boolean> cir) {
        if (!Loader.isModLoaded(LabsValues.FTB_UTILS_MODID) || !(placer instanceof EntityPlayer player)) return;

        if (CanEditChunkHelper.cannotEditChunk(player, pos, newBlockState)) cir.setReturnValue(false);
    }
}
