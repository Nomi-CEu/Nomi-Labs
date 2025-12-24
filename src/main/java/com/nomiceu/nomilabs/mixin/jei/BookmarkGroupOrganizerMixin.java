package com.nomiceu.nomilabs.mixin.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mezz.jei.gui.overlay.bookmarks.group.BookmarkGroupDisplay;
import mezz.jei.gui.overlay.bookmarks.group.BookmarkGroupOrganizer;

/**
 * Properly resets gl state.
 * Part of the impl of <a href="https://github.com/CleanroomMC/HadEnoughItems/pull/161/">HEI #161</a>.
 */
@Mixin(value = BookmarkGroupOrganizer.class, remap = false)
public class BookmarkGroupOrganizerMixin {

    @Inject(method = "drawGroup", at = @At("RETURN"))
    private void properlyResetGlState(Minecraft minecraft, int mouseX, int mouseY, BookmarkGroupDisplay display,
                                      CallbackInfo ci) {
        GlStateManager.color(1, 1, 1, 1);
    }
}
