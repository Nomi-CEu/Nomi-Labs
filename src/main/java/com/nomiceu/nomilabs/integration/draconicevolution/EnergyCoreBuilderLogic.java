package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EnergyCoreBuilderLogic {
    /**
     * Returns isDead.
     */
    public static boolean buildWorklist(TileEnergyStorageCore core, EntityPlayer player, Map<BlockPos, IBlockState> workList, LinkedList<BlockPos> workOrder) {
        Map<BlockPos, BlockStates> structureBlocks = getStructureBlocks(core);

        World world = core.getWorld();
        for (BlockPos key : structureBlocks.keySet()) {
            BlockStates neededStates = structureBlocks.get(key);
            IBlockState neededState = neededStates.getDefault();
            if (world.isAirBlock(key)) {
                workList.put(key, neededState);
                continue;
            }
            IBlockState state = world.getBlockState(key);
            if (!DraconicHelpers.validState(neededStates, state, false)) {
                player.sendMessage(new TextComponentTranslation("ecore.de.assemble_found_invalid.txt", state.getBlock().getLocalizedName(), key.toString()).setStyle(new Style().setColor(TextFormatting.RED)));
                return true;
            }
        }

        workOrder.addAll(workList.keySet());
        workOrder.sort(Comparator.comparingInt(value -> (int) value.distanceSq(core.getPos())));
        return false;
    }

    @NotNull
    private static Map<BlockPos, BlockStates> getStructureBlocks(TileEnergyStorageCore core) {
        BlockStateEnergyCoreStructure structure = (BlockStateEnergyCoreStructure) core.coreStructure;
        BlockStateMultiblockStorage storage = structure.getStorageForTier(core.tier.value);
        BlockPos start = core.getPos().add(structure.getCoreOffset(core.tier.value));
        Map<BlockPos, BlockStates> structureBlocks = new HashMap<>();
        storage.forEachBlockStates(start, structureBlocks::put);
        return structureBlocks;
    }

    /**
     * Returns isDead.
     */
    public static boolean updateProcess(World world, EntityPlayer player, Map<BlockPos, IBlockState> workList, LinkedList<BlockPos> workOrder) {
        if (workOrder.isEmpty() || player.isDead) {
            return true;
        }

        BlockPos pos = workOrder.poll();
        IBlockState state = workList.get(pos);
        if (!world.isAirBlock(pos)) {
            if (world.getBlockState(pos).getBlock() == state.getBlock()) {
                return false;
            }
            player.sendMessage(new TextComponentTranslation("ecore.de.assemble_error_expected_air.txt", pos.toString()).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
        }

        ItemStack required = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));

        if (player.capabilities.isCreativeMode || extractItem(required, player)) {
            world.setBlockState(pos, state);
            SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        }
        else {
            player.sendMessage(new TextComponentTranslation("ecore.de.assemble_missing_required_new.txt", required.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
        }
        return false;
    }

    private static boolean extractItem(ItemStack toExtract, EntityPlayer player) {
        IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null) return false;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack inSlot = handler.getStackInSlot(i);
            if (!inSlot.isEmpty() && inSlot.getItem().equals(toExtract.getItem()) && inSlot.getMetadata() == toExtract.getMetadata()) {
                ItemStack extracted = handler.extractItem(i, 1, false);
                if (!extracted.isEmpty() && extracted.getItem().equals(toExtract.getItem()) && extracted.getMetadata() == toExtract.getMetadata()) {
                    return true;
                }
            }
        }
        return false;
    }

}
