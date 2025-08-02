package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.network.PacketBuffer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.client.particle.GTLaserBeamParticle;
import gregtech.common.ConfigHolder;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityAssemblyLine;

/**
 * Allows disabling assembly line particles on dedicated servers.
 */
@Mixin(value = MetaTileEntityAssemblyLine.class, remap = false)
public class MetaTileEntityAssemblyLineClientMixin {

    @Shadow
    private int beamCount;

    @Shadow
    private int beamTime;

    @Shadow
    private GTLaserBeamParticle[][] beamParticles;

    @Inject(method = "readParticles", at = @At("HEAD"), cancellable = true)
    private void cancelParticles(PacketBuffer buf, CallbackInfo ci) {
        if (ConfigHolder.client.shader.assemblyLineParticles) return;

        // Still read data, just to avoid buffer issues
        beamCount = buf.readVarInt();
        beamTime = buf.readVarInt();

        // Invalidate existing particles
        if (beamParticles != null) {
            for (GTLaserBeamParticle[] particles : beamParticles) {
                if (particles != null) {
                    for (GTLaserBeamParticle particle : particles) {
                        if (particle != null)
                            particle.setExpired();
                    }
                }
            }
            beamParticles = null;
        }

        ci.cancel();
    }
}
