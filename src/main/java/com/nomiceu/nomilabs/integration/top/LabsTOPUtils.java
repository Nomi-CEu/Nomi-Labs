package com.nomiceu.nomilabs.integration.top;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcjty.theoneprobe.rendering.RenderHelper;

public class LabsTOPUtils {

    private static final DecimalFormat format = new DecimalFormat("#.#");

    @SideOnly(Side.CLIENT)
    public static void renderSmallText(String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 1);
        RenderHelper.renderText(Minecraft.getMinecraft(), x * 2, y * 2, text);
        GlStateManager.popMatrix();
    }

    public static String formatChance(int chance) {
        return format.format(chance / 100.0) + "%";
    }

    @SideOnly(Side.CLIENT)
    public static void renderChance(int chance, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 1);
        Minecraft mc = Minecraft.getMinecraft();

        String chanceTxt = formatChance(chance);
        RenderHelper.renderText(mc, (x + 17) * 2 - 1 - mc.fontRenderer.getStringWidth(chanceTxt),
                y * 2, chanceTxt);

        GlStateManager.popMatrix();
    }
}
