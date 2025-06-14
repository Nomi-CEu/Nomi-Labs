package com.nomiceu.nomilabs.mixin.jei;

import net.minecraft.client.gui.FontRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;

/**
 * Fixes JEI's custom ItemStackRenderer not aligning with vanilla's.
 */
@Mixin(value = ItemStackRenderer.class, remap = false)
public class ItemStackRendererMixin {

    @Redirect(method = "renderCustomStackSize",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I",
                       remap = true),
              require = 1)
    private int alignText(FontRenderer instance, String text, float x, float y, int color, @Local boolean shouldScale) {
        // Don't change it if we are scaling; scaled text off the slot looks weird
        if (!shouldScale) {
            x += 1;
            y += 1;
        }
        return instance.drawStringWithShadow(text, x, y, color);
    }
}
