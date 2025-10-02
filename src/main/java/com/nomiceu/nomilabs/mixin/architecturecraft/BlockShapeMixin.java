package com.nomiceu.nomilabs.mixin.architecturecraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.elytradev.architecture.common.block.BlockArchitecture;
import com.elytradev.architecture.common.block.BlockShape;
import com.elytradev.architecture.common.tile.TileShape;
import com.llamalad7.mixinextras.sugar.Local;

/**
 * Improves Determining Whether the Shape can be Harvested, and the Hardness of the Shape.
 */
@Mixin(value = BlockShape.class, remap = false)
public class BlockShapeMixin extends BlockArchitecture<TileShape> {

    /**
     * Default Ignored Constructor
     */
    private BlockShapeMixin(Material material) {
        super(material);
    }

    @Inject(method = "acBlockStrength",
            at = @At(value = "INVOKE",
                     target = "Lcom/elytradev/architecture/common/block/BlockShape;acCanHarvestBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;)Z"),
            cancellable = true)
    private static void baseBlockStrength(IBlockState state, EntityPlayer player, World world, BlockPos pos,
                                          CallbackInfoReturnable<Float> cir, @Local(ordinal = 1) float strength) {
        cir.setReturnValue(state.getBlock().canHarvestBlock(world, pos, player) ? strength / 100.0F : strength / 30.0F);
    }

    /**
     * Modified from
     * {@link ForgeHooks#canHarvestBlock(Block, EntityPlayer, IBlockAccess, BlockPos)}
     */
    @SuppressWarnings("DuplicatedCode")
    @Unique
    @Override
    public boolean canHarvestBlock(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        var te = TileShape.get(world, pos);
        if (te == null) return super.canHarvestBlock(world, pos, player);

        var baseState = te.getBaseBlockState();
        var baseBlock = baseState.getBlock();

        if (baseState.getMaterial().isToolNotRequired()) {
            return true;
        }

        ItemStack stack = player.getHeldItemMainhand();
        String tool = baseBlock.getHarvestTool(baseState);
        if (stack.isEmpty() || tool == null)
            return player.canHarvestBlock(baseState);

        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, baseState);
        if (toolLevel < 0)
            return player.canHarvestBlock(baseState);

        return toolLevel >= baseBlock.getHarvestLevel(baseState);
    }
}
