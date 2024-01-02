package com.nomiceu.nomilabs.mixin.solarflux;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import tk.zeitheron.solarflux.net.NetworkSF;

@SuppressWarnings("unused")
@Mixin(value = NetworkSF.class, remap = false)
public interface INetworkSF {


    @Accessor
    public static void setINSTANCE(NetworkSF instance) {}
}
