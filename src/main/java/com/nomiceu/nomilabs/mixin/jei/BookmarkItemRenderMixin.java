package com.nomiceu.nomilabs.mixin.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mezz.jei.bookmarks.BookmarkItem;
import mezz.jei.bookmarks.BookmarkItemRender;

/**
 * Properly resets gl state.
 * Part of the impl of <a href="https://github.com/CleanroomMC/HadEnoughItems/pull/161/">HEI #161</a>.
 */
@Mixin(value = BookmarkItemRender.class, remap = false)
public class BookmarkItemRenderMixin {

    @Inject(method = "render(Lnet/minecraft/client/Minecraft;IILmezz/jei/bookmarks/BookmarkItem;)V", at = @At("RETURN"))
    private void properlyResetGlState(Minecraft minecraft, int xPosition, int yPosition, BookmarkItem<?> ingredient,
                                      CallbackInfo ci) {
        // We need no lighting and plain color for continued ingredient rendering
        // Lighting and color changed by text rendering
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
    }
}
