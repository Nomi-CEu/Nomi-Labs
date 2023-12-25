package com.nomiceu.nomilabs.item;

import com.jaquadro.minecraft.storagedrawers.api.storage.INetworked;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.IFrameable;
import com.jaquadro.minecraft.storagedrawers.block.*;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityTrim;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.MaterialData;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import eutros.framedcompactdrawers.block.tile.TileControllerCustom;
import eutros.framedcompactdrawers.block.tile.TileSlaveCustom;
import eutros.framedcompactdrawers.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemHandFramingTool extends Item implements IFrameable {
    public static final String MAT_SIDE_TAG = "MatS";
    public static final String MAT_TRIM_TAG = "MatT";
    public static final String MAT_FRONT_TAG = "MatF";

    public ItemHandFramingTool(ResourceLocation rl, CreativeTabs tab) {
        setMaxStackSize(1);
        setCreativeTab(tab);
        setRegistryName(rl);

        // Add Description
        addDescription();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound == null || getItemStackFromKey(tagCompound, MAT_SIDE_TAG).isEmpty()){
            tooltip.add(I18n.format("tooltip.nomilabs.hand_framing_tool.not_set"));
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
        tooltip.add(I18n.format(translationKey, stack.isEmpty() ? "-" : stack.getDisplayName()));
    }

    @Override
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
        if (!isDecorating(Objects.requireNonNull(block.getRegistryName()))){
            // Make it framed
            makeFramedState(world, pos);

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

        matT= getItemStackFromKey(tagCompound, MAT_TRIM_TAG);
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
        String registryString = registryName.toString();

        return registryName.getNamespace().equals("framedcompactdrawers")
               || registryString.equals("storagedrawers:customdrawers")
               || registryString.equals("storagedrawers:customtrim");
    }

    @SuppressWarnings("deprecation")
    private void makeFramedState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        NBTTagCompound tag = new NBTTagCompound();

        IBlockState newState;

        // Special Case for drawers, to transfer items
        if (block instanceof BlockDrawers){
            TileEntityDrawers tile = Objects.requireNonNull((TileEntityDrawers) world.getTileEntity(pos));

            // Get nbt (items stored, locked, etc.) + direction
            tile.writeToPortableNBT(tag);
            int direction = tile.getDirection();

            // Only block that extends BlockDrawers at this point is drawers and framed drawers
            newState = block instanceof BlockCompDrawers ? ModBlocks.framedCompactDrawer.getDefaultState()
                        : com.jaquadro.minecraft.storagedrawers.core.ModBlocks.customDrawers.getStateFromMeta(block.getMetaFromState(state));

            // Set new BlockState
            world.setBlockState(pos, newState);

            // Reload tile, to the new block
            tile = Objects.requireNonNull((TileEntityDrawers) world.getTileEntity(pos));

            // Load back nbt + direction
            tile.readFromPortableNBT(tag);
            tile.setDirection(direction);
            return;
        }

        // Only block that and extends INetworked at this point is controllers, slaves, and trims
        Block newBlock = block instanceof BlockController ? ModBlocks.framedDrawerController:
                        block instanceof BlockSlave ? ModBlocks.framedSlave :
                        com.jaquadro.minecraft.storagedrawers.core.ModBlocks.customTrim;

        // Meta for controllers are their direction, so read that (Custom Controller's meta is a bit different to normal controller, so -2 to meta is needed)
        newState = block instanceof BlockController ? newBlock.getStateFromMeta(block.getMetaFromState(state) - 2)
                : newBlock.getDefaultState();

        world.setBlockState(pos, newState);
        
    }

    @Nullable
    private MaterialData getMaterialData(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        // Framed Controller
        if (tile instanceof TileControllerCustom controller) {
            return controller.material();
        }

        // Framed Slave
        if (tile instanceof TileSlaveCustom slave) {
            return slave.material();
        }

        // Framed Comp Drawers & Drawers
        if (tile instanceof TileEntityDrawers drawers) {
            return drawers.material();
        }

        // Framed Trim
        if (tile instanceof TileEntityTrim trim)
            return trim.material();

        // Tile was null, or didn't inherit from these, aka error
        NomiLabs.LOGGER.fatal("[Hand Framing Tool] Failed to get the material data of tile entity at block pos {}.", pos);
        return null;
    }

    private ItemStack getItemStackFromKey(NBTTagCompound tagCompound, String key) {
        if (!tagCompound.hasKey(key))
            return ItemStack.EMPTY;

        else {
            return new ItemStack(tagCompound.getCompoundTag(key));
        }
    }

    @Override
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

    public void addDescription() {
        JEIPlugin.addDescription(new ItemStack(this),
                "item.nomilabs.hand_framing_tool.desc1",
                "item.nomilabs.hand_framing_tool.desc2",
                "item.nomilabs.hand_framing_tool.desc3",
                "item.nomilabs.hand_framing_tool.desc4",
                "item.nomilabs.hand_framing_tool.desc5",
                "item.nomilabs.hand_framing_tool.desc6",
                "item.nomilabs.hand_framing_tool.desc7");
    }
}