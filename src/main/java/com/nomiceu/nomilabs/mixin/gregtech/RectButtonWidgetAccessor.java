package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import gregtech.api.gui.Widget;
import gregtech.api.terminal.gui.widgets.RectButtonWidget;

@Mixin(value = RectButtonWidget.class, remap = false)
public interface RectButtonWidgetAccessor {

    @Accessor("onPressed")
    BiConsumer<Widget.ClickData, Boolean> labs$getOnPressed();

    @Accessor("supplier")
    Supplier<Boolean> labs$getSupplier();
}
