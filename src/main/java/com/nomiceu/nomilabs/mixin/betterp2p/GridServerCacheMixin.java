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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGridServerCache;
import com.projecturanus.betterp2p.network.data.GridServerCache;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.network.data.P2PLocationKt;
import com.projecturanus.betterp2p.util.p2p.TunnelInfo;

import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.helpers.IInterfaceHost;
import appeng.me.GridAccessException;
import appeng.me.cache.P2PCache;
import appeng.parts.p2p.PartP2PTunnel;
import appeng.util.Platform;
import kotlin.Pair;

/**
 * Implements the logic of the new modes and switching output/input.
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

    @Inject(method = "changeP2PType", at = @At("RETURN"), cancellable = true)
    private void restoreName(PartP2PTunnel<?> tunnel, TunnelInfo newType,
                             CallbackInfoReturnable<PartP2PTunnel<?>> cir) {
        var result = cir.getReturnValue();
        if (result == null) return;

        if (tunnel.hasCustomInventoryName()) result.setCustomName(tunnel.getCustomInventoryName());
        cir.setReturnValue(result);
    }

    @Inject(method = "linkP2P",
            at = @At(value = "INVOKE",
                     target = "Lappeng/me/cache/P2PCache;getInputs(SLjava/lang/Class;)Lappeng/me/cache/helpers/TunnelCollection;",
                     ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            require = 1,
            cancellable = true)
    private void properlyLinkP2p(P2PLocation inputIndex, P2PLocation outputIndex,
                                 CallbackInfoReturnable<Pair<PartP2PTunnel<?>, PartP2PTunnel<?>>> cir,
                                 @Local(ordinal = 0) short frequency) {
        var input = listP2P.get(inputIndex);
        var output = listP2P.get(outputIndex);

        P2PCache cache = null;
        try {
            cache = input.getProxy().getP2P();
        } catch (GridAccessException e) {
            throw new RuntimeException(e);
        }

        var inputs = cache.getInputs(frequency, input.getClass());
        if (!inputs.isEmpty()) {
            for (var origInput : inputs) {
                if (origInput != input) {
                    updateP2P(P2PLocationKt.toLoc(origInput), origInput, frequency, false,
                            origInput.hasCustomInventoryName() ? origInput.getCustomInventoryName() : "");
                }
            }
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
