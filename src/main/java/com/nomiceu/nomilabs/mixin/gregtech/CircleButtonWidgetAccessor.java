package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.terminal.gui.widgets.CircleButtonWidget;

/**
 * Part of the implementation of fixing the rendering of toggle auto-output buttons in the GT Console App.
 */
@Mixin(value = CircleButtonWidget.class, remap = false)
public interface CircleButtonWidgetAccessor {

    @Accessor("icon")
    IGuiTexture labs$getIcon();

    @Accessor("hoverText")
    String[] labs$getHoverText();
}
