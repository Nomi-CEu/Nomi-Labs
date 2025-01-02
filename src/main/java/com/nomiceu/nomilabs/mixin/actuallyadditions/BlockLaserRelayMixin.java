package com.nomiceu.nomilabs.mixin.actuallyadditions;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableSet;
import com.nomiceu.nomilabs.config.LabsConfig;

import de.ellpeck.actuallyadditions.api.laser.Network;
import de.ellpeck.actuallyadditions.mod.blocks.BlockLaserRelay;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityLaserRelay;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;

@Mixin(value = BlockLaserRelay.class, remap = false)
public class BlockLaserRelayMixin {

    // Yes, we are comparing the item with the two screwdrivers' registry names.
    // For now, this will do. It works, and is simple.
    @Unique
    private static final Set<ResourceLocation> labs$screwdriverRls = ImmutableSet
            .of(new ResourceLocation("gregtech", "screwdriver"), new ResourceLocation("gregtech", "screwdriver_lv"));

    @Inject(method = "onBlockActivated", at = @At("HEAD"), remap = true, cancellable = true)
    private void detectGtScrewdrivers(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                      EnumFacing par6, float par7, float par8, float par9,
                                      CallbackInfoReturnable<Boolean> cir) {
        if (!LabsConfig.modIntegration.gtScrewdriveAARelays) return;

        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof TileEntityLaserRelay laser) || !StackUtil.isValid(stack)) return;

        if (!labs$screwdriverRls.contains(stack.getItem().getRegistryName())) return;

        if (!world.isRemote) {
            laser.onCompassAction(player);
            Network network = laser.getNetwork();
            if (network != null) {
                ++network.changeAmount;
            }

            laser.markDirty();
            laser.sendUpdate();
        }

        cir.setReturnValue(true);
    }
}
