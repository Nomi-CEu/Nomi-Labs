package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.common.items.armor.QuarkTechSuite;

/**
 * Fixes (non-helmet) quarktech suite armor causing other permanent night vision armor
 * (nanomuscle, EnderIO, Excitation Coil) to not work.
 */
@Mixin(value = QuarkTechSuite.class, remap = false)
public class QuarkTechSuiteMixin {

    @Redirect(method = "onArmorTick",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/common/items/armor/QuarkTechSuite;disableNightVision(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Z)V",
                       ordinal = 0))
    private void cancelDisableNightVision(World world, EntityPlayer player, boolean sendMsg) {}
}
