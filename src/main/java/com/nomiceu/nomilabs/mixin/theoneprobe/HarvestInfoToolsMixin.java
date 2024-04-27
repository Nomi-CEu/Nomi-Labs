package com.nomiceu.nomilabs.mixin.theoneprobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.providers.HarvestInfoTools;

/**
 * Fixes Architecture Craft Shapes' Displays on TOP.
 * As far as I can see, no API to change this outside of mixins.
 */
@Mixin(value = HarvestInfoTools.class, remap = false)
public class HarvestInfoToolsMixin {

    @Shadow
    @Final
    private static HashMap<String, ItemStack> testTools;

    /**
     * Only change the main `showHarvestInfo` method.
     * This is called using the normal configs. The other two methods are only called on some specific configurations.
     * <p>
     * `showCanBeHarvested` displays correct results.
     * `showHarvestLevel` does not provide world or position.
     */
    @Inject(method = "showHarvestInfo",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/block/Block;getHarvestTool(Lnet/minecraft/block/state/IBlockState;)Ljava/lang/String;"))
    private static void getHarvestToolForArchitectureCraft(IProbeInfo probeInfo, World world, BlockPos pos, Block block,
                                                           IBlockState blockState, EntityPlayer player, CallbackInfo ci,
                                                           @Local LocalRef<String> harvestTool) {
        if (!Loader.isModLoaded(LabsValues.ARCHITECTURE_MODID) ||
                !LabsConfig.modIntegration.enableArchitectureCraftIntegration || isNotShapeForArchitectureCraft(block))
            return;

        var baseBlockState = getBaseBlockStateForArchitectureCraft(world, pos);
        if (baseBlockState == null) return;

        var retrievedHarvestTool = baseBlockState.getBlock().getHarvestTool(baseBlockState);

        // From the Original Function
        if (retrievedHarvestTool == null) {
            // The block doesn't have an explicitly-set harvest tool, so test our wooden tools against it
            float blockHardness = baseBlockState.getBlockHardness(world, pos);
            if (blockHardness > 0f) {
                for (Map.Entry<String, ItemStack> testToolEntry : testTools.entrySet()) {
                    // loop through our test tools until we find a winner.
                    ItemStack testTool = testToolEntry.getValue();

                    if (testTool != null && testTool.getItem() instanceof ItemTool toolItem) {
                        if (testTool.getDestroySpeed(baseBlockState) >= toolItem.toolMaterial.getEfficiency()) {
                            retrievedHarvestTool = testToolEntry.getKey();
                            break;
                        }
                    }
                }
            }
        }
        harvestTool.set(retrievedHarvestTool);
    }

    @Inject(method = "showHarvestInfo",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/block/Block;getHarvestLevel(Lnet/minecraft/block/state/IBlockState;)I"))
    private static void getHarvestLevelForArchitectureCraft(IProbeInfo probeInfo, World world, BlockPos pos,
                                                            Block block, IBlockState blockState, EntityPlayer player,
                                                            CallbackInfo ci, @Local LocalIntRef harvestLevel) {
        if (!Loader.isModLoaded(LabsValues.ARCHITECTURE_MODID) ||
                !LabsConfig.modIntegration.enableArchitectureCraftIntegration || isNotShapeForArchitectureCraft(block))
            return;

        var baseBlockState = getBaseBlockStateForArchitectureCraft(world, pos);
        if (baseBlockState == null) return;
        harvestLevel.set(baseBlockState.getBlock().getHarvestLevel(baseBlockState));
    }

    /**
     * Use Reflection to prevent hard dep. Equivalent to `!(block instanceof BlockShape)`.
     */
    @Unique
    private static boolean isNotShapeForArchitectureCraft(Block block) {
        try {
            var shapeClass = Class.forName("com.elytradev.architecture.common.block.BlockShape");
            return !shapeClass.isInstance(block);
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    /**
     * Use Reflection to prevent hard dep. Equivalent to `TileShape.get(world, pos).getBaseBlockState()`.
     */
    @Unique
    @Nullable
    private static IBlockState getBaseBlockStateForArchitectureCraft(World world, BlockPos pos) {
        try {
            var tileShapeClass = Class.forName("com.elytradev.architecture.common.tile.TileShape");
            var getTileMethod = tileShapeClass.getDeclaredMethod("get", IBlockAccess.class, BlockPos.class);
            var tile = getTileMethod.invoke(null, world, pos);
            if (tile == null) return null;

            var getBaseBlockStateMethod = tile.getClass().getDeclaredMethod("getBaseBlockState");
            return (IBlockState) getBaseBlockStateMethod.invoke(tile);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            return null;
        }
    }
}
