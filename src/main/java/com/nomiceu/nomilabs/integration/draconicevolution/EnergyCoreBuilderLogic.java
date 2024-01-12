package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.*;

public class EnergyCoreBuilderLogic {
    /**
     * Returns isDead.
     */
    public static boolean build(TileEnergyStorageCore core, EntityPlayer player, Map<BlockPos, BlockStates> workList, LinkedList<BlockPos> workOrder) {
        workList.clear();
        workOrder.clear();
        if (buildWorklist(core, player, workList, workOrder)) {
            workList.clear();
            return true;
        }

        if (DraconicHelpers.instantBuilder()) {
            buildInstant(core.getWorld(), player, workList, workOrder);
            return true;
        }
        return false;
    }

    /**
     * Returns isDead.
     */
    private static boolean buildWorklist(TileEnergyStorageCore core, EntityPlayer player, Map<BlockPos, BlockStates> workList, LinkedList<BlockPos> workOrder) {
        Map<BlockPos, BlockStates> structureBlocks = DraconicHelpers.getStructureBlocks(core);

        World world = core.getWorld();
        for (BlockPos key : structureBlocks.keySet()) {
            BlockStates neededStates = structureBlocks.get(key);
            if (world.isAirBlock(key)) {
                workList.put(key, neededStates);
                continue;
            }
            IBlockState state = world.getBlockState(key);
            if (!DraconicHelpers.validState(neededStates, state, false)) {
                player.sendMessage(new TextComponentTranslation("ecore.de.assemble_found_invalid.txt",
                        BlockStates.transformStateToStack(state).getDisplayName(), key.toString()).setStyle(new Style().setColor(TextFormatting.RED)));
                return true;
            }
        }

        workOrder.addAll(workList.keySet());
        workOrder.sort(Comparator.comparingDouble(value -> value.distanceSq(core.getPos())));
        return false;
    }

    private static void buildInstant(World world, EntityPlayer player, Map<BlockPos, BlockStates> workList, LinkedList<BlockPos> workOrder) {
        boolean isDead = false;
        while (!isDead) {
            isDead = updateProcess(world, player, workList, workOrder);
        }
    }

    /**
     * Returns isDead.
     */
    public static boolean updateBuildProcess(World world, EntityPlayer player, Map<BlockPos, BlockStates> workList, LinkedList<BlockPos> workOrder) {
        for (int i = 0; i < LabsConfig.modIntegration.draconicEvolutionIntegration.autoBuilderSpeed; i++) {
            if (updateProcess(world, player, workList, workOrder)) return true;
        }
        return false;
    }

    /**
     * Returns whether updating 'failed'.
     */
    private static boolean updateProcess(World world, EntityPlayer player, Map<BlockPos, BlockStates> workList, LinkedList<BlockPos> workOrder) {
        if (workOrder.isEmpty() || player.isDead) {
            return true;
        }

        var pos = workOrder.poll();
        var states = workList.get(pos);
        if (!world.isAirBlock(pos)) {
            if (DraconicHelpers.validState(states, world.getBlockState(pos), false)) {
                return false;
            }
            player.sendMessage(new TextComponentTranslation("ecore.de.assemble_error_expected_air.txt", pos.toString()).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
        }

        if (player.capabilities.isCreativeMode || DraconicHelpers.extractItemOfBlockStates(states, player)) {
            world.setBlockState(pos, states.getDefault());
            SoundType soundtype = states.getDefault().getBlock().getSoundType(states.getDefault(), world, pos, player);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        }
        else {
            ItemStack[] requiredStacks = states.transformToStack();
            String required = String.join(", OR ",
                    Arrays.stream(requiredStacks).map(ItemStack::getDisplayName).toArray(String[]::new));
            player.sendMessage(new TextComponentTranslation("ecore.de.assemble_missing_required_new.txt", required).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
        }
        return false;
    }



}
