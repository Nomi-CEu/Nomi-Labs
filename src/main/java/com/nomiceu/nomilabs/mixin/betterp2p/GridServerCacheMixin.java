package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGridServerCache;
import com.projecturanus.betterp2p.network.data.GridServerCache;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.util.p2p.TunnelInfo;

import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.helpers.IInterfaceHost;
import appeng.me.GridAccessException;
import appeng.me.cache.helpers.TunnelCollection;
import appeng.parts.p2p.PartP2PTunnel;
import appeng.util.Platform;
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
    protected abstract void handleInterface(IInterfaceHost oldIn, IInterfaceHost oldOut, IInterfaceHost newIn,
                                            IInterfaceHost newOut, List<ItemStack> drops);

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

        changeP2PType(tunnel, newType);

        if (tunnel.getFrequency() == 0 || !tunnel.isActive()) {
            // Unbound or Inactive
            // Can't rebind same frequency if inactive, as not registered in ae2 handling
            cir.setReturnValue(true);
            return;
        }

        try {
            TunnelCollection<?> inputs = tunnel.getProxy().getP2P().getInputs(tunnel.getFrequency(), tunnel.getClass());
            TunnelCollection<?> outputs = tunnel.getProxy().getP2P().getOutputs(tunnel.getFrequency(),
                    tunnel.getClass());

            for (var input : inputs) {
                changeP2PType(input, newType);
            }

            for (var output : outputs) {
                changeP2PType(output, newType);
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

        var frequency = input.getFrequency();
        try {
            if (frequency == 0 || input.isOutput()) {
                // Generate new frequency
                // Use AE2's way instead of Better P2P's way
                frequency = input.getProxy().getP2P().newFrequency();
            }
        } catch (GridAccessException e) {
            cir.setReturnValue(null);
            return;
        }

        // Perform the link
        var inputResult = updateP2P(inputIndex, input, frequency, false,
                input.hasCustomInventoryName() ? input.getCustomInventoryName() : "");
        var outputResult = updateP2P(outputIndex, output, frequency, true,
                output.hasCustomInventoryName() ? output.getCustomInventoryName() : "");

        // Special case for interfaces
        if (input instanceof IInterfaceHost inputHost && output instanceof IInterfaceHost outputHost) {
            List<ItemStack> drops = new ArrayList<>();
            handleInterface(inputHost, outputHost, (IInterfaceHost) inputResult, (IInterfaceHost) outputResult, drops);
            Platform.spawnDrops(player.world, output.getLocation().getPos(), drops);
        }

        cir.setReturnValue(new Pair<>(inputResult, outputResult));
    }

    @Override
    @Unique
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

    @Override
    @Unique
    public boolean labs$addInput(P2PLocation key, short sourceFrequency) {
        return labs$addAs(key, sourceFrequency, true);
    }

    @Override
    @Unique
    public boolean labs$addOutput(P2PLocation key, short sourceFrequency) {
        return labs$addAs(key, sourceFrequency, false);
    }

    /**
     * Shared Logic
     */
    @Unique
    private boolean labs$addAs(P2PLocation key, short sourceFrequency, boolean isInput) {
        if (grid instanceof ISecurityGrid securityGrid &&
                !securityGrid.hasPermission(player, SecurityPermissions.BUILD)) {
            return false;
        }
        var p2p = listP2P.get(key);
        if (p2p == null) return false;

        return updateP2P(key, p2p, sourceFrequency, !isInput,
                p2p.hasCustomInventoryName() ? p2p.getCustomInventoryName() : "") != null;
    }
}
