package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.mixinhelper.RemappableForgeRegistry;
import com.nomiceu.nomilabs.mixinhelper.RemappableSnapshot;

/**
 * This Mixin allows for the remapped and block lists to be loaded from the in-world-save.
 */
@Mixin(value = GameData.class, remap = false)
public class GameDataMixin {

    @Inject(method = "loadPersistentDataToStagingRegistry",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraftforge/registries/ForgeRegistry;loadIds(Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Lnet/minecraftforge/registries/ForgeRegistry;Lnet/minecraft/util/ResourceLocation;)V"),
            require = 1,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void loadRemappedToRegistry(RegistryManager pool, RegistryManager to,
                                               Map<ResourceLocation, Integer[]> remaps,
                                               Map<ResourceLocation, Integer> missing, ResourceLocation name,
                                               ForgeRegistry.Snapshot snap, Class<?> regType, CallbackInfo ci,
                                               @Local(ordinal = 1) ForgeRegistry<?> newRegistry) {
        ((RemappableSnapshot) snap).labs$loadToRegistry((RemappableForgeRegistry) newRegistry);
    }
}
