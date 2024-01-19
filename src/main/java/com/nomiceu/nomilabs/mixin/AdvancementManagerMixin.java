package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AdvancementManager.class)
public class AdvancementManagerMixin {
    @Shadow
    @Final
    private static AdvancementList ADVANCEMENT_LIST;

    @Inject(method = "reload()V", at = @At("HEAD"), cancellable = true)
    public void reloadWithoutAdvancements(CallbackInfo ci) {
        if (!LabsConfig.advanced.disableAdvancements) return;
        ADVANCEMENT_LIST.clear();
        ci.cancel();
    }
}
