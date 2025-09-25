package com.nomiceu.nomilabs.item;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import com.jaquadro.minecraft.storagedrawers.api.storage.INetworked;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.IFrameable;
import com.jaquadro.minecraft.storagedrawers.block.*;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityTrim;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.MaterialData;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.item.registry.LabsItems;

import eutros.framedcompactdrawers.block.tile.TileControllerCustom;
import eutros.framedcompactdrawers.block.tile.TileSlaveCustom;
import eutros.framedcompactdrawers.registry.ModBlocks;

@Optional.Interface(iface = "com.jaquadro.minecraft.storagedrawers.api.storage.attribute.IFrameable",
                    modid = LabsValues.STORAGE_DRAWERS_MODID)
public class ItemHandFramingTool extends Item implements IFrameable {

    public static final String MAT_SIDE_TAG = "MatS";
    public static final String MAT_TRIM_TAG = "MatT";
    public static final String MAT_FRONT_TAG = "MatF";

    public ItemHandFramingTool(ResourceLocation rl, CreativeTabs tab) {
        setMaxStackSize(1);
        setCreativeTab(tab);
        setRegistryName(rl);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound == null || getItemStackFromKey(tagCompound, MAT_SIDE_TAG).isEmpty()) {
            tooltip.add(translate("tooltip.nomilabs.hand_framing_tool.not_set"));
            return;
        }

