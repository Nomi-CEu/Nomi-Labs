package com.nomiceu.nomilabs.mixin.deepmoblearning;

import java.awt.*;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.integration.deepmobevolution.AccessibleGuiMachine;
import com.nomiceu.nomilabs.integration.deepmobevolution.JEIExcluded;

import mustapelto.deepmoblearning.client.gui.GuiMachine;
import mustapelto.deepmoblearning.client.gui.buttons.ButtonRedstoneMode;

/**
 * Adds JEI Exclusion Areas to Gui Machine.
 */
@Mixin(value = GuiMachine.class, remap = false)
public class GuiMachineMixin implements JEIExcluded, AccessibleGuiMachine {

    @Shadow
    private ButtonRedstoneMode redstoneModeButton;

    @Override
    public List<Rectangle> getGuiExclusionAreas() {
        return ImmutableList.of(
                getRedstoneButtonRect());
    }

    @Override
    public Rectangle getRedstoneButtonRect() {
        return new Rectangle(
                redstoneModeButton.x,
                redstoneModeButton.y,
                redstoneModeButton.width,
                redstoneModeButton.height);
    }
}
