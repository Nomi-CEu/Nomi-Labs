package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsLangKeyWithArgsElement;

import appeng.integration.modules.theoneprobe.tile.PowerStorageInfoProvider;
import appeng.tile.AEBaseTile;
import appeng.tile.misc.TileInscriber;
import appeng.tile.storage.TileChest;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

/**
 * Properly localizes a TOP string; using a new lang key (original one has a typo).
 * Stops inscribers & chests from showing power info (power doesn't change)
 */
@Mixin(value = PowerStorageInfoProvider.class, remap = false)
public class PowerStorageInfoProviderMixin {

    @WrapMethod(method = "addProbeInfo")
    private void checkInscriber(AEBaseTile tile, ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                                IBlockState blockState, IProbeHitData data, Operation<Void> original) {
        if (tile instanceof TileInscriber || tile instanceof TileChest) return;

        original.call(tile, mode, probeInfo, player, world, blockState, data);
    }

    @Redirect(method = "addProbeInfo",
              at = @At(value = "INVOKE",
                       target = "Lmcjty/theoneprobe/api/IProbeInfo;text(Ljava/lang/String;)Lmcjty/theoneprobe/api/IProbeInfo;"),
              require = 1)
    private IProbeInfo properlyAddText(IProbeInfo instance, String s, @Local(ordinal = 0) String formatCurrentPower,
                                       @Local(ordinal = 1) String formatMaxPower) {
        return instance.element(new LabsLangKeyWithArgsElement("theoneprobe.appliedenergistics2.stored_energy_new",
                formatCurrentPower, formatMaxPower));
    }
}
