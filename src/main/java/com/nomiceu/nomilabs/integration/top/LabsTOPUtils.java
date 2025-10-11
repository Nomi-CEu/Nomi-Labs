package com.nomiceu.nomilabs.integration.top;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.LabsTranslate;

import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
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

    @SideOnly(Side.CLIENT)
    @Nullable
    public static Fluid getFluid(String fluidName, String packet) {
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) {
            NomiLabs.LOGGER.error("Received Fluid Info Packet {} with Unknown Fluid {}!", packet, fluidName);
            return null;
        }
        return fluid;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    public static String translateFluid(@Nullable String fluidName, @Nullable Fluid fluid, int amount) {
        if (fluidName == null || fluidName.isEmpty()) return ""; // Empty Tank

        // At least try and translate it if fluid is null
        if (fluid == null) {
            return LabsTranslate.translate(fluidName);
        }

        return fluid.getLocalizedName(new FluidStack(fluid, amount));
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFluidAtlasSprite(@Nullable Fluid fluid) {
        if (fluid == null) return null;

        String actualLocation = fluid.getStill().toString();

        // Gregtech fluids added by GRS do this for some reason
        // As a consequence the fluid texture from /dull will not show up on anything from GRS.
        if (actualLocation.contains("material_sets/fluid/") &&
                (actualLocation.contains("/gas") || actualLocation.contains("/plasma"))) {
            actualLocation = actualLocation.replace("material_sets/fluid/", "material_sets/dull/");
        }

        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(actualLocation);
    }

    public static void addProperties(ProbeMode mode, IProbeInfo info, boolean locked, boolean voiding) {
        if (!(locked || voiding)) return;

        boolean expand = mode == ProbeMode.EXTENDED;
        ILayoutStyle style = new LayoutStyle()
                .spacing(LabsPropertyElement.ICON_BUFFER);

        IProbeInfo panel;
        if (expand) {
            panel = info.vertical(style);
        } else {
            panel = info.horizontal(style);
        }

        if (locked) {
            panel.element(new LabsPropertyElement(0, expand));
        }

        if (voiding) {
            panel.element(new LabsPropertyElement(1, expand));
        }
    }
}
