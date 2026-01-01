package com.nomiceu.nomilabs.mixin.gregtech;

import static mezz.jei.api.gui.IGhostIngredientHandler.Target;

import java.awt.*;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.api.gui.impl.ModularUIGui;
import gregtech.integration.jei.utils.ModularUIGuiHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
                                                                Operation<List<Target<Object>>> original) {
        if (!(ingredient instanceof BookmarkItem<?>bookmarkItem)) {
            // noinspection unchecked
            return (List<Target<I>>) (Object) original.call(gui, ingredient, doStart);
        }

        List<Target<Object>> targets = original.call(gui, bookmarkItem.ingredient, doStart);
        List<Target<I>> result = new ObjectArrayList<>();

        // Map targets so that we give them the actual ingredient on accept
        for (Target<Object> target : targets) {
            result.add(new Target<>() {

                @Override
                @NotNull
                public Rectangle getArea() {
                    return target.getArea();
                }

                @Override
                public void accept(@NotNull I ingredient) {
                    // noinspection unchecked
                    target.accept(((BookmarkItem<Object>) ingredient).ingredient);
                }
            });
        }

        return result;
    }
}
