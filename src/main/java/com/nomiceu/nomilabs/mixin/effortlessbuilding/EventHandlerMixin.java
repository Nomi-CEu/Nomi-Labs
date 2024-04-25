package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.effortlessbuilding.EffortlessEventHandler;

import nl.requios.effortlessbuilding.EventHandler;

/**
 * Cancel Original Handling of Block Placed Event, Now Done in {@link EffortlessEventHandler#onPlayerInteract}
 */
@Mixin(value = EventHandler.class, remap = false)
public class EventHandlerMixin {

    @Inject(method = "onBlockPlaced", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private static void cancelOriginalHandling(BlockEvent.PlaceEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
