package com.nomiceu.nomilabs.mixin.betterquesting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.integration.betterquesting.DescriptionPart;
import com.nomiceu.nomilabs.integration.betterquesting.PanelDescription;

import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.client.gui2.GuiQuest;

/**
 * Allows usage of our custom PanelDescription to handle link and copy embeds.
 * <br>
 * Part of the implementation for custom Description Embeds in BQu.
 * <br>
 * See the <a href="https://github.com/Nomi-CEu/Nomi-Labs/wiki/Custom-Better-Questing-Unofficial-Embeds">Nomi Labs
 * Wiki</a> for more information.
 */
@Mixin(value = GuiQuest.class, remap = false)
public class GuiQuestMixin {

    @WrapOperation(method = "refreshDescPanel",
                   at = @At(value = "NEW",
                            target = "(Lbetterquesting/api2/client/gui/misc/IGuiRect;Ljava/lang/String;Z)Lbetterquesting/api2/client/gui/panels/content/PanelTextBox;"),
                   require = 1)
    private PanelTextBox useCustomDescPanel(IGuiRect rect, String text, boolean autoFit,
                                            Operation<PanelTextBox> original) {
        if (DescriptionPart.textContainsCustom(text)) {
            return new PanelDescription(rect, text);
        }
        return original.call(rect, text, autoFit);
    }
}
