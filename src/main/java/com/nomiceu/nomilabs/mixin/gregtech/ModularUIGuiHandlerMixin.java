package com.nomiceu.nomilabs.mixin.gregtech;

import static mezz.jei.api.gui.IGhostIngredientHandler.Target;

import java.awt.*;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.integration.jei.BookmarkItemTargetWrapper;

import gregtech.api.gui.impl.ModularUIGui;
import gregtech.integration.jei.utils.ModularUIGuiHandler;
import mezz.jei.bookmarks.BookmarkItem;

/**
 * Fixes ghost ingredient drag from bookmarks.
 */
@Mixin(value = ModularUIGuiHandler.class, remap = false)
public class ModularUIGuiHandlerMixin {

    @WrapMethod(method = "getTargets(Lgregtech/api/gui/impl/ModularUIGui;Ljava/lang/Object;Z)Ljava/util/List;")
    private <I> @NotNull List<Target<I>> normalizeBookmarkItems(ModularUIGui gui,
                                                                @NotNull I ingredient,
                                                                boolean doStart,
                                                                Operation<List<Target<?>>> original) {
        if (!(ingredient instanceof BookmarkItem<?>item))
            // noinspection unchecked
            return (List<Target<I>>) (Object) original.call(gui, ingredient, doStart);

        return BookmarkItemTargetWrapper.wrapOriginalTargets(
                original.call(gui, BookmarkItemTargetWrapper.getIngredientWithCount(item), doStart));
    }
}
