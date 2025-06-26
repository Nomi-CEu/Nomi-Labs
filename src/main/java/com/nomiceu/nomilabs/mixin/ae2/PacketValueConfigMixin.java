package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;

import appeng.core.sync.AppEngPacket;
import appeng.core.sync.network.INetworkInfo;
import appeng.core.sync.packets.PacketValueConfig;

/**
 * Deserializes incl non consume for pattern terms.
 */
@Mixin(value = PacketValueConfig.class, remap = false)
public class PacketValueConfigMixin {

    @Shadow
    @Final
    private String Name;

    @Shadow
    @Final
    private String Value;

    @Inject(method = "serverPacketData", at = @At("HEAD"), cancellable = true)
    private void handleInclNonConsme(INetworkInfo manager, AppEngPacket packet, EntityPlayer player, CallbackInfo ci) {
        Container c = player.openContainer;
        if (Name.equals("Labs$NonConsume") && c instanceof InclNonConsumeSettable pattern) {
            pattern.labs$setInclNonConsume(Value.equals("1"));
            ci.cancel();
        }
    }
}
