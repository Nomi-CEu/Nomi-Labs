package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.betterp2p.AccessibleInfoWrapper;
import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.InfoWrapper;
import com.projecturanus.betterp2p.network.data.P2PInfo;
import com.projecturanus.betterp2p.network.data.P2PLocation;

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

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void provideChannelInfo(P2PInfo info, CallbackInfo ci) {
        var channels = labs$getThis().getChannels();
        if (channels != null)
            // Index 0-3: Default Info, 4+, Bound/Unbound, Offline/Online
            labs$getThis().getHoverInfo().add(4, TextFormatting.LIGHT_PURPLE + channels);
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
