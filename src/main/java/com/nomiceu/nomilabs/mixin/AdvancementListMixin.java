package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = AdvancementList.class)
public class AdvancementListMixin {
    @Inject(method = "loadAdvancements(Ljava/util/Map;)V", at = @At("HEAD"), cancellable = true)
    public void cancelAdvancements(Map<ResourceLocation, Advancement.Builder> advancementsIn, CallbackInfo ci) {
        if (LabsConfig.advanced.disableAdvancements) ci.cancel();
    }
}
