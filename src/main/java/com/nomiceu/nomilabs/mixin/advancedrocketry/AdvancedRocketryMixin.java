package com.nomiceu.nomilabs.mixin.advancedrocketry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.libVulpes.api.LibVulpesBlocks;
import zmaster587.libVulpes.event.BucketHandler;

/**
 * Fixes Fluids Appearing with Null Texture, Prevents Bucket -> Tank when Collecting Placed Fluids
 */
@Mixin(value = AdvancedRocketry.class, remap = false)
public class AdvancedRocketryMixin {

    @Redirect(method = "registerBlocks",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/libVulpes/api/LibVulpesBlocks;registerBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"),
              require = 1)
    private <T extends Block> T registerBlocksProperly(T block) {
        if (block instanceof BlockFluidBase) {
            return LibVulpesBlocks.registerBlock(block, null, false);
        }
        return LibVulpesBlocks.registerBlock(block);
    }

    @Redirect(method = "postInit",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/libVulpes/event/BucketHandler;registerBucket(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item;Lnet/minecraftforge/fluids/Fluid;)V"),
              require = 1)
    private void cancelBucketHandlingRegister(BucketHandler instance, Block block, Item item, Fluid fluid) {}
}
