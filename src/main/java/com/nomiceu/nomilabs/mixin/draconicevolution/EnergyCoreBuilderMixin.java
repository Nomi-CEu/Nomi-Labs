package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.lib.EnergyCoreBuilder;
import com.nomiceu.nomilabs.integration.draconicevolution.EnergyCoreBuilderLogic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.Map;

@Mixin(value = EnergyCoreBuilder.class, remap = false)
public class EnergyCoreBuilderMixin {
    @Shadow
    @Final
    private TileEnergyStorageCore core;

    @Shadow
    private Map<BlockPos, IBlockState> workList;

    @Shadow
    private LinkedList<BlockPos> workOrder;

    @Shadow
    @Final
    private EntityPlayer player;

    @Shadow
    private World world;

    @Shadow
    private boolean isDead;

    @Inject(method = "buildWorkList", at = @At("HEAD"), cancellable = true)
    private void buildWorklist(CallbackInfo ci) {
        isDead = EnergyCoreBuilderLogic.buildWorklist(core, player, workList, workOrder);
        ci.cancel();
    }

    @Inject(method = "updateProcess", at = @At("HEAD"), cancellable = true)
    private void updateProcess(CallbackInfo ci) {
        isDead = EnergyCoreBuilderLogic.updateProcess(world, player, workList, workOrder);
        ci.cancel();
    }
}
