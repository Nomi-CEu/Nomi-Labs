package com.nomiceu.nomilabs.mixin.ae2;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseTileBlock;
import appeng.tile.AEBaseTile;
import appeng.util.SettingsFrom;

@Mixin(value = AEBaseTileBlock.class, remap = false)
public abstract class AEBaseTileBlockMixin extends AEBaseBlock {

    @Shadow
    @Nullable
    public abstract <T extends AEBaseTile> T getTileEntity(IBlockAccess w, BlockPos pos);

    /**
     * Default Ignored Constructor
     */
    private AEBaseTileBlockMixin(Material mat) {
        super(mat);
    }

    @Override
    @Unique
    @NotNull
    public ItemStack getPickBlock(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world,
                                  @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        ItemStack baseStack = super.getPickBlock(state, target, world, pos, player);

        AEBaseTile tile = getTileEntity(world, pos);
        if (tile == null) return baseStack;

        NBTTagCompound tag = tile.downloadSettings(SettingsFrom.DISMANTLE_ITEM);
        baseStack.setTagCompound(tag);
        return baseStack;
    }
}
