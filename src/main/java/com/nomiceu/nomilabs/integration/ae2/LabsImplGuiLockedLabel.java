package com.nomiceu.nomilabs.integration.ae2;

import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;
import static com.nomiceu.nomilabs.util.LabsTranslate.translatable;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.LabsValues;

import appeng.api.config.LockCraftingMode;
import appeng.client.gui.widgets.ITooltip;

public class LabsImplGuiLockedLabel extends GuiLabel implements ITooltip {

    private static final int ICON_IDX = 9;
    private static final int ICON_SIZE = 16;
    private static final ResourceLocation ICONS = new ResourceLocation(LabsValues.AE2_MODID,
            "textures/guis/states.png");

    private static final Translatable DISPLAY = translatable("gui.appliedenergistics2.CraftingLock");
    private static final Map<LockCraftingMode, Translatable> tooltips;

    private LockCraftingMode mode;

    static {
        tooltips = new HashMap<>();

        // Skip NONE, do not display
        tooltips.put(LockCraftingMode.LOCK_WHILE_LOW, translatable("gui.appliedenergistics2.LowRedstoneLock"));
        tooltips.put(LockCraftingMode.LOCK_WHILE_HIGH, translatable("gui.appliedenergistics2.HighRedstoneLock"));
        tooltips.put(LockCraftingMode.LOCK_UNTIL_PULSE, translatable("gui.appliedenergistics2.UntilPulseUnlock"));
        tooltips.put(LockCraftingMode.LOCK_UNTIL_RESULT, translatable("gui.appliedenergistics2.ResultLock"));
    }

    public LabsImplGuiLockedLabel(FontRenderer fontRenderer, int x, int y, LockCraftingMode mode) {
        super(fontRenderer, 0, x, y, ICON_SIZE, ICON_SIZE, 0);
        this.mode = mode;
    }

    @Override
    public void drawLabel(@NotNull Minecraft mc, int mouseX, int mouseY) {
        if (!visible || mode == null || !tooltips.containsKey(mode)) return;

        mc.renderEngine.bindTexture(ICONS);
        GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);

        int uv_y = (int) Math.floor(ICON_IDX / 16f);
        int uv_x = ICON_IDX - uv_y * 16;
        drawTexturedModalRect(x, y, uv_x * ICON_SIZE, uv_y * ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    @Override
    public String getMessage() {
        if (mode == null || !tooltips.containsKey(mode)) return null;

        return DISPLAY.translate() + "\n" + tooltips.get(mode).translate();
    }

    public void set(LockCraftingMode e) {
        if (mode != e) {
            mode = e;
        }
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
