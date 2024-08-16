package com.nomiceu.nomilabs.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes Beds Change Time and Weather in All Dimensions.
 * <p>
 * Makes Beds work in Void Dim.
 */
@Mixin(WorldServer.class)
public class WorldServerMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Redirect(method = "tick",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;setWorldTime(J)V"),
              slice = @Slice(from = @At(value = "INVOKE",
                                        target = "Lnet/minecraft/world/WorldServer;areAllPlayersAsleep()Z"),
                             to = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;wakeAllPlayers()V")),
              require = 1)
    private void changeTimeInAllDimensions(WorldServer instance, long time) {
        for (var world : server.worlds) {
            world.setWorldTime(time);
        }
    }

    @Inject(method = "resetRainAndThunder", at = @At("HEAD"))
    private void resetWeatherInAllDimensions(CallbackInfo ci) {
        for (var world : server.worlds) {
            world.provider.resetRainAndThunder();
        }
    }
}
