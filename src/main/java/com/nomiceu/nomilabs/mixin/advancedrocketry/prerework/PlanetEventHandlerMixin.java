package com.nomiceu.nomilabs.mixin.advancedrocketry.prerework;

import net.minecraftforge.event.world.WorldEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import zmaster587.advancedRocketry.api.ARConfiguration;
import zmaster587.advancedRocketry.client.render.planet.RenderPlanetarySky;
import zmaster587.advancedRocketry.event.PlanetEventHandler;

/**
 * Applies <a href="https://github.com/Advanced-Rocketry/AdvancedRocketry/pull/2369">AdvRocketry #2369</a>.
 */
@Mixin(value = PlanetEventHandler.class, remap = false)
public class PlanetEventHandlerMixin {

    @Inject(method = "worldLoadEvent", at = @At("HEAD"), cancellable = true)
    private void fixSkyboxLoading(WorldEvent.Load event, CallbackInfo ci) {
        if (!event.getWorld().isRemote) return;

        if (ARConfiguration.getCurrentConfig().skyOverride)
            event.getWorld().provider.setSkyRenderer(new RenderPlanetarySky());

        ci.cancel();
    }
}
