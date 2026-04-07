package com.nomiceu.nomilabs.integration.jei;

import static mezz.jei.api.gui.IGhostIngredientHandler.Target;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import mezz.jei.bookmarks.BookmarkItem;

public class BookmarkItemTargetWrapper<I> implements Target<I> {

    private final Target<Object> parent;

    public BookmarkItemTargetWrapper(Target<?> parent) {
        // noinspection unchecked
        this.parent = (Target<Object>) parent;
    }

    public Target<?> getParent() {
        return parent;
    }

    @Override
    @NotNull
    public Rectangle getArea() {
        return parent.getArea();
    }

    @Override
    public void accept(@NotNull I ingredient) {
        if (!(ingredient instanceof BookmarkItem<?>item)) {
            parent.accept(ingredient);
            return;
        }

        parent.accept(BookmarkItemTargetWrapper.getIngredientWithCount(item));
    }

    public static Object getIngredientWithCount(BookmarkItem<?> item) {
        if (item.ingredient instanceof ItemStack stack) {
            stack = stack.copy();
            stack.setCount((int) item.getDisplayAmount());
            return stack;
        }

        if (item.ingredient instanceof FluidStack fluid) {
            fluid = fluid.copy();
            fluid.amount = ((int) item.getDisplayAmount());
            return fluid;
        }

        return item.ingredient;
    }

    public static <I> List<Target<I>> wrapOriginalTargets(List<Target<?>> targets) {
        List<Target<I>> result = new ArrayList<>();
        for (var target : targets) {
            result.add(new BookmarkItemTargetWrapper<>(target));
        }

        return result;
    }
}
