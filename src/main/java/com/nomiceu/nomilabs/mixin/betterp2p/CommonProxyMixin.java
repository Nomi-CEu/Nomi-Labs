package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.LabsValues;
import com.projecturanus.betterp2p.CommonProxy;

import appeng.api.definitions.IItemDefinition;
import appeng.parts.p2p.PartP2PTunnel;
import co.neeve.nae2.NAE2;
import co.neeve.nae2.common.parts.p2p.PartP2PInterface;

/**
 * Loads Interface P2P (Common Side)
 */
@Mixin(value = CommonProxy.class, remap = false)
public abstract class CommonProxyMixin {

    @Shadow
    protected abstract void registerTunnel(IItemDefinition def, int type, Class<? extends PartP2PTunnel<?>> classType);

    @Inject(method = "initTunnels", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void addInterface(CallbackInfo ci, @Local(ordinal = 0) int typeId) {
        if (!Loader.isModLoaded(LabsValues.NAE2_MODID)) return;
        labs$addNae2P2ps(typeId);
    }

    @Unique
    @Optional.Method(modid = LabsValues.NAE2_MODID)
    private void labs$addNae2P2ps(int typeId) {
        registerTunnel(NAE2.definitions().parts().p2pTunnelInterface(), typeId++, PartP2PInterface.class);
    }
}
