package com.nomiceu.nomilabs.integration.ae2;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.util.LabsTranslate;

import appeng.client.gui.widgets.ITooltip;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;

public class LabsInclNonConsumableButton extends GuiButton implements ITooltip {

    private static final ResourceLocation AE2_ICONS = new ResourceLocation(LabsValues.AE2_MODID,
            "textures/guis/states.png");
    private static final ResourceLocation LABS_ICONS = new ResourceLocation(LabsValues.AE2_MODID,
            "textures/gui/labs_extra_icons.png");

    private static final int IDX_ENABLE = 0;
    private static final int IDX_DISABLE = 1;

    private boolean inclNonConsume;

    private final String title;
    private final String descEnable;
    private final String descDisable;

    public LabsInclNonConsumableButton(int x, int y) {
        super(0, x, y, 8, 8, "");
        inclNonConsume = true;

        title = LabsTranslate.translate("gui.appliedenergistics2.non_consume");
        descEnable = LabsTranslate.translate("gui.appliedenergistics2.non_consume.enable.1") + "\n" +
                LabsTranslate.translate("gui.appliedenergistics2.non_consume.enable.2");
        descDisable = LabsTranslate.translate("gui.appliedenergistics2.non_consume.disable.1") + "\n" +
                LabsTranslate.translate("gui.appliedenergistics2.non_consume.disable.2");
    }

    public static void handleButtonPress(GuiButton btn, LabsInclNonConsumableButton labs$inclNonConsume,
                                         InclNonConsumeSettable inventorySlots) {
        if (labs$inclNonConsume == btn) {
            labs$inclNonConsume.toggle();

            // Client Sync
            inventorySlots
                    .labs$setInclNonConsume(labs$inclNonConsume.isInclNonConsume());

            // Server Sync
            try {
                NetworkHandler.instance().sendToServer(
                        new PacketValueConfig("Labs$NonConsume", labs$inclNonConsume.isInclNonConsume() ? "1" : "0"));
            } catch (IOException e) {
                AELog.error(e);
            }
        }
    }

    public void setInclNonConsume(boolean inclNonConsume) {
        this.inclNonConsume = inclNonConsume;
    }

    public void toggle() {
        inclNonConsume = !inclNonConsume;
    }

    public boolean isInclNonConsume() {
        return inclNonConsume;
    }

    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!visible) return;

        hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, 0.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw BG
        mc.renderEngine.bindTexture(AE2_ICONS);
        drawTexturedModalRect(0, 0, 240, 240, 16, 16);

        // Draw FG
        mc.renderEngine.bindTexture(LABS_ICONS);
        drawModalRectWithCustomSizedTexture(0, 0, 0, (inclNonConsume ? IDX_ENABLE : IDX_DISABLE) * 16, 16, 16, 16, 32);

        mouseDragged(mc, mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    @Override
    public String getMessage() {
        return title + "\n" + (inclNonConsume ? descEnable : descDisable);
    }

    @Override
    public int xPos() {
        return x;
    }

    @Override
    public int yPos() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
