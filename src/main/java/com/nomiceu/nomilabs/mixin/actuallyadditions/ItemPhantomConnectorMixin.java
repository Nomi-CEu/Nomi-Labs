package com.nomiceu.nomilabs.mixin.actuallyadditions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.ellpeck.actuallyadditions.api.tile.IPhantomTile;
import de.ellpeck.actuallyadditions.mod.items.ItemPhantomConnector;

/**
 * Gives error message if the phantom connector's target pos and use pos are in different dimensions.
 */
@Mixin(value = ItemPhantomConnector.class, remap = false)
public abstract class ItemPhantomConnectorMixin {

    @Inject(method = "onItemUse", at = @At("HEAD"), remap = true, cancellable = true)
    private void dimCheck(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing par7, float par8,
                          float par9, float par10, CallbackInfoReturnable<EnumActionResult> cir) {
        if (world.isRemote) return;

        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null || !tag.hasKey("WorldOfTileStored", Constants.NBT.TAG_ANY_NUMERIC)) return;

        if (world.getTileEntity(pos) instanceof IPhantomTile &&
                tag.getInteger("WorldOfTileStored") != world.provider.getDimension()) {
            player.sendStatusMessage(new TextComponentTranslation("aa.message.phantom_connector.different_dim"), true);
            cir.setReturnValue(EnumActionResult.FAIL);
        }
    }
}
