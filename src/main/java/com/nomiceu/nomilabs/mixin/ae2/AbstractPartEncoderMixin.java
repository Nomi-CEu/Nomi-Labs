package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;

import appeng.parts.reporting.AbstractPartEncoder;

/**
 * Saves Non-Consume settings to part.
 */
@Mixin(value = AbstractPartEncoder.class, remap = false)
public class AbstractPartEncoderMixin implements InclNonConsumeSettable {

    @Unique
    private boolean labs$inclNonConsume = true;

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void readNonConsume(NBTTagCompound data, CallbackInfo ci) {
        if (data.hasKey("labs$nonConsume", Constants.NBT.TAG_BYTE)) {
            labs$inclNonConsume = data.getBoolean("labs$nonConsume");
        } else labs$inclNonConsume = true;
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void writeNonConsume(NBTTagCompound data, CallbackInfo ci) {
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
