package com.nomiceu.nomilabs.mixin.betterquesting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.integration.betterquesting.AccessibleGuiRectText;

/**
 * Allows setting of the height in the main gui text height, so scrolling is to the correct height.
 * <br>
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
@Mixin(targets = "betterquesting.api2.client.gui.panels.content.PanelTextBox$GuiRectText", remap = false)
public class GuiRectTextMixin implements AccessibleGuiRectText {

    @Shadow
    private int h;

    @Unique
    @Override
    public void labs$setHeight(int h) {
        this.h = h;
    }
}
