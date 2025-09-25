package com.nomiceu.nomilabs.mixin.ae2fc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.glodblock.github.common.tile.TileUltimateEncoder;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;

/**
 * Saves Incl Non Consume settings to tile.
 */
@Mixin(value = TileUltimateEncoder.class, remap = false)
public class TileUltimateEncoderMixin implements InclNonConsumeSettable {

    @Unique
    private boolean labs$inclNonConsume = true;

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void readNonConsume(NBTTagCompound data, CallbackInfo ci) {
        if (data.hasKey("labs$nonConsume", Constants.NBT.TAG_BYTE)) {
            labs$inclNonConsume = data.getBoolean("labs$nonConsume");
        } else labs$inclNonConsume = true;
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void writeNonConsume(NBTTagCompound data, CallbackInfoReturnable<NBTTagCompound> cir) {
        data.setBoolean("labs$nonConsume", labs$inclNonConsume);
    }

    @Override
    public boolean labs$inclNonConsume() {
        return labs$inclNonConsume;
    }

    @Override
    public void labs$setInclNonConsume(boolean inclNonConsume) {
        labs$inclNonConsume = inclNonConsume;
    }
}
