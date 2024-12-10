package com.nomiceu.nomilabs.integration.deepmobevolution;

import static mustapelto.deepmoblearning.DMLConstants.Gui.TrialKeystone.*;

import java.awt.*;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import mustapelto.deepmoblearning.client.gui.GuiMachine;
import mustapelto.deepmoblearning.client.gui.GuiTrialKeystone;

public class DMEJEIExclusion {

    public static class MachineGuiExclusion implements IAdvancedGuiHandler<GuiMachine> {

        @Override
        @NotNull
        public Class<GuiMachine> getGuiContainerClass() {
            return GuiMachine.class;
        }

        @Override
        public List<Rectangle> getGuiExtraAreas(@NotNull GuiMachine gui) {
            return ((JEIExcluded) gui).getGuiExclusionAreas();
        }
    }

    public static class TrialGuiExclusion implements IAdvancedGuiHandler<GuiTrialKeystone> {

        @Override
        @NotNull
        public Class<GuiTrialKeystone> getGuiContainerClass() {
            return GuiTrialKeystone.class;
        }

        @Override
        public List<Rectangle> getGuiExtraAreas(GuiTrialKeystone gui) {
            return ImmutableList.of(
                    new Rectangle(
                            gui.guiLeft + TRIAL_KEY_SLOT.LEFT,
                            gui.guiTop + TRIAL_KEY_SLOT.TOP,
                            TRIAL_KEY_SLOT.WIDTH,
                            TRIAL_KEY_SLOT.HEIGHT));
        }
    }
}
