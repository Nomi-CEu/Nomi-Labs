package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.mixinhelper.InfinitablePotionEffect;

/**
 * Allows a Potion Effect to be set as infinite.
 */
@Mixin(value = PotionEffect.class)
public class PotionEffectMixin implements InfinitablePotionEffect {

    @Unique
    private static final String LABS_INFINITE_KEY = "Infinite";

    @Shadow
    private int duration;

    @Unique
    private boolean labs$infinite = false;

    @Override
    @Unique
    public void labs$setInfinite() {
        labs$infinite = true;
        duration = Short.MAX_VALUE;
    }

    @Override
    @Unique
    public boolean labs$isInfinite() {
        return labs$infinite;
    }

    @Inject(method = "combine", at = @At("HEAD"))
    private void combineInfinite(PotionEffect other, CallbackInfo ci) {
        if (labs$infinite || ((InfinitablePotionEffect) other).labs$isInfinite())
            labs$infinite = true;
    }

    @Inject(method = "deincrementDuration", at = @At("HEAD"), cancellable = true)
    private void cancelDurationIfInfinite(CallbackInfoReturnable<Integer> cir) {
        if (labs$infinite) cir.setReturnValue(duration);
    }

    @Inject(method = "writeCustomPotionEffectToNBT", at = @At("RETURN"), cancellable = true)
    private void writeInfinite(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> cir) {
        nbt.setBoolean(LABS_INFINITE_KEY, labs$infinite);
        cir.setReturnValue(nbt);
    }

    @Inject(method = "readCurativeItems", at = @At("HEAD"), remap = false)
    private static void readInfinite(PotionEffect effect, NBTTagCompound nbt,
                                     CallbackInfoReturnable<PotionEffect> cir) {
        if (nbt.getBoolean(LABS_INFINITE_KEY)) ((InfinitablePotionEffect) effect).labs$setInfinite();
    }
}
