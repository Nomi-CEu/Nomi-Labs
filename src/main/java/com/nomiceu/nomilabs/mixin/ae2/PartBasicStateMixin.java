package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;

import appeng.api.networking.events.MENetworkBootingStatusChange;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.me.GridAccessException;
import appeng.parts.AEBasePart;
import appeng.parts.PartBasicState;

/**
 * Fixes <a href="https://github.com/Nomi-CEu/Nomi-CEu/issues/1338">Nomi-CEu #1338</a> by updating client
 * flags immediately on event, instead of waiting for update, which may never occur.
 */
@Mixin(value = PartBasicState.class, remap = false)
public abstract class PartBasicStateMixin extends AEBasePart {

    /**
     * Mandatory Ignored Constructor
     */
    private PartBasicStateMixin(ItemStack is) {
        super(is);
    }

    @Shadow
    protected abstract void setClientFlags(int clientFlags);

    @Shadow
    public abstract int getClientFlags();

    @Shadow
    protected abstract int populateFlags(int cf);

    @Inject(method = "chanRender", at = @At("RETURN"))
    private void updateClientFlagsChan(MENetworkChannelsChanged c, CallbackInfo ci) {
        NomiLabs.LOGGER.debug("[PartBasicStateMixin] Updating Client Flags, from Channel Change");
        labs$updateClientFlags();
    }

    @Inject(method = "powerRender", at = @At("RETURN"))
    private void updateClientFlagsPower(MENetworkPowerStatusChange c, CallbackInfo ci) {
        NomiLabs.LOGGER.debug("[PartBasicStateMixin] Updating Client Flags, from Power Status Change");
        labs$updateClientFlags();
    }

    @Inject(method = "bootingRender", at = @At("RETURN"))
    private void updateClientFlagsBooting(MENetworkBootingStatusChange bs, CallbackInfo ci) {
        NomiLabs.LOGGER.debug("[PartBasicStateMixin] Updating Client Flags, from Booting Status Change");
        labs$updateClientFlags();
    }

    @Unique
    private void labs$updateClientFlags() {
        NomiLabs.LOGGER.debug("Client Flags Before: {}", getClientFlags());
        setClientFlags(0);

        try {
            if (getProxy().getEnergy().isNetworkPowered()) {
                setClientFlags(getClientFlags() | 1);
            }

            if (getProxy().getNode().meetsChannelRequirements()) {
                setClientFlags(getClientFlags() | 2);
            }

            setClientFlags(populateFlags(getClientFlags()));
            NomiLabs.LOGGER.debug("Client Flags After: {}", getClientFlags());
        } catch (GridAccessException ignored) {}
    }
}
