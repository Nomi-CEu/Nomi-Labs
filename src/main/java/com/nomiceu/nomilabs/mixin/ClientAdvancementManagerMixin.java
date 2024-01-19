package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientAdvancementManager.class)
public class ClientAdvancementManagerMixin {
    @Shadow
    @Final
    private AdvancementList advancementList;

    @Inject(method = "read(Lnet/minecraft/network/play/server/SPacketAdvancementInfo;)V", at = @At("HEAD"), cancellable = true)
    public void reloadWithoutAdvancements(CallbackInfo ci) {
        if (!LabsConfig.advanced.disableAdvancements) return;
        advancementList.clear();
        ci.cancel();
    }
}
