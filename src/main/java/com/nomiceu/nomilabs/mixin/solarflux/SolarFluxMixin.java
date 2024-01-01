package com.nomiceu.nomilabs.mixin.solarflux;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tk.zeitheron.solarflux.SolarFlux;
import tk.zeitheron.solarflux.net.NetworkSF;

import java.io.File;

@Mixin(value = SolarFlux.class, remap = false)
public class SolarFluxMixin {
    @Shadow
    @Mutable
    @Final
    public static File CONFIG_DIR;
    @Redirect(method = "preInit", at = @At(value = "INVOKE", target = "Ltk/zeitheron/solarflux/SolarFlux$FinalFieldHelper;setStaticFinalField(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Z"))
    static boolean setStaticFinalField1(Class<?> cls, String var, Object val) {
        CONFIG_DIR = (File) val;
        return true;
    }
    
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ltk/zeitheron/solarflux/SolarFlux$FinalFieldHelper;setStaticFinalField(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Z"))
    static boolean setStaticFinalField2(Class<?> cls, String var, Object val) {
        INetworkSF.setINSTANCE((NetworkSF) val);
        return true;
    }
}
