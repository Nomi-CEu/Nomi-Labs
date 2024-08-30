package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.potion.PotionEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.mixinhelper.InfinitablePotionEffect;

/**
 * Ensures Potion Effects are infinite if duration >= 32767.
 */
@Mixin(value = PotionEffect.class)
public class PotionEffectClientMixin {

    @Inject(method = "setPotionDurationMax", at = @At("HEAD"))
    private void setInfinite(boolean maxDuration, CallbackInfo ci) {
        if (maxDuration) ((InfinitablePotionEffect) this).labs$setInfinite();
    }
}
