package com.nomiceu.nomilabs.integration.draconicevolution;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.nomiceu.nomilabs.config.LabsConfig;

public class EnergyCoreDestructor implements StoppableProcess {

    private Map<BlockPos, BlockStates> workList;
    private final LinkedList<BlockPos> workOrder;
    private final TileEnergyStorageCore core;
    private final EntityPlayer player;
    private boolean isDead;
    private final World world;

    public EnergyCoreDestructor(TileEnergyStorageCore core, EntityPlayer player) {
        this.workList = new HashMap<>();
        this.workOrder = new LinkedList<>();
        this.isDead = false;
        this.player = player;
        this.core = core;
        this.world = core.getWorld();
        destruct();
    }

    private void destruct() {
        workList.clear();
        workOrder.clear();

        buildWorklist();

        if (DraconicHelpers.instantDestructor()) {
            destructInstant();
        }
    }

    private void buildWorklist() {
        workList = DraconicHelpers.getStructureBlocks(core);

        workOrder.addAll(workList.keySet());
        workOrder.sort(Comparator.comparingDouble(value -> ((BlockPos) value).distanceSq(core.getPos())).reversed());
    }

    private void destructInstant() {
        while (!isDead) {
            updateProcess();
        }
    }

    public void updateDestructProcess() {
        for (int i = 0; i < LabsConfig.modIntegration.draconicEvolutionIntegration.autoDestructorSpeed; i++) {
            updateProcess();
            if (isDead) return;
        }
    }

    /**
     * Returns whether updating 'failed'.
     */
    private void updateProcess() {
        if (workOrder.isEmpty() || player.isDead) {
            stop();
        }

        var pos = workOrder.poll();
        var states = workList.get(pos);

        if (pos == null || states == null)
            return;
        if (states.isWildcard() || states.equals(((BlockStateEnergyCoreStructure) core.coreStructure).X) ||
                world.isAirBlock(pos))
            return;

        if (!player.capabilities.isCreativeMode) {
            ItemStack stack = new ItemStack(states.getDefault().getBlock(), 1,
                    states.getDefault().getBlock().getMetaFromState(states.getDefault()));
            if (!DraconicHelpers.insertItem(stack, player)) {
                Block.spawnAsEntity(world, pos, stack);
            }
        }

        world.setBlockToAir(pos);
        SoundType soundtype = states.getDefault().getBlock().getSoundType(states.getDefault(), world, pos, player);
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundtype.getBreakSound(),
                SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
    }

    @Override
    public void stop() {
        isDead = true;
        workOrder.clear();
        workList.clear();
    }

    public boolean isDead() {
        return isDead;
    }
}
