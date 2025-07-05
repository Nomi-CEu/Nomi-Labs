package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

/**
 * Makes Unbind always appear Last in `.next` calls. Also removes 'Copy Input to Output',
 * as it is redundant with 'Add as Output'.
 */
@Mixin(value = BetterMemoryCardModes.class, remap = false)
public class BetterMemoryCardModesMixin {

    @Unique
    private static BiMap<Integer, BetterMemoryCardModes> LABS_ORDER_CACHE;

    @Inject(method = "next", at = @At("HEAD"), cancellable = true)
    private void changeNextOrder(boolean reverse, CallbackInfoReturnable<BetterMemoryCardModes> cir) {
        if (LABS_ORDER_CACHE == null) {
            LABS_ORDER_CACHE = HashBiMap.create();

            LABS_ORDER_CACHE.put(0, LabsBetterMemoryCardModes.ADD_AS_INPUT);
            LABS_ORDER_CACHE.put(1, LabsBetterMemoryCardModes.ADD_AS_OUTPUT);
            LABS_ORDER_CACHE.put(2, BetterMemoryCardModes.INPUT);
            LABS_ORDER_CACHE.put(3, BetterMemoryCardModes.OUTPUT);
            LABS_ORDER_CACHE.put(4, BetterMemoryCardModes.UNBIND);
        }

        // noinspection SuspiciousMethodCalls
        Integer current = LABS_ORDER_CACHE.inverse().get(this);

        if (current == null) {
            NomiLabs.LOGGER.error("[BetterMemoryCardModes] Current Index returned null for Mode {}!", this);
            NomiLabs.LOGGER.error("[BetterMemoryCardModes] This may simply be a legacy card, if mode was COPY.");
            return;
        }

        int toGet;
        if (reverse) {
            if (current == 0) toGet = LABS_ORDER_CACHE.size() - 1;
            else toGet = current - 1;
        } else
            toGet = (current + 1) % LABS_ORDER_CACHE.size();

        BetterMemoryCardModes result = LABS_ORDER_CACHE.get(toGet);

        if (result != null)
            cir.setReturnValue(result);
        else
            NomiLabs.LOGGER.error("[BetterMemoryCardModes] Next Modifier returned null for Index {}!", toGet);
    }
}
