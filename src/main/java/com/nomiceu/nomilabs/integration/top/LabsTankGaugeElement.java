package com.nomiceu.nomilabs.integration.top;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;
import io.github.drmanganese.topaddons.reference.Colors;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

/**
 * Our improved version of {@link io.github.drmanganese.topaddons.elements.ElementTankGauge}.
 */
public class LabsTankGaugeElement implements IElement {

    // From TOP Addons. Only used as backup if TOP Addons is not loaded.
    public static final Map<String, Integer> FLUID_NAME_COLOR_MAP;
    public static int BAR_WIDTH = 100;
    public static int BAR_NORMAL = 8;
    public static int BAR_EXPANDED = 12;

    public static int DEFAULT_OUTLINE = 0xff969696;
    public static int DEFAULT_FILL = 0x44969696;

    /* Input Values */
    private final String fluidName;
    private final String tankName;

    private final int color;

    private final int amount;
    private final int capacity;

    private final boolean expandedView;

    /* Cached Values */
    @Nullable
    private Fluid fluid;

    @Nullable
    private TextureAtlasSprite sprite;

    @Nullable
    private String formattedTitle;

    @Nullable
    private String formattedAmount;

    static {
        FLUID_NAME_COLOR_MAP = new HashMap<>();
        FLUID_NAME_COLOR_MAP.put("canolaoil", 0xffb9a12d);
        FLUID_NAME_COLOR_MAP.put("refinedcanolaoil", 0xff51471a);
        FLUID_NAME_COLOR_MAP.put("crystaloil", 0xff783c22);
        FLUID_NAME_COLOR_MAP.put("empoweredoil", 0xffd33c52);
        FLUID_NAME_COLOR_MAP.put("water", 0xff345fda);
        FLUID_NAME_COLOR_MAP.put("lava", 0xffe6913c);
    }

    public LabsTankGaugeElement(@Nullable FluidStack fluid, String tankName, int capacity, boolean expandedView) {
        this.tankName = tankName;
        this.capacity = capacity;
        this.expandedView = expandedView;

        if (capacity > 0 && fluid != null && !fluid.getFluid().getName().isEmpty()) {
            fluidName = fluid.getFluid().getName();
            amount = fluid.amount;
            color = getColor(fluid);
        } else {
            fluidName = "";
            amount = 0;
            color = DEFAULT_OUTLINE;
        }
    }

    public LabsTankGaugeElement(ByteBuf byteBuf) {
        fluidName = NetworkTools.readString(byteBuf);
        amount = byteBuf.readInt();

        capacity = byteBuf.readInt();

        tankName = NetworkTools.readString(byteBuf);
        expandedView = byteBuf.readBoolean();

        color = byteBuf.readInt();

        if (hasFluid()) {
            fluid = LabsTOPUtils.getFluid(fluidName, "LabsTankGaugeElement");
            sprite = LabsTOPUtils.getFluidAtlasSprite(fluid);
        } else {
            fluid = null;
            sprite = null;
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        NetworkTools.writeString(byteBuf, fluidName);
        byteBuf.writeInt(amount);

        byteBuf.writeInt(capacity);

        NetworkTools.writeString(byteBuf, tankName);
        byteBuf.writeBoolean(expandedView);

        byteBuf.writeInt(color);
    }

    @Override
    public void render(int x, int y) {
        boolean hasFluid = hasFluid();
        boolean expand = shouldExpand();
        int barHeight = expand ? BAR_EXPANDED : BAR_NORMAL;

        // Box
        RenderHelper.drawThickBeveledBox(x, y, x + BAR_WIDTH, y + barHeight, 1,
                color, color, DEFAULT_FILL);

        // Render fluid (Adaptation of RenderUtil#drawFluidForGui)
        if (hasFluid) {
            renderFluidTexture(x, y, barHeight);
        }

        // Line Segments
        for (int i = 1; i < 10; i++) {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? barHeight - 1 : barHeight / 2),
                    color);
        }

        if (expand) {
            ElementTextRender.render(formattedAmount(), x + 3, y + 2);
            ElementTextRender.render(formattedTitle(), x + 1, y + 14);
        } else {
            LabsTOPUtils.renderSmallText(tankName(), x + 2, y + 2);
        }
    }

    @Override
    public int getWidth() {
        return shouldExpand() ?
                Math.max(barWidth(), Minecraft.getMinecraft().fontRenderer.getStringWidth(formattedTitle())) :
                barWidth();
    }

    private int barWidth() {
        return BAR_WIDTH;
    }

    @Override
    public int getHeight() {
        return (shouldExpand() ? 25 : BAR_NORMAL);
    }

    @Override
    public int getID() {
        return LabsTOPManager.TANK_GAUGE_ELEMENT;
    }

    /**
     * Adapted from {@link Colors#getHashFromFluid(FluidStack)}.
     */
    private int getColor(FluidStack stack) {
        /*
         * Fluid color: - If the fluid doesn't return white in getColor, use this value;
         * - if the fluid's name is stored in {@link Colors.FLUID_NAME_COLOR_MAP}, use that value;
         * - otherwise use default
         */
        Fluid fluid = stack.getFluid();
        if (fluid.getColor(stack) != Color.WHITE.getRGB()) {
            return fluid.getColor(stack);
        } else {
            Map<String, Integer> colorMap;
            if (Loader.isModLoaded(LabsValues.TOP_ADDONS_MODID))
                colorMap = getColorMapTopAddons();
            else
                colorMap = FLUID_NAME_COLOR_MAP;

            return colorMap.getOrDefault(stack.getFluid().getName(), DEFAULT_OUTLINE);
        }
    }

    private Map<String, Integer> getColorMapTopAddons() {
        return Colors.FLUID_NAME_COLOR_MAP;
    }

    private String tankName() {
        return LabsTranslate.translate(tankName) + (hasFluid() ? "" :
                " " + LabsTranslate.translate("nomilabs.gui.top.tank.empty"));
    }

    private String formattedTitle() {
        if (formattedTitle != null) return formattedTitle;

        formattedTitle = tankName() + ": " +
                LabsTOPUtils.translateFluid(fluidName, fluid, amount);
        return formattedTitle;
    }

    private String formattedAmount() {
        if (formattedAmount != null) return formattedAmount;

        formattedAmount = TextFormattingUtil.formatLongToCompactString(amount) + "L" + " / " +
                TextFormattingUtil.formatLongToCompactString(capacity) + "L";
        return formattedAmount;
    }

    private boolean shouldExpand() {
        return expandedView && hasFluid();
    }

    private boolean hasFluid() {
        return fluidName != null && !fluidName.isEmpty();
    }

    private void renderFluidTexture(int x, int y, int barHeight) {
        if (sprite == null) return;

        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtil.setGlColorFromInt(color, 0xFF);

        int scaledAmount = (int) ((long) amount * (BAR_WIDTH - 2) / capacity);

        int xTileCount = scaledAmount / 16;
        int xRemainder = scaledAmount - xTileCount * 16;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            int width = xTile == xTileCount ? xRemainder : 16;
            int fluidX = x + 1 + (xTile + 1) * 16 - 16;
            if (width > 0) {
                int maskTop = 16 - barHeight + 2;
                int maskRight = 16 - width;

                RenderUtil.drawFluidTexture(fluidX, y - 16 + barHeight - 1, sprite, maskTop, maskRight, 0.0);
            }
        }

        GlStateManager.disableBlend();
    }
}
