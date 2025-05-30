package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.jei.mixinhelper.AccessibleListMultiMap;
import com.nomiceu.nomilabs.integration.jei.mixinhelper.AccessibleModRegistry;

import mezz.jei.collect.ListMultiMap;
import mezz.jei.startup.ModRegistry;

/**
 * Allows replacing recipe catalysts.
 */
@Mixin(value = ModRegistry.class, remap = false)
public class ModRegistryMixin implements AccessibleModRegistry {

    @Shadow
    @Final
    private ListMultiMap<String, Object> recipeCatalysts;

    @Override
    public void labs$replaceRecipeCatalyst(String category, List<Object> catalysts) {
        // noinspection unchecked
        ((AccessibleListMultiMap<String, Object>) recipeCatalysts).labs$replaceKey(category, catalysts);
    }
}