        addTooltipItem(tooltip, "tooltip.nomilabs.hand_framing_tool.side",
                getItemStackFromKey(tagCompound, MAT_SIDE_TAG));
        addTooltipItem(tooltip, "tooltip.nomilabs.hand_framing_tool.trim",
                getItemStackFromKey(tagCompound, MAT_TRIM_TAG));
        addTooltipItem(tooltip, "tooltip.nomilabs.hand_framing_tool.front",
                getItemStackFromKey(tagCompound, MAT_FRONT_TAG));
    }

    @SideOnly(Side.CLIENT)
    private void addTooltipItem(@NotNull List<String> tooltip, String translationKey, ItemStack stack) {
        tooltip.add(translate(translationKey, stack.isEmpty() ? "-" : stack.getDisplayName()));
    }

    @Override
    @Optional.Method(modid = LabsValues.STORAGE_DRAWERS_MODID)
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, World world, @NotNull BlockPos pos,
                                               @NotNull EnumHand hand, @NotNull EnumFacing facing,
                                               float hitX, float hitY, float hitZ) {
        // This is to return success if we framed it, but not decorated it
        EnumActionResult actionResult = EnumActionResult.PASS;

        if (world.isAirBlock(pos))
            return actionResult;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof INetworked))
            return actionResult;

        // At this point, further returns should be fail
        actionResult = EnumActionResult.FAIL;

        ItemStack tool = player.getHeldItem(hand);

        // Check if we should make this block a framed one
        if (!isDecorating(Objects.requireNonNull(block.getRegistryName()))) {
            // Make it framed
            var framedResult = makeFramedState(world, pos);
            if (framedResult != null) return framedResult;

            // This should be success, if we framed but not decorated
            actionResult = EnumActionResult.SUCCESS;
        }

        NBTTagCompound tagCompound = tool.getTagCompound();

        if (tagCompound == null)
            return actionResult;

        // Get Decorate Info
        ItemStack matS, matF, matT;

        matS = getItemStackFromKey(tagCompound, MAT_SIDE_TAG);
        if (matS.isEmpty())
            return actionResult;

        matT = getItemStackFromKey(tagCompound, MAT_TRIM_TAG);
        matF = getItemStackFromKey(tagCompound, MAT_FRONT_TAG);

        // Decorate
        MaterialData materialData = getMaterialData(world, pos);
        if (materialData != null) {
            materialData.setSide(matS.copy());
            materialData.setTrim(matT.copy());
            materialData.setFront(matF.copy());
        }

        // Reload Block
        world.markBlockRangeForRenderUpdate(pos, pos);

        return EnumActionResult.SUCCESS;
    }

    private boolean isDecorating(ResourceLocation registryName) {
        return registryName.getNamespace().equals(LabsValues.FRAMED_COMPACT_MODID) ||
                registryName.equals(new ResourceLocation(LabsValues.STORAGE_DRAWERS_MODID, "customdrawers")) ||
                registryName.equals(new ResourceLocation(LabsValues.STORAGE_DRAWERS_MODID, "customtrim"));
    }

    /**
     * Returns null if everything is as planned, or an action state to return instead.
     */
    @Optional.Method(modid = LabsValues.STORAGE_DRAWERS_MODID)
    @Nullable
    private EnumActionResult makeFramedState(World world, BlockPos pos) {
        if (Loader.isModLoaded(LabsValues.FRAMED_COMPACT_MODID) && makeFramedCompactState(world, pos)) return null;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // If framed compact is not loaded (checked above), and we are trying to frame a controller, slave or compacting
        // drawer, exit.
        if (block instanceof BlockCompDrawers || block instanceof BlockController || block instanceof BlockSlave)
            return EnumActionResult.FAIL;

        // Special Case for drawers, to transfer items
        if (block instanceof BlockDrawers) {
            handleDrawerFraming(world, pos, com.jaquadro.minecraft.storagedrawers.core.ModBlocks.customDrawers
                    .getStateFromMeta(block.getMetaFromState(state)));
            return null;
        }

        // Only block that extends INetworked at this point is trims
        IBlockState newState = com.jaquadro.minecraft.storagedrawers.core.ModBlocks.customTrim.getDefaultState();
        world.setBlockState(pos, newState);
        return null;
    }

    /**
     * Returns true if succeeded, false if not
     */
    @Optional.Method(modid = LabsValues.FRAMED_COMPACT_MODID)
    private boolean makeFramedCompactState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockCompDrawers) {
            handleDrawerFraming(world, pos, ModBlocks.framedCompactDrawer.getDefaultState());
            return true;
        }
        IBlockState newState;
        // Meta for controllers are their direction, so read that (Custom Controller's meta is a bit different to normal
        // controller, so -2 to meta is needed)
        if (block instanceof BlockController)
            newState = ModBlocks.framedDrawerController.getStateFromMeta(block.getMetaFromState(state) - 2);
        else if (block instanceof BlockSlave) newState = ModBlocks.framedSlave.getDefaultState();
        else return false;

        world.setBlockState(pos, newState);
        return true;
    }

    @Optional.Method(modid = LabsValues.STORAGE_DRAWERS_MODID)
    private void handleDrawerFraming(World world, BlockPos pos, IBlockState state) {
        var tag = new NBTTagCompound();

        TileEntityDrawers tile = Objects.requireNonNull((TileEntityDrawers) world.getTileEntity(pos));

        // Get nbt (items stored, locked, etc.) + direction
        tile.writeToPortableNBT(tag);
        int direction = tile.getDirection();

        // Set new BlockState
        world.setBlockState(pos, state);

        // Reload tile, to the new block
        tile = Objects.requireNonNull((TileEntityDrawers) world.getTileEntity(pos));

        // Load back nbt + direction
        tile.readFromPortableNBT(tag);
        tile.setDirection(direction);
    }

    @Nullable
    @Optional.Method(modid = LabsValues.STORAGE_DRAWERS_MODID)
    private MaterialData getMaterialData(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (Loader.isModLoaded(LabsValues.FRAMED_COMPACT_MODID)) {
            var data = getMaterialDataFramed(tile);
            if (data != null) return data;
        }

        // Framed Comp Drawers & Drawers
        if (tile instanceof TileEntityDrawers drawers) {
            return drawers.material();
        }

        // Framed Trim
        if (tile instanceof TileEntityTrim trim)
            return trim.material();

        // Tile was null, or didn't inherit from these, aka error
        NomiLabs.LOGGER.fatal("[Hand Framing Tool] Failed to get the material data of tile entity at block pos {}.",
                pos);
        return null;
    }

    /**
     * Don't use this, this is seperated from main method to allow for just storage drawers, without framed compact
     */
    @Nullable
    @Optional.Method(modid = LabsValues.FRAMED_COMPACT_MODID)
    private MaterialData getMaterialDataFramed(TileEntity tile) {
        // Framed Controller
        if (tile instanceof TileControllerCustom controller) {
            return controller.material();
        }

        // Framed Slave
        if (tile instanceof TileSlaveCustom slave) {
            return slave.material();
        }
        return null;
    }

    private ItemStack getItemStackFromKey(NBTTagCompound tagCompound, String key) {
        if (!tagCompound.hasKey(key))
            return ItemStack.EMPTY;
        else
            return new ItemStack(tagCompound.getCompoundTag(key));
    }

    @Override
    @Optional.Method(modid = LabsValues.STORAGE_DRAWERS_MODID)
    public ItemStack decorate(ItemStack itemStack, ItemStack matSide, ItemStack matTrim, ItemStack matFront) {
        ItemStack stack = new ItemStack(LabsItems.HAND_FRAMING_TOOL, 1);
        NBTTagCompound compound = new NBTTagCompound();

        if (!matSide.isEmpty())
            compound.setTag(MAT_SIDE_TAG, getMaterialTag(matSide));

        if (!matTrim.isEmpty())
            compound.setTag(MAT_TRIM_TAG, getMaterialTag(matTrim));

        if (!matFront.isEmpty())
            compound.setTag(MAT_FRONT_TAG, getMaterialTag(matFront));

        stack.setTagCompound(compound);
        return stack;
    }

    private static NBTTagCompound getMaterialTag(@Nonnull ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
        return tag;
    }
}
