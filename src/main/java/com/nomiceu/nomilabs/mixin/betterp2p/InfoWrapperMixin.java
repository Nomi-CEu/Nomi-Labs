package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleInfoWrapper;
import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.BetterP2P;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.network.data.P2PInfo;
import com.projecturanus.betterp2p.network.data.P2PLocation;
import com.projecturanus.betterp2p.util.p2p.TunnelInfo;

/**
 * Allows saving of each P2P's distance to the player, and improves display of frequencies.
 */
@Mixin(value = InfoWrapper.class, remap = false)
public class InfoWrapperMixin implements AccessibleInfoWrapper {

    @Shadow
    @Final
    private P2PLocation loc;

    @Shadow
    private short frequency;

    @Shadow
    @Final
    @Mutable
    private String description;

    @Unique
    private int labs$connectionInfoIndex = 0;

    @Unique
    private double labs$distanceToPlayer = 0.0;

    @Unique
    @Override
    public double labs$getDistance() {
        return labs$distanceToPlayer;
    }

    @Unique
    @Override
    public void labs$calculateDistance(Vec3d playerPos) {
        // Change X, Y and Z Positions Based on Facing

        // Add 0.5 (middle of block)
        double x = loc.getPos().getX() + 0.5;
        double y = loc.getPos().getY() + 0.5;
        double z = loc.getPos().getZ() + 0.5;

        // Amt to add/subtract base on facing. Since P2P is 2 pixels wide (1/8 of block),
        // this leaves 1/16 from the half (middle of P2P)
        double mod = 0.4375;

        switch (loc.getFacing()) {
            case NORTH -> z -= mod;
            case SOUTH -> z += mod;
            case WEST -> x -= mod;
            case EAST -> x += mod;
            case UP -> y += mod;
            case DOWN -> y -= mod;
        }

        double distance = Math.sqrt(playerPos.squareDistanceTo(x, y, z));

        // Round to 1dp
        labs$distanceToPlayer = Math.round(distance * 10) / 10.0;
    }

    @Unique
    @Override
    public void labs$setConnectionAmt(int amt) {
        var hover = labs$getThis().getHoverInfo();
        if (hover.size() > labs$connectionInfoIndex)
            hover.remove(labs$connectionInfoIndex);

        StringBuilder builder = new StringBuilder("nomilabs.gui.advanced_memory_card.hover_info.connections.");
        // Opposite type
        if (labs$getThis().getOutput())
            builder.append("input.");
        else
            builder.append("output.");

        switch (amt) {
            case 0 -> builder.append("none");
            case 1 -> builder.append("one");
            default -> builder.append("multi");
        }

        hover.add(LabsTranslate.translate(builder.toString(), amt));
    }

    @Inject(method = "setFrequency", at = @At("HEAD"), cancellable = true)
    private void cancelChangeTooltip(short value, CallbackInfo ci) {
        // In the actual method, hoverInfo's bound/unbound state is updated
        // But this shouldn't happen in the first place
        frequency = value;
        ci.cancel();
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void provideChannelInfo(P2PInfo info, CallbackInfo ci) {
        // Change existing description (allow localization)
        TunnelInfo p2pType = BetterP2P.proxy.getP2PFromIndex(info.getType());
        if (p2pType == null) return; // This should never happen

        description = LabsTranslate.translate("nomilabs.gui.advanced_memory_card.info.type", p2pType.getDispName(),
                LabsTranslate.translate(info.getOutput() ? "gui.advanced_memory_card.p2p_status.output" :
                        "gui.advanced_memory_card.p2p_status.input"));

        // Custom Hover Info
        var hover = labs$getThis().getHoverInfo();
        hover.clear();

        // Title
        hover.add(LabsTranslate.translate("nomilabs.gui.advanced_memory_card.hover_info.title",
                p2pType.getDispName()));

        // General Information
        hover.add(TextFormatting.YELLOW + LabsTranslate.translate("gui.advanced_memory_card.pos",
                info.getPos().getX(), info.getPos().getY(), info.getPos().getZ()));
        hover.add(TextFormatting.YELLOW + LabsTranslate.translate("gui.advanced_memory_card.side",
                StringUtils.capitalize(info.getFacing().getName2())));
        hover.add(TextFormatting.YELLOW + LabsTranslate.translate("gui.advanced_memory_card.dim", info.getDim()));

        // ME Tunnel Specific
        var channels = labs$getThis().getChannels();
        if (channels != null)
            labs$getThis().getHoverInfo().add(TextFormatting.LIGHT_PURPLE + channels);

        // State
        if (frequency == 0) {
            hover.add(TextFormatting.RED + LabsTranslate.translate("gui.advanced_memory_card.p2p_status.unbound"));
        } else {
            hover.add(TextFormatting.GREEN + LabsTranslate.translate("gui.advanced_memory_card.p2p_status.bound"));
        }

        if (!info.getHasChannel()) {
            hover.add("§c" + LabsTranslate.translate("gui.advanced_memory_card.p2p_status.offline"));
        }

        // Init connection index, so we can update it when check applied
        labs$connectionInfoIndex = hover.size();
    }

    @Inject(method = "getFreqDisplay", at = @At("HEAD"), cancellable = true)
    private void getLabsFreqDisplay(CallbackInfoReturnable<String> cir) {
        // Use AE2's Method
        StringBuilder builder = new StringBuilder();
        builder.append(LabsTranslate.translate("item.advanced_memory_card.selected"))
                .append(" ");

        if (frequency == 0)
            builder.append(LabsTranslate.translate("gui.advanced_memory_card.desc.not_set"));
        else
            builder.append(String.format("%04X", frequency));

        cir.setReturnValue(builder.toString());
    }

    @Unique
    private InfoWrapper labs$getThis() {
        return ((InfoWrapper) (Object) this);
    }
}
