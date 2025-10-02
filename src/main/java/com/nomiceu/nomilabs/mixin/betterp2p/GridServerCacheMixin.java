package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGridServerCache;
import com.projecturanus.betterp2p.BetterP2P;
import com.projecturanus.betterp2p.network.data.GridServerCache;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.util.p2p.TunnelInfo;

import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.me.GridAccessException;
import appeng.me.cache.helpers.TunnelCollection;
import appeng.parts.p2p.PartP2PTunnel;
import kotlin.Pair;

/**
 * Implements the logic of the new modes and switching output/input.
 * Fixes changing type and binding with multiple inputs.
 */
@Mixin(value = GridServerCache.class, remap = false)
public abstract class GridServerCacheMixin implements AccessibleGridServerCache {

    @Shadow
    @Final
    private IGrid grid;

    @Shadow
    @Final
    private EntityPlayer player;

    @Shadow
    @Final
    private Map<P2PLocation, PartP2PTunnel<?>> listP2P;

    @Shadow
    protected abstract PartP2PTunnel<?> updateP2P(P2PLocation key, PartP2PTunnel<?> tunnel, short frequency,
                                                  boolean output, String name);

    @Shadow
    protected abstract PartP2PTunnel<?> changeP2PType(PartP2PTunnel<?> tunnel, TunnelInfo newType);

    @Inject(method = "changeP2PType", at = @At("RETURN"), cancellable = true)
    private void restoreName(PartP2PTunnel<?> tunnel, TunnelInfo newType,
                             CallbackInfoReturnable<PartP2PTunnel<?>> cir) {
        var result = cir.getReturnValue();
        if (result == null) return;

        if (tunnel.hasCustomInventoryName()) result.setCustomName(tunnel.getCustomInventoryName());
        cir.setReturnValue(result);
    }

    @Inject(method = "changeAllP2Ps", at = @At("HEAD"), cancellable = true)
    private void properlyChangeAllP2PTypes(P2PLocation p2p, TunnelInfo newType, CallbackInfoReturnable<Boolean> cir) {
        PartP2PTunnel<?> tunnel = listP2P.get(p2p);

        if (tunnel == null) {
            cir.setReturnValue(false);
            return;
        }

        if (tunnel.getFrequency() == 0) {
            // Unbound
            // Just change this P2P
            var result = changeP2PType(tunnel, newType);
            cir.setReturnValue(result != null);
            return;
        }

        try {
            TunnelCollection<?> inputs = tunnel.getProxy().getP2P().getInputs(tunnel.getFrequency(), tunnel.getClass());
            TunnelCollection<?> outputs = tunnel.getProxy().getP2P().getOutputs(tunnel.getFrequency(),
                    tunnel.getClass());

            // Add all parts to a new list, so we don't CME
            List<PartP2PTunnel<?>> toModify = new ArrayList<>();
            inputs.forEach(toModify::add);
            outputs.forEach(toModify::add);

            // Bound P2Ps, to a frequency with no other P2Ps
            if (toModify.isEmpty())
                toModify.add(tunnel);

            for (var modify : toModify) {
                var result = changeP2PType(modify, newType);
                if (result == null) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        } catch (GridAccessException ignored) {}
        cir.setReturnValue(true);
    }

    @Inject(method = "linkP2P",
            at = @At(value = "INVOKE",
                     target = "Lappeng/parts/p2p/PartP2PTunnel;getFrequency()S"),
            require = 1,
            cancellable = true)
    private void properlyLinkP2P(P2PLocation inputIndex, P2PLocation outputIndex,
                                 CallbackInfoReturnable<Pair<PartP2PTunnel<?>, PartP2PTunnel<?>>> cir) {
        var input = listP2P.get(inputIndex);
        var output = listP2P.get(outputIndex);

        short frequency;
        try {
            // Generate new frequency, always
            // This makes 'bind as' modes create a separate network, making them still useful with 'add as' modes
            // Use AE2's way instead of Better P2P's way
            frequency = input.getProxy().getP2P().newFrequency();
        } catch (GridAccessException e) {
            cir.setReturnValue(null);
            return;
        }

        // Perform the link
        var inputResult = updateP2P(inputIndex, input, frequency, false,
                input.hasCustomInventoryName() ? input.getCustomInventoryName() : "");
        var outputResult = updateP2P(outputIndex, output, frequency, true,
                output.hasCustomInventoryName() ? output.getCustomInventoryName() : "");

        // Original code here handles P2Ps that extend IInterfaceHost; these seem not to exist
        // Perhaps a side effect of porting code from GTNH?
        // Skip

        cir.setReturnValue(new Pair<>(inputResult, outputResult));
    }

    @Unique
    @Override
    public PartP2PTunnel<?> labs$changeIsInput(P2PLocation key, boolean isInput) {
        if (grid instanceof ISecurityGrid securityGrid &&
                !securityGrid.hasPermission(player, SecurityPermissions.BUILD)) {
            return null;
        }
        var p2p = listP2P.get(key);
        if (p2p == null) return null;

        if (p2p.isOutput() == !isInput) {
            player.sendMessage(new TextComponentTranslation("nomilabs.gui.better_p2p.error.same_output",
                    isInput ? "Input" : "Output"));
            return null;
        }

        return updateP2P(key, p2p, p2p.getFrequency(), !isInput,
                p2p.hasCustomInventoryName() ? p2p.getCustomInventoryName() : "");
    }

    @Unique
    @Override
    public boolean labs$addInput(P2PLocation toAdd, P2PLocation toBind) {
        return labs$addAs(toAdd, toBind, true);
    }

    @Unique
    @Override
    public boolean labs$addOutput(P2PLocation toAdd, P2PLocation toBind) {
        return labs$addAs(toAdd, toBind, false);
    }

    /**
     * Shared Logic
     */
    @Unique
    private boolean labs$addAs(P2PLocation toAdd, P2PLocation toBind, boolean isInput) {
        if (grid instanceof ISecurityGrid securityGrid &&
                !securityGrid.hasPermission(player, SecurityPermissions.BUILD)) {
            return false;
        }
        var toAddP2P = listP2P.get(toAdd);
        var toBindP2P = listP2P.get(toBind);
        if (toAddP2P == null || toBindP2P == null) return false;

        // Update Type
        if (toAddP2P.getClass() != toBindP2P.getClass()) {
            toAddP2P = changeP2PType(toAddP2P, BetterP2P.proxy.getP2PFromClass(toBindP2P.getClass()));

            if (toAddP2P == null) return false;
        }

        // Handle Unbound To Bind P2Ps
        if (toBindP2P.getFrequency() == 0) {
            short frequency;
            try {
                frequency = toBindP2P.getProxy().getP2P().newFrequency();
            } catch (GridAccessException e) {
                return false;
            }

            toBindP2P = updateP2P(toBind, toBindP2P, frequency, toBindP2P.isOutput(),
                    toBindP2P.hasCustomInventoryName() ? toBindP2P.getCustomInventoryName() : "");

            if (toBindP2P == null) return false;
        }

        return updateP2P(toAdd, toAddP2P, toBindP2P.getFrequency(), !isInput,
                toAddP2P.hasCustomInventoryName() ? toAddP2P.getCustomInventoryName() : "") != null;
    }
}
