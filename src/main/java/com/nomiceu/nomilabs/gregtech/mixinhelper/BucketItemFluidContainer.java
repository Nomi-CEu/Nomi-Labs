package com.nomiceu.nomilabs.gregtech.mixinhelper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

import org.jetbrains.annotations.Nullable;

import gregtech.api.items.metaitem.stats.IItemBehaviour;
import gregtech.api.items.metaitem.stats.ItemFluidContainer;
import gregtech.api.util.GTUtility;

/**
 * Adapted from <a href="https://github.com/GregTechCEu/GregTech/pull/2660">GTCEu #2660</a>.
 */
public class BucketItemFluidContainer extends ItemFluidContainer implements IItemBehaviour {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        RayTraceResult result = rayTrace(world, player, true);
        if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) return pass(stack);

        ItemStack cellStack = GTUtility.copy(1, stack);
        BlockPos tracedPos = result.getBlockPos();

        FluidActionResult filledResult = FluidUtil.tryPickUpFluid(cellStack, player, world, tracedPos, result.sideHit);
        if (filledResult.isSuccess()) {
            addToPlayerInventory(stack, filledResult.getResult(), player, hand);
            return success(stack);
        }

        var fluid = FluidUtil.getFluidContained(cellStack);
        if (fluid == null) return pass(stack);

        // Recalculate ray trace, ignoring fluids
        result = rayTrace(world, player, false);
        if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) return pass(stack);

        tracedPos = result.getBlockPos();
        BlockPos placePos = tracedPos.offset(result.sideHit);

        if (!world.isBlockModifiable(player, tracedPos) || !player.canPlayerEdit(placePos, result.sideHit, cellStack))
            return pass(stack);

        FluidActionResult emptiedResult = FluidUtil.tryPlaceFluid(player, world, placePos, cellStack, fluid);
        if (emptiedResult.isSuccess()) {
            addToPlayerInventory(stack, emptiedResult.getResult(), player, hand);
            return success(stack);
        }
        return pass(stack);
    }

    // copied and adapted from Item.java
    @Nullable
    private static RayTraceResult rayTrace(World worldIn, EntityPlayer player, boolean useFluids) {
        Vec3d lookPos = player.getPositionVector()
                .add(0, player.getEyeHeight(), 0);

        Vec3d lookOffset = player.getLookVec()
                .scale(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

        return worldIn.rayTraceBlocks(lookPos, lookPos.add(lookOffset),
                useFluids, !useFluids, false);
    }

    private void addToPlayerInventory(ItemStack playerStack, ItemStack resultStack, EntityPlayer player,
                                      EnumHand hand) {
        if (player.capabilities.isCreativeMode) return;

        if (playerStack.getCount() > resultStack.getCount()) {
            playerStack.shrink(resultStack.getCount());
            if (!player.inventory.addItemStackToInventory(resultStack) && !player.world.isRemote) {
                EntityItem dropItem = player.entityDropItem(resultStack, 0);
                if (dropItem != null) dropItem.setPickupDelay(0);
            }
        } else {
            player.setHeldItem(hand, resultStack);
        }
    }

    /* Util */
    private ActionResult<ItemStack> pass(ItemStack stack) {
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    private ActionResult<ItemStack> success(ItemStack stack) {
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
