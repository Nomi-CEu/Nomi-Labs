package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.config.LabsConfig;

import mezz.jei.gui.ingredients.GuiIngredient;

/**
 * Allows adding a newline before ore tooltip.
 */
@Mixin(value = GuiIngredient.class, remap = false)
public class GuiIngredientMixin<T> {

    @Inject(method = "drawTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void addNewLine(Minecraft minecraft, int xOffset, int yOffset, int mouseX, int mouseY, T value,
                            CallbackInfo ci, @Local List<String> tooltip) {
        if (LabsConfig.modIntegration.addJEIIngEmptyLine)
            tooltip.add("");
    }
}
