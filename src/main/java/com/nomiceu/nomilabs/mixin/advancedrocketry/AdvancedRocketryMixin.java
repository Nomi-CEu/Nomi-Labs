package com.nomiceu.nomilabs.mixin.advancedrocketry;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.BlockFluidBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.libVulpes.api.LibVulpesBlocks;

@Mixin(value = AdvancedRocketry.class, remap = false)
public class AdvancedRocketryMixin {

    @Redirect(method = "registerBlocks",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/libVulpes/api/LibVulpesBlocks;registerBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"))
    public <T extends Block> T registerBlocksProperly(T block) {
        if (block instanceof BlockFluidBase) {
            return LibVulpesBlocks.registerBlock(block, null, false);
        }
        return LibVulpesBlocks.registerBlock(block);
    }
}
