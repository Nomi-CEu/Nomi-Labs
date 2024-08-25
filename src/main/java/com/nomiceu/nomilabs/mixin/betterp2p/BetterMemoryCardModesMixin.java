package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nomiceu.nomilabs.NomiLabs;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

/**
 * Makes Unbind always Appear Last in `.next` calls.
 */
@Mixin(value = BetterMemoryCardModes.class, remap = false)
public class BetterMemoryCardModesMixin {

    @Unique
    private static BiMap<Integer, BetterMemoryCardModes> LABS_ORDER_CACHE;

    @Inject(method = "next", at = @At("HEAD"), cancellable = true)
    private void changeNextOrder(boolean reverse, CallbackInfoReturnable<BetterMemoryCardModes> cir) {
        if (LABS_ORDER_CACHE == null) {
            LABS_ORDER_CACHE = HashBiMap.create();

            var modes = BetterMemoryCardModes.values();
            for (var mode : modes) {
                if (mode != BetterMemoryCardModes.UNBIND)
                    LABS_ORDER_CACHE.put(LABS_ORDER_CACHE.size(), mode);
            }

            LABS_ORDER_CACHE.put(LABS_ORDER_CACHE.size(), BetterMemoryCardModes.UNBIND);

            NomiLabs.LOGGER.debug("[BetterMemoryCardModes] Created Labs Order Cache: {}", LABS_ORDER_CACHE);
        }

        //noinspection SuspiciousMethodCalls
        int current = LABS_ORDER_CACHE.inverse().get(this);
        int toGet = (reverse ? current - 1 : current + 1) % LABS_ORDER_CACHE.size();
        BetterMemoryCardModes result = LABS_ORDER_CACHE.get(toGet);

        if (result != null)
            cir.setReturnValue(result);
        else
            NomiLabs.LOGGER.error("[BetterMemoryCardModes] Next Modifier returned null for Index {}!", toGet);
    }
}
