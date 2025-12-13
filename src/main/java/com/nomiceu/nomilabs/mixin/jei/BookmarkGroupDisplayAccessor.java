package com.nomiceu.nomilabs.mixin.jei;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import mezz.jei.bookmarks.BookmarkGroup;
import mezz.jei.gui.overlay.bookmarks.group.BookmarkGroupDisplay;

/**
 * Part of the impl for <a href="https://github.com/CleanroomMC/HadEnoughItems/pull/158">HEI #158</a>.
 */
@Mixin(value = BookmarkGroupDisplay.class, remap = false)
public interface BookmarkGroupDisplayAccessor {

    @Accessor("group")
    BookmarkGroup labs$getGroup();
}
