package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import appeng.client.me.ClientDCInternalInv;

@Mixin(value = ClientDCInternalInv.class, remap = false)
public interface ClientDCInternalInvAccessor {

    @Accessor("unlocalizedName")
    String labs$getUnlocalizedName();
}
