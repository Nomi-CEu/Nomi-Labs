package com.nomiceu.nomilabs.mixinhelper;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.NomiLabs;

@SideOnly(Side.CLIENT)
public class GuiCustomConfirmOpenLink extends GuiScreen {

    private final GuiScreen parent;

    private final String titleText;

    private final String confirmButtonText;
    private final String cancelButtonText;
    private final String copyLinkButtonText;

    private final String linkText;

    private final boolean hasNoteText;
    private final String noteText;

    private final List<String> listLines;

    private static final int CONFIRM_ID = 0;
    private static final int COPY_ID = 2;
    private static final int CANCEL_ID = 1;

    public GuiCustomConfirmOpenLink(GuiScreen parent, String linkText, @Nullable String noteText) {
        this.parent = parent;
        this.titleText = TextFormatting.BOLD + I18n.format("chat.link.confirmTrusted");

        this.confirmButtonText = I18n.format("chat.link.open");
        this.cancelButtonText = I18n.format("gui.cancel");
        this.copyLinkButtonText = I18n.format("chat.copy");

        this.noteText = noteText;
        this.hasNoteText = this.noteText != null;

        this.linkText = linkText;
        this.listLines = new ArrayList<>();
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        makeButton(CONFIRM_ID, -105, confirmButtonText);
        makeButton(COPY_ID, 0, copyLinkButtonText);
        makeButton(CANCEL_ID, 105, cancelButtonText);
        listLines.clear();
        listLines.addAll(fontRenderer.listFormattedStringToWidth(TextFormatting.AQUA + linkText + TextFormatting.RESET,
                this.width - 50));
    }

    private void makeButton(int id, int xOffset, String displayText) {
        buttonList.add(new GuiButton(id, width / 2 - 50 + xOffset, height / 6 + 96, 100, 20, displayText));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == COPY_ID)
            setClipboardString(linkText);

        if (button.id == CONFIRM_ID) {
            openLink(linkText);
        }

        mc.displayGuiScreen(parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawCenteredString(fontRenderer, titleText, width / 2, 70 - (hasNoteText ? 20 : 0), 0xffffff);
        if (hasNoteText)
            drawCenteredString(fontRenderer, noteText, width / 2, 70, 0xffffff);

        int i = 90;
        for (String s : listLines) {
            drawCenteredString(fontRenderer, s, width / 2, i, 0xffffff);
            i += fontRenderer.FONT_HEIGHT;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void openLink(String url) {
        try {
            Class<?> desktopClass = Class.forName("java.awt.Desktop");
            Object desktopObj = desktopClass.getMethod("getDesktop").invoke(null);
            desktopClass.getMethod("browse", URI.class).invoke(desktopObj, new URI(url));
        } catch (URISyntaxException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException e) {
            NomiLabs.LOGGER.error("Failed to Open Link!");
            NomiLabs.LOGGER.throwing(e);
        }
    }
}
