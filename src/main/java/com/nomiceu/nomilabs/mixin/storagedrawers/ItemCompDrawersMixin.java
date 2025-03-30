package com.nomiceu.nomilabs.mixin.storagedrawers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.item.ItemCompDrawers;
import com.nomiceu.nomilabs.integration.storagedrawers.CustomUpgradeHandler;

/**
 * Reads from Labs Upgrade NBT.
 * <p>
 * ItemCompDrawers doesn't inherit from ItemDrawers.
 */
@Mixin(value = ItemCompDrawers.class, remap = false)
public class ItemCompDrawersMixin {

    @Inject(method = "placeBlockAt",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/World;getTileEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;"),
            remap = true,
            require = 1)
    private void handleCustomNBT(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                 float hitX, float hitY, float hitZ, IBlockState newState,
                                 CallbackInfoReturnable<Boolean> cir) {
        var te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityDrawers teDrawers)) return;
        CustomUpgradeHandler.addCustomUpgradesToTile(teDrawers, stack.getTagCompound());
    }
}
