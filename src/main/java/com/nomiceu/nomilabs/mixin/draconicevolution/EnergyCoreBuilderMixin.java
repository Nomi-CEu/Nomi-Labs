package com.nomiceu.nomilabs.mixin.draconicevolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.lib.EnergyCoreBuilder;
import com.nomiceu.nomilabs.integration.draconicevolution.BlockStates;
import com.nomiceu.nomilabs.integration.draconicevolution.EnergyCoreBuilderLogic;
import com.nomiceu.nomilabs.integration.draconicevolution.StoppableProcess;

@SuppressWarnings("unused")
@Mixin(value = EnergyCoreBuilder.class, remap = false)
public class EnergyCoreBuilderMixin implements StoppableProcess {

    @Shadow
    @Final
    private TileEnergyStorageCore core;

    @Unique
    private final Map<BlockPos, BlockStates> labs$blockStatesWorkList = new HashMap<>();

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
        isDead = EnergyCoreBuilderLogic.build(core, player, labs$blockStatesWorkList, workOrder);
        ci.cancel();
    }

    @Inject(method = "updateProcess", at = @At("HEAD"), cancellable = true)
    private void updateProcess(CallbackInfo ci) {
        isDead = EnergyCoreBuilderLogic.updateBuildProcess(world, player, labs$blockStatesWorkList, workOrder);
        ci.cancel();
    }

    @Unique
    @Override
    public void labs$stop() {
        isDead = true;
        workOrder.clear();
        labs$blockStatesWorkList.clear();
    }
}
