package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.draconicevolution.blocks.machines.EnergyPylon;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon;
import com.nomiceu.nomilabs.NomiLabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Checks if the pylon is valid on place, instead of after block update.
 * <br>
 * Prevents instances when replacing of glass is needed.
 */
@Mixin(value = EnergyPylon.class, remap = false)
public abstract class EnergyPylonMixin extends BlockBCore {
    @Override
    @Unique
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (world.getTileEntity(pos) instanceof TileEnergyPylon tile) {
            NomiLabs.LOGGER.info("Success!");
            tile.validateStructure();
        } else NomiLabs.LOGGER.info("Fail!");
    }
}
