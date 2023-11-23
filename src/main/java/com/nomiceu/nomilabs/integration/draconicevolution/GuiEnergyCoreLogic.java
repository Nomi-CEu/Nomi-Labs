package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.brandonscore.client.gui.GuiButtonAHeight;
import com.brandon3055.brandonscore.client.utils.GuiHelper;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.draconicevolution.client.gui.GuiEnergyCore;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEnergyCoreLogic {
    public static GuiButton addDestructButtonToList(GuiContainer gui, List<GuiButton> buttonList) {
        GuiButton destructCore = new GuiButtonAHeight(7, gui.guiLeft + 9, gui.guiTop + 73 + 10, 162, 12,
                I18n.format("button.de.destructCore.txt"));
        buttonList.add(destructCore);
        return destructCore;
    }

    /**
     * Changes the wrap text parameters of the invalid message.
     */
    public static void drawBackground(GuiEnergyCore gui, FontRenderer fontRenderer) {
        GuiHelper.drawGuiBaseBackground(gui, gui.guiLeft, gui.guiTop, gui.xSize, gui.ySize);
        GuiHelper.drawPlayerSlots(gui, gui.guiLeft + (gui.xSize / 2), gui.guiTop + 115 + 10, true);
        gui.drawCenteredString(fontRenderer, I18n.format("gui.de.energyStorageCore.name", gui.tile.tier.toString()),
                gui.guiLeft + (gui.xSize / 2), gui.guiTop + 5, InfoHelper.GUI_TITLE);
        int stabColour = gui.tile.stabilizersOK.value ? 0x00FF00 : 0xFF0000;
        String stabText = I18n.format("gui.de.stabilizers.txt") + ": " + (gui.tile.stabilizersOK.value ? I18n.format("gui.de.valid.txt") : I18n.format("gui.de.invalid.txt"));
        GuiHelper.drawCenteredString(fontRenderer, stabText, gui.guiLeft + gui.xSize / 2, gui.guiTop + 18, stabColour, gui.tile.stabilizersOK.value);
        if (gui.tile.tier.value >= 5) {
            GuiHelper.drawCenteredString(fontRenderer, I18n.format("gui.de.advancedStabilizersRequired.txt"), gui.guiLeft + gui.xSize / 2, gui.guiTop + 28, 0x777777, false);
        }

        int coreColour = gui.tile.coreValid.value ? 0x00FF00 : 0xFF0000;
        String coreText = I18n.format("gui.de.core.txt") + ": " + (gui.tile.coreValid.value ? I18n.format("gui.de.valid.txt") : I18n.format("gui.de.invalid.txt"));
        GuiHelper.drawCenteredString(fontRenderer, coreText, gui.guiLeft + gui.xSize / 2, gui.guiTop + 36, coreColour, gui.tile.coreValid.value);
        if (!gui.tile.coreValid.value) {
            GuiHelper.drawCenteredSplitString(fontRenderer, gui.tile.invalidMessage.value, gui.guiLeft + gui.xSize / 2, gui.guiTop + 46, 150, coreColour, false);
        }
    }
}
