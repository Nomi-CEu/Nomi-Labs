package com.nomiceu.nomilabs.mixin.solarflux;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import tk.zeitheron.solarflux.net.NetworkSF;

@Mixin(value = NetworkSF.class, remap = false)
public class INetworkSF {
    @Shadow
    @Mutable
    @Final
    public static NetworkSF INSTANCE;
    
    @Accessor
    public static void setINSTANCE(NetworkSF instance) {}
}
